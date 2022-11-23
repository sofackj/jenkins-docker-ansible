#!/bin/sh

if [ ! -f "/etc/ssh/ssh_host_rsa_key" ] ; then
    ssh-keygen -f /etc/ssh/ssh_host_rsa_key -N '' -t rsa
fi


if [ ! -f "/etc/ssh/ssh_host_dsa_key" ] ; then
    ssh-keygen -f /etc/ssh/ssh_host_dsa_key -N '' -t dsa
fi 

if [ ! -d "/var/run/sshd" ]; then
    mkdir -p /var/run/sshd
fi

exec "$@"
