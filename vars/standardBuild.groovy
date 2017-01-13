#!/usr/bin/env groovy
import org.thesis_ci_automation_test.*

// This is an example of a highly standardized Pipeline
// See https://github.com/jenkinsci/workflow-cps-global-lib-plugin
//
// In a project's Jenkinsfile, this should be used like:
//
// @Library('this-library') _
// standardBuild {
//   projectName = 'my-project'
// }

// Using Pipeline libraries, all libraries' vars/*.groovy
// scripts with call-methods are exposed as [fileName] functions.
def call(body) {
  // Read the body-closure's content
  // into config and evaluate it (body()).
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  // ACTUAL BUILD BEGINS

  def slack = new SlackNotifier()
  def utils = new Utils()

  def dockerEnv = null
  def dockerEnvArgs = '-v /var/run/docker.sock:/var/run/docker.sock'

  // Keep only last 5 builds
  properties([buildDiscarder(logRotator(numToKeepStr: '5'))])

  // In regular Jenkinsfile (not declarative), we need to
  // manually manage errors and post-actions,
  // so wrap everything in try-catch-finally.
  try {

    ansiColor('xterm') {
      node {
        stage('Checkout') {
          deleteDir()
          checkout scm
        }

        // This image will be re-used later, so save a reference
        dockerEnv = docker.build("${config.projectName}_build", '-f Dockerfile.test .')
          .withRegistry('http://my-registry:8082', 'docker-registry-creds')
        dockerEnv.inside(dockerEnvArgs) {

          stage('Build') {
            sh 'npm run dependencies'
          }

          stage('Test') {
            try {
              parallel 'Unit tests': {
                sh 'grunt unit'
              }, 'Smoke tests': {
                sleep 10
                echo 'Do some rudimentary smoke tests here'
              }
            } finally {
              // Test results should always be saved (or attempted)
              junit 'test-results/**/unit-test-results.xml'
            }
          }

          stage('Prepare dev deploy') {
            // Deployment's should only be made from the dev branch.
            // Blue Ocean will also mark this stage "Skipped",
            // as there are no steps executed in else-case.
            //
            // This is clearer than skipping whole stages,
            // as then they would not be rendered at all.
            if (env.BRANCH_NAME == 'dev') {
              milestone 1
              sh 'npm run build:dev'
            }
          }

          stage('Development deploy') {
            if (env.BRANCH_NAME == 'dev') {
              milestone 2
  
              // We should only allow a single deploy at a time
              lock(resource: 'dev-server', inversePrecedence: true) {
                milestone 3
                sh './deploy.dev.sh'
              }
            }
          }

        }
      }

      stage('Accept production deploy') {
        if (env.BRANCH_NAME == 'master') {
          milestone 4

          // As there's currently no good way to visualize
          // pending inputs, we need to manually notify users.
          slack.sendMessage(
            SlackColours.GOOD.colour,
            "${currentBuild.getFullDisplayName()} - Waiting for input (${utils.getBuildLink(env)})"
          )
          input 'Deploy to production?'

          // When a milestone is passed, no currently running
          // other job can pass the same milestone,
          // and will be cancelled.
          //
          // This is used in combination with input to only allow
          // the selected build to deploy.
          milestone 5
        }
      }

      node {
        // Re-use the previously created Docker image
        dockerEnv.inside(dockerEnvArgs) {

          stage('Prepare production deploy') {
            // Production deploys should only be made from master
            if (env.BRANCH_NAME == 'master') {
              sh 'npm run build:prod'
            }
          }

          stage('Production deploy') {
            if (env.BRANCH_NAME == 'master') {
              milestone 6
              lock(resource: 'prod-server', inversePrecedence: true) {
                milestone 7
                sh './deploy.prod.sh'
              }
            }
          }

        }
      }
    }

  } catch (err) {
    // TODO: Handle ABORTED separately
    currentBuild.result = 'FAILURE'
    throw err
  } finally {
    // Use a separate stage for visualization purposes,
    // otherwise these steps won't be shown in Blue Ocean (at least currently).
    stage('Post build actions') {
      slack.notify(currentBuild, currentBuild.result, env)
    }
  }
}
