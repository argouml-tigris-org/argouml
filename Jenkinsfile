#!groovy

if (env.GERRIT_API_URL == null) {
  this.gerritComment = { dict -> }
  this.gerritReview = { dict -> }
}

gerritReview labels: [:], message: "Build started ${env.BUILD_URL} doing nothing."
