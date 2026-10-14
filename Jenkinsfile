
library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
     remote: 'https://github.com/Llouz/jenkins-shared-library.git'])

pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    options {
        disableConcurrentBuilds()
        skipDefaultCheckout()
    }

    environment {
        APP_VERSION = ''
        SKIP_VERSIONING = 'false'
    }

    stages {

        stage('checkout and check commit') {
            steps {
                checkout scm

                script {
                    def commitMessage = sh(
                        script: 'git log -1 --pretty=%s',
                        returnStdout: true
                    ).trim()

                    echo "Latest commit: ${commitMessage}"

                    if (commitMessage.startsWith('chore: bump version to ') &&
                        commitMessage.endsWith('[skip ci]')) {

                        env.SKIP_VERSIONING = 'true'
                        echo 'Version commit detected. Skipping pipeline.'

                    } else {
                        env.SKIP_VERSIONING = 'false'
                        echo 'Normal commit detected.'
                    }
                }
            }
        }

        stage('test') {
            when {
                expression {
                    env.SKIP_VERSIONING != 'true'
                }
            }
            steps {
                sh 'mvn test'
            }
        }


        stage('increment version') {
            when {
                branch 'main'
                expression {
                    env.SKIP_VERSIONING != 'true'
                }
            }
            steps {
                script {
                    incrementVersion()

                    def pom = readMavenPom file: 'pom.xml'
                    def version = pom.version.toString()

                    if (!version || version == 'null') {
                        error "Invalid Maven version"
                    }

                    env.APP_VERSION = version

                    echo "New application version: ${env.APP_VERSION}"
                }
            }
        }



        stage('build jar') {
            when {
                expression {
                    env.SKIP_VERSIONING != 'true'
                }
            }
            steps {
                buildJar()
            }
        }

        stage('build and push image') {
            when {
                branch 'main'
                expression {
                    env.SKIP_VERSIONING != 'true'
                }
            }
            steps {
                buildImage("ellouz/devops-demo:${env.APP_VERSION}")
            }
        }

        stage('commit and push version') {
            when {
                branch 'main'
                expression {
                    env.SKIP_VERSIONING != 'true'
                }
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'github-token',
                    usernameVariable: 'GIT_USER',
                    passwordVariable: 'GIT_TOKEN'
                )]) {
                    sh '''
                        set -eu
                        set +x

                        git config user.name "jenkins-bot"
                        git config user.email "jenkins-bot@users.noreply.github.com"

                        git add pom.xml

                        if git diff --cached --quiet; then
                            echo "No version changes to commit"
                            exit 0
                        fi

                        git commit -m "chore: bump version to ${APP_VERSION} [skip ci]"

                        ASKPASS_FILE=$(mktemp)
                        trap 'rm -f "$ASKPASS_FILE"' EXIT

                        cat > "$ASKPASS_FILE" <<'EOF'
#!/bin/sh
case "$1" in
    *Username*) printf '%s\\n' "$GIT_USER" ;;
    *Password*) printf '%s\\n' "$GIT_TOKEN" ;;
esac
EOF

                        chmod 700 "$ASKPASS_FILE"

                        GIT_ASKPASS="$ASKPASS_FILE" \
                        GIT_TERMINAL_PROMPT=0 \
                        git -c credential.helper= push \
                            https://github.com/Llouz/devops-demo.git \
                            HEAD:main
                    '''
                }
            }
        }
    }
}
