#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import hudson.tasks.test.AbstractTestResultAction
import org.jenkinsci.plugins.displayurlapi.DisplayURLProvider

// Sample utility methods for Jenkins pipelines

/**
 * Get build link for Slack
 *
 * @param build Current build
 * @returns String Slack-formatted link
 */
def getBuildLink(build) {
  return "<${DisplayURLProvider.get().getRunURL(currentBuild.getRawBuild()}|Open>"
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

