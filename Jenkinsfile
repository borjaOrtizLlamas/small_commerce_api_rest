def variablesDef = null

pipeline {
   agent any

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
                        if (env.BRANCH_NAME == 'master') {
                            variablesDef = env.BUILD_NUMBER + '-pro'
                        } else {
                            variablesDef = env.BUILD_NUMBER + '-' + env.BRANCH_NAME
                        }
                        sh "cp ../target/gs-rest-service.jar ./"
                        git credentialsId: 'github_credential', url: 'https://github.com/borjaOrtizLlamas/small_comerce_api_rest_container'
                        sh "docker build . -t 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:${variablesDef}"
                    }
                }
            }
        }
        stage('docker - test') {
            steps {
                dir('dockerconf') {
                    script {
                        try {
                            sh "sed '1,35 s/change_tag/${variablesDef}/g' docker-compose > docker-compose.yml"
                            sh "docker-compose up -d"
                            def versionReadfile = httpRequest url:'http://localhost:90/client'
                            echo versionReadfile
                            def requestBodyMock = readFile "mock.json"
                            httpRequest url:'http://localhost:90/client', httpMode: 'POST', acceptType:'APPLICATION_JSON', requestBody: requestBodyMock
                            if(versionReadfile != requestBodyMock){
                                error("Build failed because the data is not returned in the correct way")
                            }
                        } catch (exc) {
                            throw exc
                        } finally {

                        }
                        
                    }
                }
            }
        }

        stage('login') {
            steps {
                sh 	"aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest"
            }
        }

        stage('push to repository') {
            steps {
                script {
                    sh 	"docker push 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:${variablesDef}"
                    if (env.BRANCH_NAME == 'master') {
                        sh 	"docker tag 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:${variablesDef} 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:latest"
                        sh  "docker push 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:latest"
                    }
                }
            }
        }
    }
}