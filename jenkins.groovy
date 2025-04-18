pipeline {
    agent any

    parameters {
        string(name: 'BRANCH', defaultValue: 'master', description: 'Git branch to build')
        choice(name: 'BROWSER', choices: ['edge', 'chrome', 'firefox', 'safari'], description: 'Browser for testing')
    }

    environment {
        EXECUTION = "jenkins"
        BRANCH_NAME = "${params.BRANCH}"
        BROWSER_NAME = "${params.BROWSER}"
        REPOSITORY = "https://github.com/tdeugdoer/UI-test-selenide-jenkins.git"
        BASE_URL = "https://pizzeria.skillbox.cc"
        ALLURE_SCREENSHOTS = "true"
        ALLURE_PAGE_SOURCES = "true"
    }

    stages {
        stage('Display Parameters and Environment') {
            steps {
                echo "=== PARAMETERS ==="
                echo "BRANCH: ${params.BRANCH}"
                echo "BROWSER: ${params.BROWSER}"

                echo "\n=== ENVIRONMENT VARIABLES ==="
                echo "EXECUTION: ${env.EXECUTION}"
                echo "BRANCH_NAME: ${env.BRANCH_NAME}"
                echo "BROWSER_NAME: ${env.BROWSER_NAME}"
                echo "REPOSITORY: ${env.REPOSITORY}"
                echo "BASE_URL: ${env.BASE_URL}"
                echo "ALLURE_SCREENSHOTS: ${env.ALLURE_SCREENSHOTS}"
                echo "ALLURE_PAGE_SOURCES: ${env.ALLURE_PAGE_SOURCES}"

                echo "\n=== SYSTEM PROPERTIES ==="
                sh 'printenv | sort'
            }
        }

//        stage("clone repo") {
//            steps {
//                getProject("$REPOSITORY", "$BRANCH_NAME")
//            }
//        }
//
//        stage("run tests") {
//            steps {
//                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
//                    sh """
//                            mvn clean test \
//                                -Dexecution=$EXECUTION \
//                                -Dbase.url=$BASE_URL \
//                                -Dbrowser=$BROWSER_NAME \
//                                -Dallure.screenshots=$ALLURE_SCREENSHOTS \
//                                -Dallure.page.sources=$ALLURE_PAGE_SOURCES
//                            """
//                }
//            }
//        }
//
//        stage("allure reports") {
//            steps {
//                script {
//                    generateAllure()
//                }
//            }
//        }
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