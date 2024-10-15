pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fcfb-arceus'
        CONTAINER_NAME = 'FCFB-Arceus'
        DOCKERFILE = 'Dockerfile'
        APP_PROPERTIES = './src/main/resources/application.properties'
    }

    stages {
        stage('Stop and Remove Existing Bot') {
            steps {
                script {
                    echo 'Stopping and removing the existing Arceus instance...'
                    sh """
                        docker stop ${CONTAINER_NAME} || echo "Arceus is not running."
                        docker rm ${CONTAINER_NAME} || echo "No old Arceus instance to remove."
                    """
                }
            }
        }

        stage('Build New Docker Image') {
            steps {
                script {
                    echo 'Building the new Arceus Docker image...'
                    sh """
                        docker build -t ${IMAGE_NAME}:${DOCKERFILE} .
                    """
                }
            }
        }

        stage('Run New Pong Bot Container') {
            steps {
                script {
                    echo 'Starting the new Arceus container...'
                    sh """
                        docker run -d --restart=always --name ${CONTAINER_NAME} \\
                            --env-file ${APP_PROPERTIES} \\
                            ${IMAGE_NAME}:${DOCKERFILE}
                    """
                }
            }
        }

        stage('Cleanup Docker System') {
            steps {
                script {
                    echo 'Pruning unused Docker resources...'
                    sh 'docker system prune -a --force'
                }
            }
        }
    }

    post {
        success {
            echo 'Arceus has been successfully deployed!'
        }
        failure {
            echo 'An error occurred during the Arceus deployment.'
        }
    }
}
