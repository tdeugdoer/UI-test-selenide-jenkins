pipeline {
    agent any

    tools {
        maven 'maven-jenkins'
    }

    parameters {
        string(name: 'BRANCH', defaultValue: 'master', description: 'Git branch to build')
        choice(name: 'BROWSER', choices: ['edge', 'chrome', 'firefox', 'safari'], description: 'Browser for testing')
    }

    environment {
        EXECUTION = "jenkins"
        REPOSITORY = "https://github.com/tdeugdoer/UI-test-selenide-jenkins.git"
        BASE_URL = "https://pizzeria.skillbox.cc"
        ALLURE_SCREENSHOTS = "true"
        ALLURE_PAGE_SOURCES = "true"
    }

    stages {
        stage('Setup Edge') {
            steps {
                sh '''
                    # Команды установки из раздела 1
                    curl -fsSL https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor | sudo tee /usr/share/keyrings/microsoft-edge.gpg > /dev/null
                    echo "deb [arch=amd64 signed-by=/usr/share/keyrings/microsoft-edge.gpg] https://packages.microsoft.com/repos/edge stable main" | sudo tee /etc/apt/sources.list.d/microsoft-edge.list
                    sudo apt-get update && sudo apt-get install -y microsoft-edge-stable
                '''
            }
        }

        stage("clone repo") {
            steps {
                getProject("$REPOSITORY", "$BRANCH")
            }
        }

        stage("run tests") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh """
                            mvn clean test \
                                -Dexecution=$EXECUTION \
                                -Dbase_url=$BASE_URL \
                                -Dbrowser=$BROWSER \
                                -Dallure_screenshots=$ALLURE_SCREENSHOTS \
                                -Dallure_page_sources=$ALLURE_PAGE_SOURCES
                            """

                }
            }
        }

        stage("allure reports") {
            steps {
                script {
                    generateAllure()
                }
            }
        }
    }
}

def getProject(String repo, String branch) {
    try {
        cleanWs()
        checkout([
                $class           : 'GitSCM',
                branches         : [[name: branch]],
                userRemoteConfigs: [[url: repo]]
        ])
    } catch (Exception e) {
        error("Failed to clone repo: ${e.message}")
    }
}

def generateAllure() {
    allure([
            includeProperties: true,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'target/allure-results']]
    ])
}