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
// Check containers
pipelineJob("check-container") {
        definition {
            cps {
                sandbox(true)
                script("""
def list_containers = ["registry"]
def STATUS
node('dockerHost'){
    stage('Define variables') {
    }
    list_containers.each { container ->
        stage("Check Status ${container}") {
            echo "HELLO"
        }
    }
}
                """)
        }
    }
}


