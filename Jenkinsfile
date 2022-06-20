pipeline {
    agent any

    stages {
        stage('Prepare') {

            steps {
                checkout scm
            }

            post {

                success {
                    echo 'prepare success'
                }

                always {
                    echo 'done prepare'
                }

                cleanup {
                    echo 'after all other post conditions'
                }
            }
        }

        stage('build gradle') {
            steps {
                sh 'chmod +x gradlew'
                sh  './gradlew clean build'

                sh 'ls -al ./build'
            }
            post {
                success {
                    echo 'gradle build success'
                }

                failure {
                    echo 'gradle build failed'
                }
            }
        }

        stage('Unit & Integration Tests') {
            steps{
                junit '**/build/test-results/test/*.xml'
            }
        }

        stage('dockerizing'){
            steps{
                sh 'docker build . -t mooh2jj/todo_backend'
            }
        }

        stage('Deploy') {
            steps {
            // 기존 생긴 images 삭제를 위해 필요 처음시는 필요없음.
                sh 'docker rm -f $(docker ps -a -q -f name=todo_backend)'
                sh 'docker rmi -f mooh2jj/todo_backend'
                sh 'docker run --name todo_backend -d -p 8090:8090 mooh2jj/todo_backend'
            }

            post {
                success {
                    echo 'success'
                }

                failure {
                    echo 'failed'
                }
            }
        }
    }
}