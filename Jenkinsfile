#!groovy

import groovy.json.JsonOutput

List CHANGED_DIRECTORIES = []

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
                script{

                          //git  branch: 'main',
                          //    credentialsId: 'US1783052_GitHub_App_test',
                          //    url: 'https://github.com/zuznar/test.git'
                          // sh 'env|sort'
                          //sh 'curl -v -L https://github.com/'


                          withCredentials([gitUsernamePassword(credentialsId: 'US1783052_GitHub_App_test')]) {
                            sh 'git clone https://github.com/zuznar/test.git'
                            sh 'cd test'
                            //sh 'git config --global user.name "jenkins"'
                           // sh 'git config --global user.email abcd@abcd.com'
                            //sh "git branch ${branch_name}"
                          //  sh "git checkout -b ${branch_name}"
                          //  sh "git push --set-upstream origin ${branch_name}"
                           // script{
                             //   for (directory in CHANGED_DIRECTORIES) {
                              //  sh "git add ${directory}"
                              //  }
                            //}
                            //sh 'git checkout -b newBranch'
                            sh 'ls'
                            sh 'echo "yhytjnujn" > Jenkinsfile'
                            sh 'git add Jenkinsfile'
                            sh 'git commit -am "test commit"'
                            sh 'git push https://github.com/zuznar/test.git'

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

