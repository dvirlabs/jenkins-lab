pipeline {
  agent any

  stages {
    stage('Pull base image') {
      steps {
        withCredentials([string(credentialsId: 'Docker', variable : 'SECRET_TEXT')]) {
          sh 'docker login -u dvirlabs -p $SECRET_TEXT'
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

  environment {
    DOCKER_REGISTRY = 'https://index.docker.io/v1/'
    SECRET_TEXT = credentials('Docker')
  }
}
