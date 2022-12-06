// Example of pipelines ready to use after first login
// Initiate Project B
pipelineJob("init-system") {
        definition {
            cps {
                sandbox(true)
                script("""
node('jenkins') {
    stage("Check Docker Host"){
        try {
            timeout(time: 10, unit: 'SECONDS') {
                node('dockerHost'){
                    echo "Status Docker Host => OK"
                }
            }
        } catch(err) {
            error("Status Docker Host => DOWN")
        }
    }
}
                """)
        }
    }
}
// How to use trigger via the configure block
pipelineJob("registry-process") {
    configure { project ->
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' {
            'spec'('')
            'upstreamProjects'('init-system')
        }
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' / 'threshold' {
            'name'('Blue')
            'ordinal'('0')
            'color'('BLUE')
            'completeBuild'('true')
        }
    }
    definition {
        cpsScm {
            scm {
                git {
                    branch('*/dev')
                    remote {
                        url('https://gitlab.com/project-b-its/img-for-infra.git')
                        credentials('gitlab-credentials')
                    }
                    extensions {
                        cleanAfterCheckout()
                    }
                }
            }
            scriptPath("Jenkinsfile")
        }
    }
}
//
// How to use trigger via the configure block
pipelineJob("check-agents") {
    configure { project ->
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' {
            'spec'('')
            'upstreamProjects'('registry-process')
        }
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' / 'threshold' {
            'name'('Blue')
            'ordinal'('0')
            'color'('BLUE')
            'completeBuild'('true')
        }
    }
    definition {
            cps {
                sandbox(true)
                script("""
node ('dockerHost') {
    stage("Check Agent"){
        try {
            timeout(time: 120, unit: 'SECONDS') {
                node('dockerAgent'){
                    echo "Status Docker Agent => OK"
                }
            }
        } catch(err) {
            error "Status Docker Agent => DOWN"
        }
    }
}
                """)
        }
    }
}
// How to use trigger via the configure block
pipelineJob("check-ansible") {
    configure { project ->
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' {
            'spec'('')
            'upstreamProjects'('registry-process')
        }
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' / 'threshold' {
            'name'('Blue')
            'ordinal'('0')
            'color'('BLUE')
            'completeBuild'('true')
        }
    }
    definition {
            cps {
                sandbox(true)
                script("""
// Start the job in the chosen node
// Here we choose to filter by label 'docker'
node ('dockerHost') {
    // Delete the workspace then rebuild it
    stage ('Clean the worspace') {
        // Class to delete the workspace direcory
        cleanWs()
    }
    // Import the git repository
    stage ('Cloning of the repository') {
        // Git pipeline integrated command
        git (
            // Branch of the repository to import
            branch: 'dev',
            // Url of the repository
            url: 'https://gitlab.com/project-b-its/ansible-tests.git'
            )
    }
    // Start the ansible playbook in the Ansible container
    stage ('Launch of the Playbook') {
        // Docker pipeline integrated command
        docker
        // Name of the image to use
        .image('localhost:5000/ansible')
        // Property to do commands inside tha container
        // Go inside as root to avoid permission errors
        .inside('-u root --network projectb_default'){
            // Go to the existing directory (if no directory, it will create one)
            dir("host_interaction"){
                // ansiblePlaybook invoke via jenkins
                ansiblePlaybook(
                    // Path of the playbook but other options can be added
                    playbook: "host-int-playbook.yml"
                )
            }
        }
    }
}
                """)
        }
    }
}

