#!/usr/bin/env groovy
package org.thesis_ci_automation_test

// Sample utility methods for Jenkins pipelines

def getBuildLink(env) {
    return "<${env.BUILD_URL}|Open>"
}

