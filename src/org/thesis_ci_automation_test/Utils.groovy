#!/usr/bin/env groovy
package org.thesis_ci_automation_test

// Sample utility methods for Jenkins pipelines

def getBuildLink(env) {
  return "<${env.BUILD_URL}|Open>"
}

@NonCPS
def getFailedTestCount(build) {
  def testResultAction = build.rawBuild.getAction(AbstractTestResultAction.class)
  if (testResultAction == null ) {
    return 0
  }
  return testResultAction.failCount
}

