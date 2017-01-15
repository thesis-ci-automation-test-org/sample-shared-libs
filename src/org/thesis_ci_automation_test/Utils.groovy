#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import hudson.tasks.test.AbstractTestResultAction

// Sample utility methods for Jenkins pipelines

/**
 * Get build link for Slack
 *
 * @param env Current build's environment map
 * @returns String Slack-formatted link
 */
def getBuildLink(env) {
  return "<${env.BUILD_URL}|Open>"
}

/**
 * Get test count message for notifications
 *
 * @param build Current build
 * @returns String Test counts or an empty state message if no test results recorded
 */
@NonCPS
def getTestCounts(build) {
  def res = build.rawBuild.getAction(AbstractTestResultAction.class)
  if (res == null ) {
    return 'No test results found'
  }
  return "Passed: ${res.totalCount - res.failCount}, Failed: ${res.failCount}, Skipped: ${res.skipCount}"
}

