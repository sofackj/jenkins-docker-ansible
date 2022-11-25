// xample of pipelines ready to use after first login
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
// How to use trigger
pipelineJob("test") {
    // configure { project ->
    //     project / 'triggers' / 'jenkins.triggers.ReverseBuildTrigger' {
    //         'spec'('')
    //         'upstreamProjects'('my-pipeline')
    //         'threshold'('''
    //         <name>FAILURE</name>
    //         <ordinal>2</ordinal>
    //         <color>RED</color>
    //         <completeBuild>true</completeBuild>
    //         ''')
    //     }
    // }
    configure { project ->
        project / 'properties' / 'hello' {
            'one'('ok')
            'two'('ok')
        }
    }
    configure { project ->
        project / 'properties' / 'hello' / 'test' {
            'three'('ok')
            'four'('ok')
        }
    }
    definition {
        cps {
            sandbox(true)
            script("""
node('dockerHost') {
    stage("test trigger"){
        echo "Hello World"
    }
}
            """)
        }
    }
}
// Example to use later on
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

