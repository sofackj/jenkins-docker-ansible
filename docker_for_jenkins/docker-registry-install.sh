#!/bin/bash

sudo -i <<EOF

# Start the registry container after pulling the registry image
docker run -d -p 5000:5000 --restart=always --name registry registry:2


EOF