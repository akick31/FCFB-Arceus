#!/bin/bash

# Start Nginx in the background
service nginx start

# Obtain SSL certificates if they don't exist
if [ ! -f "/etc/letsencrypt/live/api.fakecollegefootball.com/fullchain.pem" ]; then
    certbot --nginx --non-interactive --agree-tos --email nfcaaofficial@gmail.com -d api.fakecollegefootball.com
fi

# Renew certificates in the background
while :; do
    certbot renew --quiet
    sleep 12h
done &

# Start the Spring Boot application
exec java -Djava.awt.headless=true -jar /app/app.jar