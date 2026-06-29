pipeline {
    agent any

    tools {
        maven 'apache-maven-3.9.9'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }

        stage('Start Selenium Grid') {
            steps {
                echo 'Starting Selenium Grid...'

                // Clean up any previous Compose project
                bat 'docker compose down'

                // Start Selenium Grid
                bat 'docker compose up -d'
            }
        }

        stage('Wait for Selenium Grid') {
            steps {
                echo 'Waiting for Selenium Grid to become ready...'

                powershell '''
                $maxRetries = 24
                $retry = 0

                do {
                    Start-Sleep -Seconds 5

                    try {
                        $response = Invoke-RestMethod -Uri "http://localhost:4444/status"

                        if ($response.value.ready -eq $true) {
                            Write-Host "Selenium Grid is READY."
                            exit 0
                        }

                        Write-Host "Grid not ready yet..."
                    }
                    catch {
                        Write-Host "Waiting for Selenium Grid..."
                    }

                    $retry++

                } while ($retry -lt $maxRetries)

                throw "Selenium Grid did not become ready within 2 minutes."
                '''
            }
        }

        stage('Build & Test') {
            steps {
                script {

                    if (env.CHANGE_ID) {

                        echo 'Pull Request detected - Running smoke suite'

                        bat 'mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml'

                    } else if (env.BRANCH_NAME == 'main') {

                        echo 'Main branch detected - Running parallel Grid suite'

                        bat 'mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml'

                    } else {

                        echo 'Feature branch detected - Running test suite'

                        bat 'mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml'

                    }
                }
            }
        }

        stage('Archive Extent Report') {
            steps {
                echo 'Archiving reports...'

                archiveArtifacts artifacts: '**/extentReport*.html', fingerprint: true
            }
        }
    }

    post {

        always {

            echo 'Publishing reports...'

            junit '**/target/surefire-reports/*.xml'

            publishHTML([
                reportDir: 'src/test/resources/reports',
                reportFiles: 'extentReport.html',
                reportName: 'Extent Test Report',
                keepAll: true,
                alwaysLinkToLastBuild: true,
                allowMissing: true
            ])

            echo 'Stopping Selenium Grid...'

            bat 'docker compose down'
        }

        success {
            echo 'Build completed successfully.'
        }

        failure {
            echo 'Build failed.'
        }
    }
}