 # Dockerfile
FROM node:14 as build
WORKDIR /app
COPY ./ ./
RUN npm install --force
RUN npm run build

FROM nginx:1.23.2
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]