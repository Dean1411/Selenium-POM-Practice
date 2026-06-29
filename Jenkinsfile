pipeline {
    agent any

    tools {
        maven 'apache-maven-3.9.9'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out SCM...'
                checkout scm
            }
        }

        stage('Start Selenium Grid') {
            steps {
                echo 'Starting Selenium Grid...'

                bat 'docker compose up -d'

                timeout(time: 2, unit: 'MINUTES') {
                    waitUntil {
                        script {
                            def status = bat(
                                script: 'curl -s http://localhost:4444/status',
                                returnStdout: true
                            ).trim()

                            return status.contains('"ready":true')
                        }
                    }
                }
            }
        }

        stage('Build & Test') {
            steps {

                script {

                    if (env.CHANGE_ID) {

                        echo "Pull Request detected -> Running single suite"

                        bat "mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml"

                    } else if (env.BRANCH_NAME == 'main') {

                        echo "Main branch detected -> Running parallel suite"

                        bat "mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml"

                    } else {

                        echo "Feature branch detected -> Running suite"

                        bat "mvn clean test -DsuiteXmlFile=TestRunner/grid-docker.xml"

                    }
                }
            }
        }

        stage('Archive Extent Report') {
            steps {
                archiveArtifacts artifacts: '**/extentReport*.html', fingerprint: true
            }
        }
    }

    post {

        always {

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
    }
}