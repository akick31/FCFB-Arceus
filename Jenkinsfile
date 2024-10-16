pipeline {
    agent any

    environment {
        IMAGE_NAME = 'fcfb-arceus'
        CONTAINER_NAME = 'FCFB-Arceus'
        DOCKERFILE = 'Dockerfile'
        APP_PROPERTIES = './src/main/resources/application.properties'
        DB_URL = credentials('DB_URL')
        DB_USERNAME = credentials('DB_USERNAME')
        DB_PASSWORD = credentials('DB_PASSWORD')
        JWT_ENCRYPTION_KEY = credentials('JWT_ENCRYPTION_KEY')
        EMAIL_HOST = credentials('EMAIL_HOST')
        EMAIL_PORT = credentials('EMAIL_PORT')
        EMAIL = credentials('EMAIL')
        EMAIL_PASSWORD = credentials('EMAIL_PASSWORD')
        DISCORD_TOKEN = credentials('DISCORD_TOKEN')
        DISCORD_GUILD_ID = credentials('DISCORD_GUILD_ID')
        DISCORD_FORUM_CHANNEL_ID = credentials('DISCORD_FORUM_CHANNEL_ID')
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

        stage('Build') {
            steps {
                echo 'Creating the properties file...'
                script {
                    def propertiesContent = """
                        # Domain configuration
                        domain.url=${env.DOMAIN_URL}
                        server.servlet.context-path=/arceus

                        # Spring Boot configuration
                        spring.datasource.url=${env.DB_URL}
                        spring.datasource.username=${env.DB_USERNAME}
                        spring.datasource.password=${env.DB_PASSWORD}
                        spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
                        spring.jpa.hibernate.ddl-auto=update
                        spring.jpa.show-sql=true
                        spring.jackson.property-naming-strategy=SNAKE_CASE
                        management.security.enabled=false

                        # JWT configuration
                        encryption.key=${env.JWT_ENCRYPTION_KEY}
                        encryption.algorithm=aes

                        # Email configuration
                        spring.mail.host=${env.EMAIL_HOST}
                        spring.mail.port=${env.EMAIL_PORT}
                        spring.mail.username=${env.EMAIL}
                        spring.mail.password=${env.EMAIL_PASSWORD}
                        spring.mail.properties.mail.smtp.auth=true
                        spring.mail.properties.mail.smtp.starttls.enable=true

                        # Discord configuration
                        discord.bot.token=${env.DISCORD_TOKEN}
                        discord.guild.id=${env.DISCORD_GUILD_ID}
                        discord.forum.channel.id=${env.DISCORD_FORUM_CHANNEL_ID}
                    """.stripIndent()

                    writeFile file: "${env.APP_PROPERTIES}", text: propertiesContent
                }

                echo 'Building the Arceus project...'
                sh './gradlew clean build'
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

        stage('Run New Arceus Container') {
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
