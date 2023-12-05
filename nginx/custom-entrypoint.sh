#!/bin/sh

exec ./docker-entrypoint.sh "$@" "-c" $([ -d '/etc/letsencrypt/live' ] && echo '/etc/nginx/config/nginx.conf' || echo '/etc/nginx/config/nginx_init_ssl_certificate.conf')
