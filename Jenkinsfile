pipeline {
    agent any

    options {
        disableConcurrentBuilds()
    }

    stages {
        stage('Build') {
            stages {
                stage('build tfh-backend') {
                    steps {
                        script {
                            dir('tfh-backend') {
                                echo 'Building tfh-backend'
                                sh("mvn clean install -DskipTests")
                            }
                        }
                    }
                }

                stage('build tfh-frontend') {
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

                stage('build docker') {
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
                                    sh('docker login -u $digitalOceanApiToken -p $digitalOceanApiToken registry.digitalocean.com')
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

        stage('Deploy') {
            stages {
                stage('deploy apps') {
                    steps {
                        script {
                            sh('docker stop tfh-backend || true')
                            sh('docker stop tfh-frontend || true')
                            sh('docker rm tfh-backend || true')
                            sh('docker rm tfh-frontend || true')
                            sh('docker image rm registry.digitalocean.com/tfh-container/tfh-backend')
                            sh('docker image rm registry.digitalocean.com/tfh-container/tfh-frontend')
                            sh('docker pull registry.digitalocean.com/tfh-container/tfh-backend')
                            sh('docker pull registry.digitalocean.com/tfh-container/tfh-frontend')
                            sh('docker run -e DATABASE_HOST=$DATABASE_HOST -e DATABASE_PORT=$DATABASE_PORT -e DATABASE_NAME=$DATABASE_NAME -e DATABASE_USER=$DATABASE_USER -e DATABASE_PASSWORD=$DATABASE_PASSWORD -e JWT_SECRET=$JWT_SECRET -p 8081:8081 --name tfh-backend -d registry.digitalocean.com/tfh-container/tfh-backend')
                            sh('docker run -p 80:80 --name tfh-frontend -d registry.digitalocean.com/tfh-container/tfh-frontend')
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