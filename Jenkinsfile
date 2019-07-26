#!groovy

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
    stage('checkout') {
      steps {
        gerritReview labels: [:], message: "Build started ${env.BUILD_URL}."
        checkout scm
        gerritReview labels: [:], message: "Checkout done."
      }
    }
    stage('compile') {
      steps {
        gerritReview labels: [:], message: "Compile starting."
        sh 'mvn -B compile'
        gerritReview labels: [:], message: "Compile done."
      }
    }
    stage('test') {
      steps {
        gerritReview labels: [:], message: "Test starting."
        sh 'mvn -B test'
        gerritReview labels: [:], message: "Test done."
      }
    }
  }
  post {
    success { gerritReview score:1 }
    failure { gerritReview score:-1 }
  }
}
