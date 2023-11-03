#!groovy

import groovy.json.JsonOutput

List CHANGED_DIRECTORIES = []
List EMPTY_LIST = []
String BRANCH_NAME = "Branch_name"

pipeline {
    agent { label 'master' }
    parameters {
        string(name: "GIT_USER", defaultValue: "svc-ark-ctopscicd-git-ssh-key", description: "git user to access git.sabre.com")
        string(name: "payload_changes_0_toHash", defaultValue: '', description: "for uniqueness and not discard builds")
        string(name: "payload_changes_0_fromHash", defaultValue: '', description: "for uniqueness and not discard builds")
        string(name: "payload_changes_0_ref_displayId", defaultValue: '', description: "to detect branch name")
    }
    options {
        disableConcurrentBuilds()
    }

    stages{
        //here we search for modified files between commits provided by push from/to webhook
        stage("detect GIT changes") {
            when {
                allOf {
                    not{
                        equals expected: '0000000000000000000000000000000000000000', actual: params.payload_changes_0_fromHash
                    }
                }
            }
            steps {
                script {
                    println("from: " + params.payload_changes_0_fromHash + " to: " + params.payload_changes_0_toHash + ", branch: " + params.payload_changes_0_ref_displayId)
                    CHANGED_DIRECTORIES = sh(script: "git diff-tree --name-only ${params.payload_changes_0_fromHash} ${params.payload_changes_0_toHash}",
                            returnStdout: true).trim().split('\n')
                    CHANGED_DIRECTORIES.remove("Jenkinsfile")
                    CHANGED_DIRECTORIES.remove(".idea")
                    CHANGED_DIRECTORIES.remove("lib")
                    CHANGED_DIRECTORIES.remove("_tools")
                    println("directories to build: " + CHANGED_DIRECTORIES)
                }
            }
        }

        stage('Commit to GitHub') {
              steps {
                script {
                    if (CHANGED_DIRECTORIES != EMPTY_LIST) {
                          withCredentials([gitUsernamePassword(credentialsId: 'US1783052_GitHub_App_test')]) {
                            sh 'git clone https://github.com/zuznar/test.git'
                            sh 'cd test'
                            sh 'git remote set-url origin https://github.com/zuznar/test.git'
                            sh 'git branch -r'
                            sh "git branch ${BRANCH_NAME}"
                            sh "git checkout ${BRANCH_NAME}"
                            sh 'git remote -v'
                            script{
                                for (directory in CHANGED_DIRECTORIES) {
                                    sh "echo ${directory}"
                                    sh "cp ${directory} test"
                                    sh "git add test/${directory}"
                                }
                            }
                            sh 'git commit -am "test commit"'
                            sh "git push origin ${BRANCH_NAME}"
                           }
                    }
                }
              }
        }
    }
    post {
        always{
            cleanWs()
        }
    }
}

