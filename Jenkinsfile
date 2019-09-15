#!groovy

// This is the same file in all argouml projects that use maven. The
// process to do changes in this file is:
// 1. Make the change in the argouml project.
// 2. Push the change to gerrit to get it verified, approved and merged.
// 3. Copy the new version of the file into all other projects and push
//    it to gerrit to get it verified, approved and merged in each of
//    the other projects.
//
// The file contains some assumptions on how the build machine is set up.
// See https://github.com/argouml-tigris-org/argouml/wiki/Jenkins-configuration

if (env.GERRIT_API_URL == null) {
  this.gerritComment = { dict -> }
  this.gerritReview = { dict -> }
}

pipeline {
  agent {
      docker {
        image 'maven:3-ibmjava-8'
        args '-v maven-repo:/.m2 -v maven-repo:/root/.m2'
      }
  }
  environment {
    GERRIT_CREDENTIALS_ID = 'gerrithub-user'
  }
  stages {
    stage('compile') {
      steps {
        gerritReview labels: [Verified:0], message: """Build starts.

Build has two steps:
1. Compile (corresponding to mvn compile).
2. Run tests (corresponding to mvn test).

If these are all successful, it is scored as Verified."""
        timeout(time:1, unit: 'HOURS') {
          withMaven() {
            sh 'mvn -B compile'
          }
        }
        gerritReview labels: [:], message: "Compiled without error."
      }
    }
    stage('test') {
      steps {
        timeout(time:1, unit: 'HOURS') {
          withMaven() {
            sh 'mvn -B test'
          }
        }
        gerritReview labels: [:], message: "Tests run without error."
      }
    }
  }
  post {
    success { gerritReview labels: [Verified: 1], message: 'Build succeeded.' }
    unstable { gerritReview labels: [Verified: 0], message: 'Build is unstable' }
    failure { gerritReview labels: [Verified: -1], message: 'Build failed.' }
  }
}
