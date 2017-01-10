#!/usr/bin/env groovy
package org.thesis_ci_automation_test

static def getBuildLink(env) {
    return "<${env.BUILD_URL}|Open>"
}

return this

