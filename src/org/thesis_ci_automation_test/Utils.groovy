#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import hudson.tasks.test.AbstractTestResultAction

// Sample utility methods for Jenkins pipelines

def getBuildLink(env) {
  return "<${env.BUILD_URL}|Open>"
}

@NonCPS
def getTestCounts(build) {
  def res = build.rawBuild.getAction(AbstractTestResultAction.class)
  if (res == null ) {
    return 'No test results found'
  }
  return "Passed: ${res.totalCount - res.failCount}, Failed: ${res.failCount}, Skipped: ${res.skipCount}"
}

