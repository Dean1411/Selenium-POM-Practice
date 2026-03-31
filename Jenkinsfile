pipeline {
    agent any

    tools {
        maven 'apache-maven-3.9.9'
        jdk 'JDK21'
    }

    stages {
    
        stage('Checkout') {
            steps {
            	echo 'Checking out SCM..'
                checkout scm
            }
        }

        stage('Build & Test') {
            steps  {
				
				script{
					if(env.CHANGE_ID){
						echo "Pull Request detected -> Running single suite."
						bat "mvn clean test -DsuiteXmlFile=TestRunner/testng.xml"
					}
					else if (env.BRANCH_NAME == 'main'){
	                    echo "Main branch detected → Running parallel cross browser suite"

                        bat "mvn clean test -DsuiteXmlFile=TestRunner/testng-parallel.xml"
					}
					else{
                        echo "Feature branch → Running single suite"

                        bat "mvn clean test -DsuiteXmlFile=TestRunner/testng.xml"
					}
				}
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
            	reportDir: 'src/test/resources/reports',
            	reportFiles: 'extentReport.html',
            	reportName: 'Extent Test Report',
            	keepAll: true,
            	alwaysLinkToLastBuild: true,
            	allowMissing: true
            ])
        }
    }
}