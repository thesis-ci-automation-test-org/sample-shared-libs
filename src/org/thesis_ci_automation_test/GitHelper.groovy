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
    def str = ""
    def changeLogSets = build.changeSets
    
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            str += "- ${entry.msg} [${entry.author}]\n"
        }
    }

    if (!str) {
        return "No Changes."
    } else {
        str = "${build.getFullDisplayName()} Changes:\n" + str
    }

    return str    
}

return this
