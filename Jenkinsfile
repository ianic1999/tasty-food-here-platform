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
                                sh('npm i --force')
                                sh('npm run build')
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
                                        sh('docker build -t tfh-backend .')
                                    }
                                }
                            }
                        }

                        stage('build docker frontend') {
                            steps {
                                script {
                                    dir('tfh-frontend') {
                                        echo 'Building docker frontend'
                                        sh('docker build -t tfh-frontend .')
                                    }
                                }
                            }
                        }

                        stage('connect to digital ocean') {
                            steps {
                                script {
                                    sh('docker login -u dop_v1_4e206e9dc4d2adf51c2f6c31fe6bac04bfc395fb6522a6330bedb8c85cedf00f -p dop_v1_4e206e9dc4d2adf51c2f6c31fe6bac04bfc395fb6522a6330bedb8c85cedf00f registry.digitalocean.com')
                                }
                            }
                        }

                        stage('push to digitalocean') {
                            stages {
                                stage('push backend image') {
                                    steps {
                                        script {
                                            sh('docker tag tfh-backend registry.digitalocean.com/tfh-container/tfh-backend')
                                            sh('docker push registry.digitalocean.com/tfh-container/tfh-backend')
                                        }
                                    }
                                }

                                stage('push frontend image') {
                                    steps {
                                        script {
                                            sh('docker tag tfh-frontend registry.digitalocean.com/tfh-container/tfh-frontend')
                                            sh('docker push registry.digitalocean.com/tfh-container/tfh-frontend')
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