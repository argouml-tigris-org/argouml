#!groovy

if (env.GERRIT_API_URL == null) {
  this.gerritComment = { dict -> }
  this.gerritReview = { dict -> }
}

pipeline {
  agent {
      docker { image 'java:oracle-java8' }
  }
  stages {
    stage('checkout') {
      checkout scm
    }
    stage('compile') {
      sh 'mvn -B compile'
    }
    stage('test') {
      sh 'mvn -B test'
    }
  }
}

// gerritReview labels: [:], message: "Build started ${env.BUILD_URL} doing nothing."

