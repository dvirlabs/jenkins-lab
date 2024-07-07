pipeline {
  agent any

  stages {
    stage('Pull base image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'Docker', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh 'docker pull dvirlabs/jenkins-httpd:v1'
        }
      }
    }
    stage('Build new image') {
      steps {
        script {
          def image = docker.build(imageName: "dvirlabs/jenkins-httpd:${env.BUILD_ID}")
          image.push()
        }
      }
    }
  }

  credentials {
    usernamePassword(credentialsId: 'docker-hub-credentials', username: 'USERNAME', password: 'PASSWORD')
  }

  environment {
    DOCKER_REGISTRY = 'https://index.docker.io/v1/'
  }
}
