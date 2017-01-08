#!/usr/bin/env groovy
package org.thesis_ci_automation_test

/**
 * Notify Slack about build results
 *
 * TODO: Use actual result enum, when whitelisted in scripts
 * @param script Instance of pipeline script
 * @param result Result string
 */
@NonCPS
static def notify(script, steps, result = 'FAILURE') {
    def msg = "${script.currentBuild.getFullDisplayName()}"
    def colour = "danger"

    switch (result) {
        case 'FAILURE':
            msg += " - Build failed!"
            colour = SlackColours.DANGER
            break
        case 'SUCCESS':
        default:
            msg += " - Build successful"
            colour = SlackColours.GOOD
    }

    msg += " (<${script.env.BUILD_URL}|Open>)"

    // TODO: Enable when test results are accessible from JUnit
    //msg += "\nTest Status:\n"
    //msg += "Passed: TODO, Failed: TODO, Skipped: TODO"

    steps.slackSend color: colour.colour, message: msg
    steps.slackSend color: colour.colour, message: GitHelper.getChangeLogString(script)
}

return this
