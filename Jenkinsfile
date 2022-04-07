pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    environment {
        digitalOceanApiKey = ""
        dockerRegistry = ""
        backendImage = ""
        frontendImage = ""
    }

    stages {
        stage('Init') {
            steps {
                script {
                    echo 'Initiate variables'
                    digitalOceanApiKey = "dop_v1_4e206e9dc4d2adf51c2f6c31fe6bac04bfc395fb6522a6330bedb8c85cedf00f"
                    dockerRegistry = "tfh-container"
                    backendImage = "tfh-backend"
                    frontendImage = "tfh-frontend"
                }
            }
        }

        stage('Build') {
            stages {
                stage('Build tfh-backend') {
                    steps {
                        script {
                            dir('tfh-backend') {
                                echo 'Building tfh-backend'
                                sh("mvn clean install -DskipTests")
                            }
                        }
                    }
                }

                stage('Build tfh-frontend') {
                    steps {
                        script {
                            dir('tfh-frontend') {
                                echo 'Building tfh-frontend'
                                nodejs('nodejs12.16') {
                                    sh('npm i')
                                    sh('npm run build')
                                }
                            }
                        }
                    }
                }

                stage('Build docker') {
                    stages {
                        stage('build docker backend') {
                            steps {
                                script {
                                    dir('tfh-backend') {
                                        echo 'Building docker backend'
                                        sh('docker build -t $backendImage .')
                                    }
                                }
                            }
                        }

                        stage('build docker frontend') {
                            steps {
                                script {
                                    dir('tfh-frontend') {
                                        echo 'Building docker frontend'
                                        sh('docker build -t $frontendImage .')
                                    }
                                }
                            }
                        }

                        stage('connect to digital ocean') {
                            steps {
                                script {
                                    sh('docker login -u $digitalOceanApiKey -p $digitalOceanApiKey registry.digitalocean.com')
                                }
                            }
                        }

                        stage('push to digitalocean') {
                            stages {
                                stage('push backend image') {
                                    steps {
                                        script {
                                            sh('docker tag $backendImage registry.digitalocean.com/$container/$backendImage')
                                            sh('docker push registry.digitalocean.com/$container/$backendImage')
                                        }
                                    }
                                }

                                stage('push frontend image') {
                                    steps {
                                        script {
                                            sh('docker tag $backendImage registry.digitalocean.com/$container/$backendImage')
                                            sh('docker push registry.digitalocean.com/$container/$backendImage')
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        failure {
            emailext body: 'The last build has failed. Please check the logs and fix the issues.', subject: 'Jenkins - Build failure', to: 'ianicumanschii@gmail.com'
        }
    }
}