#!/usr/bin/env groovy
package org.thesis_ci_automation_test

/**
 * Get Git changelog log for current build
 * as a multiline string.
 *
 * Includes commit messages and authors.
 * @param script Instance of pipeline script
 * @return String
 */
@NonCPS
static def getChangeLogString(script) {
    def str = ""
 
    script.currentBuild.changeSets.each { set ->
        set.items.each { item ->
            str+= "- ${item.msg} [${item.author}]\n"
        }
    }

    if (!str) {
        return "No Changes."
    } else {
        str = "${script.currentBuild.getFullDisplayName()} Changes:\n" + str
    }

    return str    
}

return this
