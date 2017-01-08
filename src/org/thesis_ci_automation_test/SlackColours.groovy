package org.thesis_ci_automation_test

enum SlackColours {
    GOOD("good"), WARNING("warning"), DANGER("danger")

    SlackColours(String colour) {
        this.colour = colour
    }

    private final String colour
}
