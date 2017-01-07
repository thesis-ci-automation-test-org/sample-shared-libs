#!/usr/bin/env groovy
package org.thesis_ci_automation_test

/**
 * Notify Slack about build results
 * @param build Current build
 * @param result Result string TODO: Use actual result enum
 */
@NonCPS
static def notify(build, result = 'FAILURE') {
    def msg = "${build.getFullDisplayName()} - Build failed!"
    def color = "danger"
    if (result == 'SUCCESS') {
        msg = "${build.getFullDisplayName()} - Build successful"
        color = "good"
    }

    msg += " (<${env.BUILD_URL}|Open>)"

    msg += "\nTest Status:\n"
    msg += "Passed: TODO, Failed: TODO, Skipped: TODO"

    slackSend color: color, message: msg
    slackSend color: color, message: GitHelper.getChangeLogString(build)
}

return this
