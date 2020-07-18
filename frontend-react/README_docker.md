# Project structure
This project contains two different dockerfiles, one for production (`Dockerfile`) and for "local development" (`Dockerfile.local`). The `Dockerfile.local` is kind of half-assed with resepect to using it for development (due to (currently) the `package.json` file being overriden and therefore not proxying correctly, it's therefore better to just run `npm start` locally and make sure that the container running the backend has its ports exposed to the docker host. 

The `.dockerignore`-file specifies which files and folders should be ignored when copying files from this directory (i.e `COPY . ./`) to the image during image build.

# Production build
The production build utilizes "[multistage build](https://docs.docker.com/develop/develop-images/multistage-build/)" for building the production image so we won't have to first manually build the project locally, then build the docker image from our local files.

This production build uses `nginx` to host the generated React-frontend and to also proxy the backend calls. To get nginx to correctly proxy the backend I hade to create a configuration file `nginx.conf` for the nginx-server. The key point in the configuration file is the following part which enables proxying to another container on the same `docker network`named `backend`;
```
location ~* (?:\/[^.]+)+$ {
    proxy_pass http://backend:8080;
} 
```
The proxy-url is hard-coded to `backend:8080`, for this to work with running a running docker-container (or `docker-compose` I assume (I haven't gotten to that part yet)) named "backend" (i.e `--name backend`) and be listening on port "8080" (doesn't need to be exposed using `-p 8080:8080`). The container must be connected to the same local docker network as the container named "backend".

## Building and running the image
To build and run this image, do the following:
```shell script
# Docker automatically uses the file named Dockerfile
docker build -t frontend-production .

docker run -itd \
-p 3000:80\ # The nginx-configuration specifies that port 80 is the listening port, here we override the Dockerfile's default port EXPOSE
--name frontend-prod\
--net docker-apps\  # Assumes that the backend-container also runs in the docker-apps network
frontend-production
```

# Local build
The local build is somewhat simpler than the production build since we're directly using a node-image as the base-image in order to run the application. The only "gotcha" here is the `sed` command which is used to alter the proxy-url defined in `package.json`, this is necessary for the frontend-container to be able to communicate with the backend-container.

## Building and running the image
To build and run this image, do the following:
```shell scripts
# Docker wil automatically use the file named Dockerfile, therefore we specify a different file
docker build -t frontend-development -f Dockerfile.local .

# Mode 1: Run the image without the ability to modify the frontend-code
docker run -itd\
-p 3001:3000\
--name frontend-dev
--net docker-apps
frontend-development

# Mode 2: Run the image with the ability to modify the frontend-code
docker run -itd\
-p 3001:3000\
--name frontend-dev
--net docker-apps
-v "D:\MJDev\docker-apps\frontend-react":/app
-v /app/node_modules    # When we ommit : we basically ignore the docker host folder node_modules. node_modules is connected to an empty volume, therefore no files will be overridden
frontend-development
```
Note that when we run the frontend in "Mode 2" the docker container will ignore our `sed`ed package.json and insted use the `package.json`-file on the docker host which will most likely use the "wrong" proxy (i.e `http://localhost:8001` instead of `http://backend:8080`). For the frontend to communicate with the backend in "Mode 2", the backend must be exposed to the docker host on port 8001 (i.e starting the backend-container with `-p 8001:8080`).