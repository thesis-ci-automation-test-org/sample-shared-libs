#!/usr/bin/env groovy
package org.thesis_ci_automation_test

// This is sample of a high-level Slack notification
// module for Jenkins pipelines.
//
// Builds can simply call:
//
//   def slack = new SlackNotifier()
//   slack.notify(currentBuild, currentBuild.getResult(), env)
//
// so the necessary logic for the message contents
// are implemented here, and not in every single Jenkinsfile.

/**
 * Standardized wrapper for getting the Slack API colour
 * for a build result code.
 *
 * @param result Build result code
 * @returns SlackColours
 */
def getSlackColour(result) {
  result = result ?: 'SUCCESS' // null result means success
  switch (result) {
    case 'FAILURE':
      return SlackColours.DANGER
    case 'SUCCESS':
      return SlackColours.GOOD
    default:
      return SlackColours.WARNING // Don't know what happened
  }
}

/**
 * Notify Slack about build results
 *
 * @param build Current build
 * @param result Current build result
 * @param env Current build environment
 */
def notify(build, result, env) {
  result = result?.toString() ?: 'SUCCESS' // null means success in Result
  def msg = "${build.getFullDisplayName()}"
  def colour = getSlackColour(result)

  def gitHelper = new GitHelper()
  def utils = new Utils()

  switch (result) {
    case 'ABORTED':
      echo 'Build was aborted, skipping Slack messages'
      break
    case 'FAILURE':
      if (build.previousBuild?.result == 'FAILURE') {
        msg += ' - Build still failing!'
      } else {
        msg += ' - Build failed!'
      }
      break
    case 'SUCCESS':
      if (build.previousBuild?.result == 'FAILURE') {
        msg += ' - Back to normal'
      } else {
        msg += ' - Build successful'
      }
      break
    default:
      msg += ' - Build status unknown, check logs'
      echo "SlackNotifier: Did not recognize build status code: ${result}"
  }

  msg += " (${utils.getBuildLink(env)})"

  // TODO: Enable when test results are accessible from JUnit
  //msg += "\nTest Status:\n"
  //msg += "Passed: TODO, Failed: TODO, Skipped: TODO"

  // Include Git changelog
  msg += "\n${gitHelper.getChangeLogString(build)}"

  // NOTE: For some reason, calling slackSend stops execution in this thread
  // and try-catch does not see it. So, we can only send one message.
  sendMessage(colour.colour, msg)
}

/**
 * Send a freeform Slack message
 *
 * @param colour Colour name
 * @param msg Message to send
 */
def sendMessage(colour, msg) {
  slackSend color: colour, message: msg
}

