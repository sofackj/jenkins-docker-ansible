// Pipeline ready to use after first login
pipelineJob("my-pipeline") {
    triggers {
        githubPush()
    }
    parameters {
        booleanParam('FLAG', true)
        choiceParam('OPTION', ['option 1 (default)', 'option 2', 'option 3'])
    }
    definition {
        cpsScm {
            scm {
                git {
                    branch('*/dev')
                    remote {
                        url('https://github.com/sofackj/jenkins-docker-ansible.git')
                    }
                    extensions {
                        cleanAfterCheckout()
                    }
                }
                
            }
            scriptPath("jenkinsfile")
        }
    }
}
