FROM ubuntu:18.04

# Make sure the package repository is up to date.
RUN apt-get update && \
    apt-get install -qy git && \
# Install a basic SSH server
    apt-get install -qy openssh-server && \
    sed -i 's|session    required     pam_loginuid.so|session    optional     pam_loginuid.so|g' /etc/pam.d/sshd && \
    mkdir -p /var/run/sshd && \
# Install JDK 11
    apt-get install -qy openjdk-11-jdk && \
# Cleanup old packages
    apt-get -qy autoremove && \
# Add user jenkins to the image
    adduser -d /home/jenkins -m jenkins && \
# Set password for the jenkins user (you may want to alter this).
    echo "jenkins:password" | chpasswd && \
    mkdir /home/jenkins/.m2

# Copy authorized keys
COPY .ssh/authorized_keys /home/jenkins/.ssh/authorized_keys

# Setup ownership for jenkins user
RUN chown -R jenkins:jenkins /home/jenkins/.m2/ && \
    chown -R jenkins:jenkins /home/jenkins/.ssh/

# Standard SSH port
EXPOSE 22

# Start ssh service
CMD ["/usr/sbin/sshd", "-D"]
