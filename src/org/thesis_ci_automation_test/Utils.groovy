#!/usr/bin/env groovy

static def getBuildLink(env) {
    return "<${env.BUILD_URL}|Open>"
}

return this

