FROM nginx:1.23.3

COPY nginx-entrypoint.sh /usr/local/bin/nginx-entrypoint.sh


RUN rm /etc/nginx/conf.d/default.conf
COPY default.conf /etc/nginx/conf.d/default.conf