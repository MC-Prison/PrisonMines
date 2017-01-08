node {
  stage 'Stage Checkout'

  checkout scm
  sh 'git submodule update --init'  

  stage 'Stage Build'

  sh "./gradlew build --"

  stage 'Stage Archive'
  archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true

}
