# Imagen del proxy nginx que protege Supabase Studio con Basic Auth.
# Se hornea el nginx.conf dentro de la imagen para no depender de bind mounts
# (en Coolify una ruta relativa que no resuelve hace que Docker cree una
# carpeta vacia donde nginx espera un fichero).
# apache2-utils trae 'htpasswd', que genera el fichero de credenciales al arrancar.
FROM nginx:1.27-alpine

RUN apk add --no-cache apache2-utils

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 3000
