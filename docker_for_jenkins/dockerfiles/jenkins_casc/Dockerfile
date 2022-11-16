FROM jenkins/jenkins:lts-jdk11

# Environment variables
# Avoid the pass by the statndard setup
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
# Variables for the configuration file path
ENV CASC_JENKINS_CONFIG /var/jenkins_home/casc.yml

# Install plugins for jenkins
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
# In an older version the command used was :
# RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
# Now this one is used
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt

# Setup the configuration as code file
COPY casc.yml /var/jenkins_home/casc.yml