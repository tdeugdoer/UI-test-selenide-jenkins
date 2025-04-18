pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'master')
        choice(name: 'BROWSER', choices: ['edge', 'chrome', 'firefox', 'safari'])
    }

    environment {
        EXECUTION = "jenkins"
        REPOSITORY = "https://github.com/tdeugdoer/UI-test-selenide-jenkins.git"
        BASE_URL = "https://pizzeria.skillbox.cc"
        ALLURE_SCREENSHOTS = "true"
        ALLURE_PAGE_SOURCES = "true"
    }

    stages {
        stage("clone repo") {
            steps {
                getProject("$REPOSITORY", "${parameters.BRANCH}")
            }
        }

        stage("run tests") {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    sh """
                    mvn clean test \
                        -Dexecution=$EXECUTION \
                        -Dbase.url=$BASE_URL \
                        -Dbrowser=${parameters.BROWSER} \
                        -Dallure.screenshots=$ALLURE_SCREENSHOTS \
                        -Dallure.page.sources=$ALLURE_PAGE_SOURCES
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
                $class: 'GitSCM',
                branches: [[name: branch]],
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