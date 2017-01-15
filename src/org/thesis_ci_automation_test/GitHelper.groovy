#!/usr/bin/env groovy
package org.thesis_ci_automation_test

// Sample module for Git actions,
// like getting a pretty-printed changelog.

/**
 * Get Git changelog log for current build
 * as a multiline string.
 *
 * Includes commit messages and authors.
 * @param build Current build
 * @returns String
 */
@NonCPS
def getChangeLogString(build) {
  def str = ""

  // NOTE: Includes changes in this library
  // which is probably OK
  currentBuild.changeSets.each { set ->
    set.items.each { item ->
      str+= "- ${item.msg} [${item.author}]\n"
    }
  }

  if (!str) {
    return "No Changes."
  } else {
    str = "Changes:\n" + str
  }

  return str    
}

