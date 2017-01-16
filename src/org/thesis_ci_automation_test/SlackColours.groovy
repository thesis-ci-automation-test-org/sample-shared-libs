#!/usr/bin/env groovy
package org.thesis_ci_automation_test

import java.lang.Enum 

// Slack API requires special colour name Strings
// for slackSend.

enum SlackColours {
  GOOD("good"), WARNING("warning"), DANGER("danger")

  private final String colour

  private SlackColours(String colour) {
    this.colour = colour
  }
}

