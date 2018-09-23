pipeline {
  agent any
  tools {
    maven 'mvn'
  }
  options {
      timestamps()
      buildDiscarder(logRotator(numToKeepStr: '10'))
  }
  stages {
    stage('prepare'){
      steps{
        echo 'prepare'
        git 'https://github.com/vineetvermait/poc'
      }
    }
    stage('build'){
      steps{
        sh "mvn -Dmaven.test.failure.ignore clean package"
      }
    }
    stage('test'){
      steps{
        junit '**/target/surefire-reports/TEST-*.xml'
        archiveArtifacts 'target/*.jar'
      }
    }
    stage('dockerise'){
      steps{
        echo 'dockerise'
        script {
          dockerPath = tool 'docker'
          sh "'${dockerPath}/bin/docker' build -t vineetvermait/poc:${env.BUILD_ID} ."
        }
      }
    }
  }
  post {
    always {
      archiveArtifacts 'target/**/*.jar'
      junit 'target/**/*.xml'
    }
    success {
      withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        script {
          dockerPath = tool 'docker'
          sh "'${dockerPath}/bin/docker' login -u ${USERNAME} -p ${PASSWORD}"

          sh "'${dockerPath}/bin/docker' build -t vineetvermait/poc:${env.BUILD_ID} ."
          sh "'${dockerPath}/bin/docker' push vineetvermait/poc:${env.BUILD_ID}"

          sh "'${dockerPath}/bin/docker' tag vineetvermait/poc:${env.BUILD_ID} vineetvermait/poc:latest"
          sh "'${dockerPath}/bin/docker' push vineetvermait/poc:latest"
        }
      }
    }
  }
}