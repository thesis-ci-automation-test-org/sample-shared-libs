#!/usr/bin/env groovy
package org.thesis_ci_automation_test

static def getSlackColour(result = 'FAILURE') {
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
static def notify(script, steps, result) {
    result = result ?: 'SUCCESS' // null result means success
    def msg = "${script.currentBuild.getFullDisplayName()}"
    def colour = SlackNotifier.getSlackColour(result)

    switch (result) {
        case 'ABORTED':
            // Don't send any notifications, bail out
            return
        case 'FAILURE':
            msg += " - Build failed!"
            break
        case 'SUCCESS':
            msg += " - Build successful"
            break
        default:
            msg += " - Build status unknown, see logs"
            steps.echo "SlackNotifier: Did not recognize build status code: ${result}"
    }

    msg += " (<${script.env.BUILD_URL}|Open>)"

    // TODO: Enable when test results are accessible from JUnit
    //msg += "\nTest Status:\n"
    //msg += "Passed: TODO, Failed: TODO, Skipped: TODO"
    
    // Include Git changelog
    msg += "\n${GitHelper.getChangeLogString(script)}"

    // NOTE: For some reason, calling slackSend stops execution in this thread
    // and try-catch does not see it. So, we can only send one message.
    SlackNotifier.sendMessage(steps, colour.colour, msg)
}

static def sendMessage(steps, colour, msg) {
    steps.slackSend color: colour, message: msg
}

return this

