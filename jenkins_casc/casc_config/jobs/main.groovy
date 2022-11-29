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
            node(&apos;dockerHost&apos;){
    //
    stage(&apos;Define variables&apos;){
        env.registry_status
        env.registry_output
        env.image_list
    }
    //
    stage(&apos;Check Registry Status&apos;){
        // Check if the registry container is running
        try {
           registry_status = sh(
            script: &quot;docker exec registry sh -c &apos;echo Check OK&apos;&quot;,
            returnStdout: true
            ).trim()
            echo &quot;${registry_status}&quot;
        // If exit error occurs, the pipeline fails
        } catch (err) {
            error(&quot;Check if the &apos;registry&apos; container is running properly&quot;)
        }
    }
    //
    stage(&apos;Display registered images&apos;){
        // Catch the output listing images list
        registry_output = sh(
            script: &quot;curl -s http://localhost:5000/v2/_catalog&quot;,
            returnStdout: true
            ).trim()
        // Convert the precedent string output to a list
        images_list = convert_registry_output(registry_output)
        echo &quot;${images_list}&quot;
    }
}
// Useful function for the pipeline
def convert_registry_output(String name) {
    my_list = []
    pre_list = name.split(&apos;:&apos;)[1][1..-3].split(&apos;,&apos;)
    pre_list.each { item -&gt;
            my_list &lt;&lt; item[1..-2]
        }
    return my_list
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

