# sample-shared-libs
Sample of Jenkins Pipeline Shared Libraries

## Usage

```groovy
@Library('github.com/thesis-ci-automation-test-org/sample-shared-libs@master') // Make library usable for this pipeline
import org.thesis_ci_automation_test.* // Import to use a simpler namespace in code

node {
  stage('Build') {
    checkout scm
    echo GitHelper.getChangeLogString(this) // Use a static method
  }
}
```

## Development

Add all libraries under `src/`, like any Java/Groovy library.

