#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper

/**
 * Get Git changelog log for current build
 * as a multiline string.
 *
 * Includes commit messages and authors.
 * @param build Current build
 * @return String
 */
@NonCPS
static def getChangeLogString(RunWrapper build) {
    def str = build.changeSets.inject("") { result, set ->
        result += set.items.inject("") { itemResult, item ->
            itemResult += "- ${item.msg} [${item.author}]\n"
            itemResult
        }
        result
    }
    
    if (!str) {
        return "No Changes."
    } else {
        str = "${build.getFullDisplayName()} Changes:\n" + str
    }

    return str    
}

return this
