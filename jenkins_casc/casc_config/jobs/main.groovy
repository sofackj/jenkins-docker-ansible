// Example of pipelines ready to use after first login
// Initiate Project B
pipelineJob("init-system") {
        definition {
            cps {
                sandbox(true)
                script("""
node('jenkins') {
    stage("Ping Docker Host"){
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

