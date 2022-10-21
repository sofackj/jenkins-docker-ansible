FROM rockylinux:8.6.20227707

# Make sure the package repository is up to date.
RUN dnf update -y && \
    dnf install -qy git && \
# Install a basic SSH server
    dnf install -qy openssh-server && \
    sed -i 's|session    required     pam_loginuid.so|session    optional     pam_loginuid.so|g' /etc/pam.d/sshd && \
    mkdir -p /var/run/sshd && \
# Install JDK 11
    dnf install -qy java-11-openjdk.x86_64 && \
# Cleanup old packages
    dnf -qy autoremove && \
# Add user jenkins to the image
    useradd -d /home/jenkins -m jenkins && \
# Set password for the jenkins user (you may want to alter this).
    echo "jenkins:password" | chpasswd && \
    mkdir /home/jenkins/.m2 && \
    rm -rf /run/nologin/ && \
    ssh-keygen -A

# Copy authorized keys
COPY .ssh/authorized_keys /home/jenkins/.ssh/authorized_keys

# Setup ownership for jenkins user
RUN chown -R jenkins:jenkins /home/jenkins/.m2/ && \
    chown -R jenkins:jenkins /home/jenkins/.ssh/

# Standard SSH port
EXPOSE 22

# Start ssh service
CMD ["/usr/sbin/sshd", "-D"]
