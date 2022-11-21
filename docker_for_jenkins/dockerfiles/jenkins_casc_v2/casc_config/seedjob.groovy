// create an array with our two pipelines
pipelines = ["first-test"]

// iterate through the array and call the create_pipeline method
pipelines.each {
    pipeline ->
    println "Creating pipeline ${pipeline}"
    create_pipeline(pipeline)
}

def create_pipeline(String name) {
    pipelineJob('job-dsl-plugin') {
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url('https://github.com/jenkinsci/job-dsl-plugin.git')
                        }
                        branch('*/master')
                    }
                }
                lightweight()
            }
        }
    }
}