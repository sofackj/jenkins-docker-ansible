// // create an array with our two pipelines
// pipelines = ["first-test"]

// // iterate through the array and call the create_pipeline method
// pipelines.each {
//     pipeline ->
//     println "Creating pipeline ${pipeline}"
//     create_pipeline(pipeline)
// }

// def create_pipeline(String name) {
pipelineJob(name) {
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
                        credentials('my-git-credentials')
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
// }