#!groovy

if (env.GERRIT_API_URL == null) {
  this.gerritComment = { dict -> }
  this.gerritReview = { dict -> }
}

node {
  stage('checkout') {
    checkout scm
  }
  stage('clean') {
    sh 'mvn -B clean'
  }
  stage('test') {
    sh 'mvn -B compile'
  }
}

// gerritReview labels: [:], message: "Build started ${env.BUILD_URL} doing nothing."

