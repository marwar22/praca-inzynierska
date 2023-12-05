FROM nginx

COPY custom-entrypoint.sh /


ENTRYPOINT ["/custom-entrypoint.sh"]
CMD ["nginx", "-g", "daemon off;"]

