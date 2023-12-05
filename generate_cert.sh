#!/bin/bash

mkdir -p data/certbot/letsencrypt/live/rozgrywkitenisa.pl
openssl req -nodes -newkey rsa:2048 -keyout data/certbot/letsencrypt/live/rozgrywkitenisa.pl/privkey.pem -x509 -out data/certbot/letsencrypt/live/rozgrywkitenisa.pl/fullchain.pem
