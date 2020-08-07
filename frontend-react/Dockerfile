# multistage build - https://docs.docker.com/develop/develop-images/multistage-build/
# build environment
FROM node:12-alpine AS builder
WORKDIR /app
COPY . ./
RUN npm install
RUN npm run build

# production environment
FROM nginx:stable-alpine
ARG BACKEND_HOST=backend
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/build /usr/share/nginx/html
RUN sed -i s/backend:8080/${BACKEND_HOST}:8080/ /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]