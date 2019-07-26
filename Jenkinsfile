#!groovy

// This is the same file in all argouml projects.

if (env.GERRIT_API_URL == null) {
  this.gerritComment = { dict -> }
  this.gerritReview = { dict -> }
}

pipeline {
  agent {
      docker {
        image 'maven:3-ibmjava-8'
        args '-v maven-repo:/.m2'
      }
  }
  environment {
    GERRIT_CREDENTIALS_ID = 'gerrithub-user'
  }
  stages {
    stage('compile') {
      steps {
        gerritReview labels: [:], message: """Build starts.

Build has two steps:
1. Compile (corresponding to mvn compile).
2. Run tests (corresponding to mvn test).

If these are all successful, it is scored as Verified."""
        sh 'mvn -B compile'
        gerritReview labels: [:], message: "Compile done."
      }
    }
    stage('test') {
      steps {
        sh 'mvn -B test'
        gerritReview labels: [:], message: "Run tests done."
      }
    }
  }
  post {
    success { gerritReview score:1 }
    failure { gerritReview score:-1 }
  }
}
