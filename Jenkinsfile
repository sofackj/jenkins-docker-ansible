node("dockerHost"){
    stage('Import repositories'){
        dir("build_project"){
            checkout([$class: 'GitSCM',
                branches: [[name: '*/dev']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [[$class: 'CleanCheckout']],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'my-git-credentials', url: 'https://github.com/sofackj/jenkins-docker-ansible.git']]
            ])
        }
        dir("ansible_project"){
            checkout([$class: 'GitSCM',
                branches: [[name: '*/dev']],
                doGenerateSubmoduleConfigurations: false,
                extensions: [[$class: 'CleanCheckout']],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'my-git-credentials', url: 'https://github.com/sofackj/docker-for-projects.git']]
            ])
        }
    }
    stage ('Environment variables') {
        env.ANSIBLE_DIR = "ansible_project/ansible/ansible_volumes/host_interaction"
        env.ANSIBLE_PLAYBOOK = "host-int-playbook.yml"
    }
    stage ('Build and Deployment') {
        def ANSIBLE_IMG = docker.build (
            "my-ansible",
            "./build_project/ansible_controller/"
            )
        ANSIBLE_IMG.inside ('-u root') {
            dir ("${ANSIBLE_DIR}") {
                ansiblePlaybook (
                    playbook: "${ANSIBLE_PLAYBOOK}"
                )  
            }
        }
    }
    stage("Cleanup"){
        cleanWs()
    }
}
