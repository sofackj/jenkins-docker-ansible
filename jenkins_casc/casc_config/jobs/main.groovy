// Example of pipelines ready to use after first login
// Initiate Project B
pipelineJob("initiation") {
        definition {
            cps {
                sandbox(true)
                script("""
node ('dockerHost') {
    stage("Check docker node") {
        def my_build = build (
            job: "check-node",
            propagate: false,
        )
    }
    stage("Setup Registry") {
        def my_build = build (
            job: "registry-process",
            propagate: false,
        )
    }
    stage('Check temporary containers') {
        parallel(
            "rocky agent" :
            {
                stage("rocky agent") {
                    def my_build = build (
                        job: "check-agents",
                        propagate: false,
                    )
                }
            },
            "ansible controller" :
            {
                stage("ansible controller") {
                    def my_build = build (
                        job: "check-ansible",
                        propagate: false,
                    )
                }
            }
        )
    }
}
                """)
        }
    }
}
// Check docker node
pipelineJob("check-node") {
        definition {
            cps {
                sandbox(true)
                script("""
try {
    timeout(time: 120, unit: 'SECONDS') {
        node('dockerHost'){
            echo "Status Docker Host => OK"
        }
    }
} catch(err) {
    error("Status Docker Host => DOWN")
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
            timeout(time: 240, unit: 'SECONDS') {
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
        cpsScm {
            scm {
                git {
                    branch('*/dev')
                    remote {
                        url('https://gitlab.com/project-b-its/ansible-tests.git')
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
// How to use trigger via the configure block
pipelineJob("generate-token") {
    definition {
        cpsScm {
            scm {
                git {
                    branch('*/dev')
                    remote {
                        url('https://gitlab.com/project-b-its/api-token-generator.git')
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