# Imagen del gateway nginx de Supabase con la configuracion ya incluida.
# Se hornea el nginx.conf dentro de la imagen para no depender de bind mounts
# (en Coolify una ruta relativa que no resuelve hace que Docker cree una
# carpeta vacia donde nginx espera un fichero).
FROM nginx:1.27-alpine

COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 8000
