#!/bin/bash

sed -E "s/server [^\:]+:/server $1:/" /etc/nginx/nginx.conf > /tmp/nginx.conf
mv /tmp/nginx.conf /etc/nginx/nginx.conf

/usr/sbin/nginx -s reload
