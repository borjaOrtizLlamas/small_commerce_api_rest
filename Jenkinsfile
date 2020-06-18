pipeline {
   agent any
   	stages {
        stage('login') {
            steps {
                sh 	"aws ecr get-login-password --region eu-west-1 | docker login --username AWS --password-stdin 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest"
            }
        }

        stage('build') {
            dir('envs') {
                git credentialsId: 'github_credential', url: 'https://github.com/borjaOrtizLlamas/shop_infraestucture_generator_vars.git'
                sh 	"docker build --no-cache --build-arg TAG=$TAG . -t 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:$TAG"
            }
        }

        stage('push to repository') {
            steps {
                sh 	"docker push 005269061637.dkr.ecr.eu-west-1.amazonaws.com/small_comerce_api_rest:$TAG"
            }
        }
    }
}