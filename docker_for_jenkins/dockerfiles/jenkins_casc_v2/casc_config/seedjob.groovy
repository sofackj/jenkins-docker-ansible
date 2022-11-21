// create an array with our two pipelines
pipelines = ["first-test"]

// iterate through the array and call the create_pipeline method
pipelines.each {
    pipeline ->
    println "Creating pipeline ${pipeline}"
    create_pipeline(pipeline)
}

def create_pipeline(String name) {
    pipelineJob(name) {
        definition {
            cps {
                sandbox(true)
                script(
                    """
node(){
    stage("first"){
        echo "Hello World !!!"
    }
    stage("second"){
        echo "Bye World !!!"
    }
}
                    """
                )
            }
        }
    }
}