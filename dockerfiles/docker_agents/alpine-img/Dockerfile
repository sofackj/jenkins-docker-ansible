# on part d'une image alpine 
FROM alpine:3.7

# on ajoute openssh pour la connexion ssh
# et openjdk qui est requis par jenkins
RUN apk add openssh openjdk8-jre-base

# on ajoute git pour accéder au repo
RUN apk add git

# gcc et make 
RUN apk add gcc make

# et enfin les bibliothèques dont le projet a besoin
RUN apk add libc-dev libsndfile-dev

# un peu de configuration
# un script pour générer des clefs ssh 
COPY ssh-slave-entrypoint.sh /usr/bin/

# en le rendant executable (est-ce bien nécessaire ?)
RUN chmod +x /usr/bin/ssh-slave-entrypoint.sh

# on efface d'éventuels anciennes clefs (même remarque)
RUN rm -rf /etc/ssh/ssh_host_rsa_key /etc/ssh/ssh_host_dsa_key

# on crée un utilisateur
RUN adduser -D jenkins

# auquel on ajoute un mot de passe
RUN echo "jenkins:jenkins" | chpasswd

# et on crée un répertoire pour ranger le bazard jenkins
RUN mkdir /home/jenkins/jenkins_slave

# que l'on donne à l'utilisateur
RUN chown jenkins /home/jenkins/jenkins_slave

# on ouvre le port 22 pour ssh
EXPOSE 22

# on définit le script copié plutôt comme le point d'entrée
ENTRYPOINT ["ssh-slave-entrypoint.sh"]

# et on lance le serveur ssh au démarrage de la machine
CMD ["/usr/sbin/sshd", "-D"]
