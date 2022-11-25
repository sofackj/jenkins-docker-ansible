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
// pipelineJob("check-container") {
//         definition {
//             cps {
//                 sandbox(true)
//                 script("""
// def list_containers = ["registry"]
// def STATUS
// node('dockerHost'){
//     stage('Define variables') {
//     }
//     list_containers.each { container ->
//         stage("Check Status ${container}") {
//             echo "HELLO"
//         }
//         stage("Specs ${container}") {
//             def NETWORK = shell_output ("docker inspect -f '{{ .HostConfig.NetworkMode }}' ${container}")
//             echo "STATUS      => ${shell_output ("docker exec registry echo OK")}"
//             echo "ID          => ${shell_output ("docker inspect -f '{{ .Id }}' ${container}")}"
//             echo "Image       => ${shell_output ("docker inspect -f '{{ .Config.Image }}' ${container}")}"
//             echo "Network     => ${shell_output ("docker inspect -f '{{ .HostConfig.NetworkMode }}' ${container}")}"
//             echo "IP Address  => ${shell_output ("docker inspect -f '{{ .NetworkSettings.Networks.${NETWORK}.IPAddress }}' ${container}")}"
//         }
//     }
// }
// def shell_output (String command) {
//     def command_output = sh(
//         script: command,
//         returnStdout: true
//         ).trim()
//     return command_output
// }
//                 """)
//         }
//     }
// }


