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
        definition {
            cps {
                sandbox(true)
                script("""
def list_containers = [&quot;registry&quot;]
def STATUS

node(&apos;dockerHost&apos;){
    stage(&apos;Define variables&apos;) {
    }
    list_containers.each { container -&gt;
        stage(&quot;Check container : ${container}&quot;) {
            echo &quot;HELLO&quot;
        }
        stage(&quot;Display specs : ${container}&quot;) {
            echo &quot;&quot;&quot;
            STATUS      =&gt; ${shell_output (&quot;docker exec registry echo OK&quot;)}
            ID          =&gt; ${shell_output (&quot;docker inspect -f &apos;{{ .Id }}&apos; ${container}&quot;)}
            Image       =&gt; ${shell_output (&quot;docker inspect -f &apos;{{ .Config.Image }}&apos; ${container}&quot;)}
            Network     =&gt; ${shell_output (&quot;docker inspect -f &apos;{{ .HostConfig.NetworkMode }}&apos; ${container}&quot;)}
            IP Address  =&gt; ${shell_output (&quot;docker inspect -f &apos;{{ .NetworkSettings.Networks.projectb_default.IPAddress }}&apos; ${container}&quot;)}
            &quot;&quot;&quot;
        }
    }

}

def shell_output (String command) {
    def command_output = sh(
        script: command,
        returnStdout: true
        ).trim()
    return command_output
}
                """)
        }
    }
}

