node('master') {

    stage('Pre-commit clean') {
        cleanWs()
    }
    stage('test-http-token-sswdcdevgcp') {
        echo 'Cloning configuration'
        git branch: 'master', url: 'https://proxy.git.sabre-gcp.com/scm/sswops/zuzanna-tests.git', credentialsId: 'svc-ark-sswdcdevgcp-git-http-token'
    }
    stage('test-http-token-ctopscicd') {
        echo 'Cloning configuration'
        git branch: 'master', url: 'https://proxy.git.sabre-gcp.com/scm/sswops/zuzanna-tests.git', credentialsId: 'svc-ark-ctopscicd-git-http-token'
    }

    stage('test-ssh-key-sswdcdevgcp') {
        echo 'Cloning configuration'
        git branch: 'master', url: 'ssh://git@proxy.git.sabre-gcp.com/sswops/zuzanna-tests.git', credentialsId: 'svc-ark-sswdcdevgcp-git-ssh-key'
    }

    stage('test-ssh-key-ctopscicd') {
        echo 'Cloning configuration'
        git branch: 'master', url: 'ssh://git@proxy.git.sabre-gcp.com/sswops/zuzanna-tests.git', credentialsId: 'svc-ark-ctopscicd-git-ssh-key'
    }

    stage('Post-commit clean') {
        cleanWs()
    }

    stage("download from nexus trusted dev ctops") {
        response = httpRequest url: "https://gcp.repository.sabre.com/repository/config-trusted" + "/" + "com/sabre/as/ecomm/bundles/stacks/awsdev" + "/" +
                "cf-as-dev-ecomm-jenkins88m" + "/" + "2.0.361" + "/" +
                "cf-as-dev-ecomm-jenkins88m" + "-" + "2.0.361" + ".tar.gz",
                outputFile: "cf-as-dev-ecomm-jenkins88m-ctops" + "-" + "2.0.361" + ".tar.gz" ,
                authentication: "svc-ark-ctopscicd-nexus-token-dev"
    }

    stage("download from nexus trusted dev sswdev") {
        response = httpRequest url: "https://gcp.repository.sabre.com/repository/config-trusted" + "/" + "com/sabre/as/ecomm/bundles/stacks/awsdev" + "/" +
                "cf-as-dev-ecomm-jenkins88m" + "/" + "2.0.360" + "/" +
                "cf-as-dev-ecomm-jenkins88m" + "-" + "2.0.360" + ".tar.gz",
                outputFile: "cf-as-dev-ecomm-jenkins88m-sswdev" + "-" + "2.0.360" + ".tar.gz" ,
                authentication: "svc-ark-sswdcdevgcp-nexus-token-dev"
    }

    stage("promote to trusted - sswdcdevgcp") {
        try {
            triggerRemoteJob(
                    remoteJenkinsName: "gcp.promote.repository.sabre.com",
                    auth: CredentialsAuth(credentials: 'svc-ark-sswdcdevgcp-promotion-token'),
                    enhancedLogging: true,
                    job: "promote-config",
                    blockBuildUntilComplete: true,
                    parameters: "groupId=" + "testgroup" + "\n" +
                            "artifactId=" + "testartifact" + "\n" +
                            "version=" + "testver"

            )
        }catch (err) {
            echo "something failed"
            currentBuild.result = 'UNSTABLE'
        }
    }

    stage("promote to trusted - ctopscicd") {
        try {
            triggerRemoteJob(
                    remoteJenkinsName: "gcp.promote.repository.sabre.com",
                    auth: CredentialsAuth(credentials: 'svc-ark-ctopscicd-promotion-token'),
                    enhancedLogging: true,
                    job: "promote-config",
                    blockBuildUntilComplete: true,
                    parameters: "groupId=" + "testgroup" + "\n" +
                            "artifactId=" + "testartifact" + "\n" +
                            "version=" + "testver"

            )
        }catch (err) {
            echo "something failed"
            currentBuild.result = 'UNSTABLE'
        }
    }

    stage("promote to trusted - ctopscicd (cfg from global)") {
        try {
            triggerRemoteJob(
                    remoteJenkinsName: "gcp.promote.repository.sabre.com",
                    enhancedLogging: true,
                    job: "promote-config",
                    blockBuildUntilComplete: true,
                    parameters: "groupId=" + "testgroup" + "\n" +
                            "artifactId=" + "testartifact" + "\n" +
                            "version=" + "testver"

            )
        }catch (err) {
            echo "something failed"
            currentBuild.result = 'UNSTABLE'
        }
    }

}
