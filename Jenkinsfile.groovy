pipeline {
  agent any

  environment {
    DOCKER_REGISTRY = 'https://index.docker.io/v1/'
    SECRET_TEXT = credentials('Docker')
  }
  
  stages {
    stage('Pull base image') {
      steps {
        withCredentials([string(credentialsId: 'Docker', variable : 'SECRET_TEXT')]) {
          sh 'docker login -u dvirlabs -p $SECRET_TEXT'
          sh 'docker pull ${MY_DOCKERHUB_REGISTRY}/jenkins-httpd:v1'
        }
      }
    }
    stage('Build new image') {
      steps {
        script {
          sh 'docker build -t ${MY_DOCKERHUB_REGISTRY}/jenkins-httpd:v${BUILD_NUMBER} .'
        }
      }
    }

    stage('Push image') {
      steps {
        script {
          sh 'docker push ${MY_DOCKERHUB_REGISTRY}/jenkins-httpd:v${BUILD_NUMBER}'
        }
      }
    }
  }

}
