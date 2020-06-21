pipeline {
   agent any

    parameters {
        string(name: 'Greeting', defaultValue: 'Hello', description: 'How should I greet the world?')
    }


   	stages {
        stage('build  proyect with JUnit') {
            steps {
                sh 	"mvn clean install" 
            }
        }

        stage('docker - build') {
            steps {
                dir('dockerconf') {
                    script {
                        if (env.BRANCH_NAME != 'master') {
                            sh "cp ../target/gs-rest-service.jar ./"
                            git credentialsId: 'github_credential', url: 'https://github.com/borjaOrtizLlamas/small_comerce_api_rest_container'
                            sh "docker build --no-cache  . -t 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:${BUILD_NUMBER}"
                        } else {    
                            sh "cp ../target/gs-rest-service.jar ./"
                            git credentialsId: 'github_credential', url: 'https://github.com/borjaOrtizLlamas/small_comerce_api_rest_container'
                            sh "docker build --no-cache  . -t 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:${BUILD_NUMBER}-BETA"
                        }
                }
            }
        }

        stage('login') {
            steps {
                sh 	"aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest"
            }
        }

//        stage('push to repository') {
//            steps {
//                sh 	"docker tag 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:$TAG 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:latest"
//                sh 	"docker push 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:$TAG && docker push 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:latest"
//            }
//        }
    }
}