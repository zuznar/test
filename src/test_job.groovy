node('master') {

    stage('Pre-commit clean') {
        cleanWs()
    }
    stage('Git') {
        echo 'Cloning configuration'
        git branch: 'master', url: 'https://proxy.git.sabre-gcp.com/scm/sswops/zuzanna-tests.git', credentialsId: 'svc-ark-ctopscicd-nexus-token-dev'
    }
    stage('Post-commit clean') {
        cleanWs()
    }
}
