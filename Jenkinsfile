#!groovy

import groovy.json.JsonOutput
import java.text.SimpleDateFormat

List CHANGED_DIRECTORIES = []
List EMPTY_LIST = []
String BRANCH_NAME = "newBranch56"

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
                        def date = new Date()
                        def sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                        def branch = sdf.format(date).toString().replace(" ", "_").replace("/","-").replace(":","-")
                        println("branch name: " + branch)
                        BRANCH_NAME = branch
                }

                          withCredentials([gitUsernamePassword(credentialsId: 'US1783052_GitHub_App_test')]) {
                            sh 'git clone https://github.com/zuznar/test.git'
                            //sh "cp -r Stateless\ Services/Customer\ Tier\ -\ External/beta/*.json postman-collections/Digital\ Connect/Stateless\ Services/stable/"
                            sh 'cp -r *.txt test'
                            //sh 'cd postman-collections'
                            sh 'cd test'
                           // sh "git checkout -b ${BRANCH_NAME}"
                           sh "git checkout -b abcdef"
                            //sh 'git add Digital\ Connect/Stateless\ Services/stable/*.json'
                            sh 'git add test/*.txt'
                            sh 'git commit -m "test commit"'
                            //sh "git push --set-upstream origin ${BRANCH_NAME}"
                            //sh "git push https://github.com/zuznar/test.git ${BRANCH_NAME}"
                            sh "git push https://github.com/zuznar/test.git abcdef"
                          }
//                withCredentials([usernamePassword(credentialsId: 'US1783052_GitHub_App_test', usernameVariable: 'USER', passwordVariable: 'TOKEN')]) {
//                    httpRequest(
//                        url: "https://api.github.com/repos/zuznar/test/pulls",
//                        httpMode: 'POST',
//                        contentType: 'APPLICATION_JSON',
//                        customHeaders: [
//                            [maskValue: true, name: 'Authorization', value: "Bearer ${TOKEN}"],
//                            [name: 'Accept', value: 'application/vnd.github+json']
//                        ],
//                        consoleLogResponseBody: true,
//                        quiet: false,
//                        requestBody: writeJSON(
//                            returnText: true,
//                            json: [
//                                title: 'TEST Pull Request',
//                                head: '${BRANCH_NAME}',
//                                base: 'main'
//                            ]
//                        )
//                    )
//                }

sh """curl -L \
      -X POST \
      -H "Accept: application/vnd.github+json" \
      -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpYXQiOiAxNjk5MzU5OTg5LCAiZXhwIjogMTY5OTM2MDU4OSwgImlzcyI6ICI0MTA2ODgifQ.eijgAgdg75QH2W0cN3b7h7Z9G2VTS9d-QoIbpV6foeiWLc4ZoQXoiAu9P4NOc2a-cVL5dr8z_enuILDK3wsoD59zEaLeTOhGzt3mINIToCbLwzUT0mFdEwkKMUZLwiNHgcPNmconROLULhF0asrreG2aMFQlDfnC__ak4MY3Vg59q9SiXoPCh92l2Cg-xEub9tdDal0q5eLZNK8ufp0GsDQ8rFWPwZQbQ7lyh1QuV6K2oD0tRFRl3-juZ03ZwdCCMFQqPzgfRZR1si2h3XWHCEqK5eFl8Ko892gCbjBQOK7EbFrJW27pUC1teOVvDfYss0MQud15wFa_KSADIIG6Jg" \
      -H "X-GitHub-Api-Version: 2022-11-28" \
      https://api.github.com/repos/zuznar/test/pulls \
      -d '{"title":"Amazing new feature","body":"Please pull these awesome changes in!","head":"abcdef","base":"main"}'"""
              }
        }
    }
    post {
        always{
            cleanWs()
        }
    }
}
