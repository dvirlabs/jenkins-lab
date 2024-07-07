pipeline {
  agent any

  stages {
    stage('Pull base image') {
      steps {
        // sh "docker login -u dvirlabs -p ${DOCKER_TOKEN}"
        docker.image('dvirlabs/jenkins-httpd:v1').pull() 
      }
    }
    stage('Build new image') {
      steps { 
        script {
          def image = docker.build(imageName: "dvirlabs/jenkins-httpd:${env.BUILD_ID}", credentialsId: 'Docker')
          image.push()
        }
      }
    }
  }

  environment {
    DOCKER_REGISTRY = 'https://index.docker.io/v1/'
    // DOCKER_TOKEN = creden
  }
}
