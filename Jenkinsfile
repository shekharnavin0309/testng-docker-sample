pipeline {
  agent any                           // ensures everything (including post) runs on a node

  environment {
    MVN_OPTS = '-B -V'                // batch mode + show versions
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        // will use whatever `mvn` is on PATH
        sh "mvn ${MVN_OPTS} clean test"
      }
      post {
        always {
          // publish JUnit result regardless of pass/fail
          junit '**/target/surefire-reports/*.xml' 
        }
      }
    }

    stage('Package') {
      steps {
        sh "mvn ${MVN_OPTS} package"
      }
      post {
        success {
          archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
      }
    }
  }

  post {
    always {
      cleanWs()                       // runs on whatever node ran your stages
    }
    success {
      echo '✅ Pipeline succeeded!'
    }
    failure {
      echo '❌ Pipeline failed—see above logs.'
    }
  }
}
