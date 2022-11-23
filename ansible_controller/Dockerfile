# Build tehe image from this base image
FROM rockylinux:8.6.20227707

# Update the system
RUN dnf update -qy &&\
# Install this pre-requisite allowing us to install ansible
dnf install epel-release -qy &&\
# Install ansible
dnf install ansible -qy

# Create this directory where the future command will start
# A link will be created to this directory when running the container
WORKDIR /ansible_files

# Display the ansible version when running the container without command
CMD ["ansible", "--version"]
