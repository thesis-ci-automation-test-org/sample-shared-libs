# sample-shared-libs

Sample of Jenkins Pipeline Shared Libraries

See [official documentation for more](https://github.com/jenkinsci/workflow-cps-global-lib-plugin/blob/master/README.md)

## Usage

### Of a standardized build / globals

```groovy
@Library('thesisSampleLib') _

standardBuild {
  projectName = 'my-project-name'
}

```

### Of modules

```groovy
// Make library usable for this pipeline
@Library('thesisSampleLib')
// Import to use a simpler namespace in code
import org.thesis_ci_automation_test.*

node {
  stage('Build') {
    checkout scm
    echo GitHelper.getChangeLogString(this) // Use a static method
  }
}
```

## Development

Add all modules under `src/`, like any Java/Groovy library.

Global pipeline methods should be placed inside `vars/`.

