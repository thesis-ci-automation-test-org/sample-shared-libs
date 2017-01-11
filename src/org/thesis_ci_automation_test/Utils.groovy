#!/usr/bin/env groovy
package org.thesis_ci_automation_test

def getBuildLink(env) {
    return "<${env.BUILD_URL}|Open>"
}

