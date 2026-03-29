pipeline {
    agent any

    tools {
        maven 'apache-maven-3.9.9'
        jdk 'JDK21'
    }

    stages {
    
        stage('Checkout') {
            steps {
            	echo 'Checking out SCM.'
                checkout scm
            }
        }

        stage('Build & Test') {
            steps  {
				echo 'Building and running tests.'
                bat 'mvn clean test'
            }
        }

        stage('Archive Extent Report') {
            steps {
            	echo 'Archiving artifacts.'
                archiveArtifacts artifacts: '**/extentReport*.html', fingerprint: true
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            
            publishHTML([
            	reportDir: 'reports',
            	reportFiles: 'extentReport.html',
            	reportName: 'Extent Test Report',
            	keepAll: true,
            	alwaysLinkToLastBuild: true,
            	allowMissing: true
            ])
        }
    }
}