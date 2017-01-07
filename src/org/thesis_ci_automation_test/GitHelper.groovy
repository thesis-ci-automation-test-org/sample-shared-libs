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
    
    build.changeSets.each { set ->
        set.items.each {
            str+= "- ${it.msg} [${it.author}]\n"
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
