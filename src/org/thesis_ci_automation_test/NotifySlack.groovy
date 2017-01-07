#!/usr/bin/env groovy
package org.thesis_ci_automation_test

/**
 * Notify Slack about build results
 * @param script Instance of pipeline script
 * @param result Result string TODO: Use actual result enum
 */
@NonCPS
static def notify(script, result = 'FAILURE') {
    def msg = "${script.currentBuild.getFullDisplayName()} - Build failed!"
    def color = "danger"
    if (result == 'SUCCESS') {
        msg = "${script.currentBuild.getFullDisplayName()} - Build successful"
        color = "good"
    }

    msg += " (<${script.env.BUILD_URL}|Open>)"

    msg += "\nTest Status:\n"
    msg += "Passed: TODO, Failed: TODO, Skipped: TODO"

    slackSend color: color, message: msg
    slackSend color: color, message: GitHelper.getChangeLogString(script)
}

return this
