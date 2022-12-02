// How to use trigger via the configure block
pipelineJob("test") {
    configure { project ->
        project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' {
            'spec'('')
            'upstreamProjects'('my-pipeline')
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
node('dockerHost') {
    stage("test trigger"){
        echo "This job has been triggered by init-system"
    }
}
            """)
        }
    }
}

// Exampleof a pipeline with SCM
pipelineJob("my-pipeline") {
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