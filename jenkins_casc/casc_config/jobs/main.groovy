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
            timeout(time: 30, unit: 'SECONDS') {
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

