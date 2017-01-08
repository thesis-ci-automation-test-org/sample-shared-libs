#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import hudson.model.Result

/**
 * Notify Slack about build results
 * @param script Instance of pipeline script
 * @param result Result string TODO: Use actual result enum
 */
@NonCPS
static def notify(script, steps, result = 'FAILURE') {
    def msg = "${script.currentBuild.getFullDisplayName()}"
    def color = "danger"

    switch (result) {
        case Result.FAILURE:
            msg += " - Build failed!"
            color = SlackColours.DANGER
            break
        case Result.SUCCESS:
            msg += " - Build successful"
            color = SlackColours.GOOD
        default:
            color = SlackColours.WARNING
            break
    }

    msg += " (<${script.env.BUILD_URL}|Open>)"

    // TODO: Enable when test results are accessible from JUnit
    //msg += "\nTest Status:\n"
    //msg += "Passed: TODO, Failed: TODO, Skipped: TODO"

    steps.slackSend color: color.toString(), message: msg
    steps.slackSend color: color.toString(), message: GitHelper.getChangeLogString(script)
}

return this
