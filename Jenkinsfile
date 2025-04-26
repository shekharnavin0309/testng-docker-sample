pipeline {
  agent any

  environment {
    MVN = 'mvn -B -V'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build & Test') {
      steps {
        sh "${MVN} clean test site"
      }
      post {
        always {
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Publish HTML Report') {
      steps {
        // publish the Maven Surefire site report:
        publishHTML([
          reportName: 'Surefire Site Report',
          reportDir: 'target/site', 
          reportFiles: 'surefire-report.html',
          keepAll: true,
          alwaysLinkToLastBuild: true,
          allowMissing: false
        ])

        // publish TestNG’s default HTML (if you prefer that):
        //publishHTML([
        //  reportName: 'TestNG HTML Report',
        //  reportDir: 'test-output',
        //  reportFiles: 'index.html',
        //  keepAll: true,
        //  alwaysLinkToLastBuild: true,
        //  allowMissing: false
        //])
      }
    }
  }

  post {
    always { cleanWs() }
    success { echo '✅ Done!' }
    failure { echo '❌ Build failed.' }
  }
}
