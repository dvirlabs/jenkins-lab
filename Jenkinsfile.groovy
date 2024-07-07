pipeline {
  agent any

  stages {
    stage('Pull base image') {
      steps {
        // This line is corrected
        docker.image('dvirlabs/jenkins-httpd:v1').pull()
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
  }
}
