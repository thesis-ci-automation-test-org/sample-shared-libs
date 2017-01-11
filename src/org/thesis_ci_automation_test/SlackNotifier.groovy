#!/usr/bin/env groovy
package org.thesis_ci_automation_test

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
 * TODO: Use actual result enum, when whitelisted in scripts
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
      msg += ' - Build failed!'
      break
    case 'SUCCESS':
      msg += ' - Build successful'
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

def sendMessage(colour, msg) {
  slackSend color: colour, message: msg
}

