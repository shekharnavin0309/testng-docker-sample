// Jenkinsfile

pipeline {
  agent any

  tools {
    // name must match what you configured under Manage Jenkins → Global Tool Configuration
    jdk 'OpenJDK 11'
    maven 'Maven 3.8'
  }

  environment {
    // any env vars you need
    MVN_OPTS = '-B'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh 'mvn $MVN_OPTS clean compile'
      }
    }

    stage('Unit Tests') {
      steps {
        sh 'mvn $MVN_OPTS test'
        junit '**/target/surefire-reports/*.xml'
      }
    }

    stage('Build Docker') {
      when { expression { fileExists('Dockerfile') } }
      steps {
        script {
          docker.build("my-app:${env.BUILD_NUMBER}")
        }
      }
    }

    stage('Publish Artifacts') {
      steps {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }
  }

  post {
    always {
      cleanWs()
    }
    success {
      echo '🎉 Build succeeded!'
    }
    failure {
      echo '🔥 Build failed!'
    }
  }
}
