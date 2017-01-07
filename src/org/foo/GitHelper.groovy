#!/usr/bin/env groovy
package org.foo

/**
 * Get Git changelog log for current build
 * as a multiline string.
 *
 * Includes commit messages and authors.
 * @return String
 */
static def getChangeLogString() {
    def str = ""
    def changeLogSets = currentBuild.changeSets
    
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
        str = "${currentBuild.getFullDisplayName()} Changes:\n" + str
    }

    return str    
}

return this
