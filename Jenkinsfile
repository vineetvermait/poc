pipeline {
  stages {
    def mvnHome
    stage('Preparation') {
      git 'https://github.com/vineetvermait/poc'
      mvnHome = tool 'mvn'
    }
    stage('Build') {
      if (isUnix()) {
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
      } else {
        bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
      }
    }
    stage('Make Container') {
      docker = tool 'docker'
      sh "'${docker}/bin/docker' build -t vineetvermait/poc:${env.BUILD_ID} ."
      sh "'${docker}/bin/docker' tag vineetvermait/poc:${env.BUILD_ID} vineetvermait/poc:latest"
    }
    stage('Results') {
      junit '**/target/surefire-reports/TEST-*.xml'
      archiveArtifacts 'target/*.jar'
    }
    stage('Publish to Docker') {
      withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh "'${docker}/bin/docker' login -u ${USERNAME} -p ${PASSWORD}"
        sh "'${docker}/bin/docker' push vineetvermait/poc:${env.BUILD_ID}"
        sh "'${docker}/bin/docker' push vineetvermait/poc:latest"
      }
    }
  }
}