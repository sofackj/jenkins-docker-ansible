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

pipelineJob("check") {
    configure { project ->
        project / 'definition' {
            'script'('''
            node('nodeHost'){
                echo "Hello World"
            }
            ''')
        }
    }
        definition {
            cps {
                sandbox(true)
        }
    }
}

