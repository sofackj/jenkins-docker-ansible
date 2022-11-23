# on part d'une image alpine
FROM python:alpine3.16

ARG USER=devops

# on ajoute openssh pour la connexion ssh
# et openjdk qui est requis par jenkins
RUN apk add openssh docker openrc sudo \
        && apk add gcc make \
        && apk add libc-dev libsndfile-dev

# un peu de configuration
# un script pour générer des clefs ssh
COPY ssh-slave-entrypoint.sh /usr/bin/

# en le rendant executable (est-ce bien nécessaire ?)
RUN chmod +x /usr/bin/ssh-slave-entrypoint.sh \
        && rm -rf /etc/ssh/ssh_host_rsa_key /etc/ssh/ssh_host_dsa_key

# on crée un utilisateur
RUN adduser -D $USER && mkdir -p /etc/sudoers.d \
        && echo "$USER ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/$USER \
        && chmod 0440 /etc/sudoers.d/$USER \
        && echo "$USER:$USER" | chpasswd

# on ouvre le port 22 pour ssh
EXPOSE 22

# on définit le script copié plutôt comme le point d'entrée
ENTRYPOINT ["ssh-slave-entrypoint.sh"]

# et on lance le serveur ssh au démarrage de la machine
CMD ["/usr/sbin/sshd", "-D"]