pipeline {
  agent any

  tools {
    // must match your Global Tool Configuration
    allure 'Allure-2.34.0'
  }

  environment {
    MVN = 'mvn -B -V'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh "${MVN} clean test site"
      }
      post {
        always {
          // publish the XML results to Jenkins
          junit '**/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Publish Reports') {
      steps {
        // 1) Allure Report
        allure([
          results: [[path: 'target/allure-results']],
          reportBuildPolicy: 'ALWAYS'
        ])

        // 2) Surefire Site HTML
        publishHTML([
          reportName: 'Surefire Site Report',
          reportDir:  'target/site',
          reportFiles:'surefire-report.html',
          keepAll:           true,
          alwaysLinkToLastBuild: true,
          allowMissing:      false
        ])

        // 3) TestNG Default HTML
        publishHTML([
          reportName: 'TestNG HTML Report',
          reportDir:  'test-output',
          reportFiles:'index.html',
          keepAll:           true,
          alwaysLinkToLastBuild: true,
          allowMissing:      false
        ])
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
