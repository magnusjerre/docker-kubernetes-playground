# multistage build - https://docs.docker.com/develop/develop-images/multistage-build/
# build environment
FROM node:12-alpine AS builder
WORKDIR /app
COPY . ./
RUN npm install
RUN npm run build

# production environment
FROM nginx:stable-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]