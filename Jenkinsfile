pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        IMAGE_NAME = 'devops-demo'
        IMAGE_TAG  = "${BUILD_NUMBER}"
    }

    stages {
        stage('test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('build jar') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('build image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-repo',
                                                  usernameVariable: 'USER',
                                                  passwordVariable: 'PASS')]) {
                    sh 'docker build -t $USER/$IMAGE_NAME:$IMAGE_TAG .'
                }
            }
        }

        stage('push image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-repo',
                                                  usernameVariable: 'USER',
                                                  passwordVariable: 'PASS')]) {
                    sh 'echo $PASS | docker login -u $USER --password-stdin'
                    sh 'docker push $USER/$IMAGE_NAME:$IMAGE_TAG'
                }
            }
        }
    }
}
