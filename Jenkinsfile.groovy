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

  // Move the credentials section here
  credentials {
    usernamePassword(credentialsId: 'Docker', username: 'USERNAME', password: 'USERNAME')
  }

  environment {
    DOCKER_REGISTRY = 'https://index.docker.io/v1/'
  }
}
