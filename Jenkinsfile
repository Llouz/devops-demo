library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
     remote: 'https://github.com/Llouz/jenkins-shared-library.git'])

pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('build jar') {
            steps {
                buildJar()
            }
        }

        stage('build and push image') {
            when { branch 'main' }
            steps {
                buildImage("ellouz/devops-demo:${BUILD_NUMBER}")
            }
        }
    }
}
