# Building the docker `image`
This will build a docker image named `app-backend-rest-java`.
```shell script
docker build -t app-backend-rest-java .

# or using the multistage version
docker build -t app-backend-rest-java -f Dockerfile.multistage .
```
Before building the `Dockerfile`, the project must have been locally packaged using maven so that the necessary jar-file has been generated. IF using `Dockerfile.multistage` instead we won't have to package the jar file locally.

# Running the container explicitly
By running one of the following docker commands in the `terminal` or `command prompt`, the app will be exposed on the docker-host at port 8001. Accessible through `localhost:8001/users`.

## Persisting the usernames-file in the container only 
The file will be removed when the container is removed, therefore no changes will be persisted when a container is removed and a new one is created.
```shell script
docker run -it --rm --name backend -p 8001:8080 app-backend-rest-java
```

## Persisting the usernames-file on the docker-host
Persisting the usernames in a file stored on the docker-host. The file will be available between container startups and accessible on the docker-host.
```shell script
# Windows, command prompt
docker run -it --rm^
--name backend^
-p 8001:8080^ 
--mount type=bind,source=D:\MJDev\docker-apps\configs,target=/usr/app/configs^
app-backend-rest-java

# unix, terminal
docker run -it --rm\
 --name backend\
-p 8001:8080\
--mount type=bind,source=D:\MJDev\docker-apps\configs,target=/usr/app/configs\
app-backend-rest-java
```

## Persisting the usernames-file using docker's `volumes`
A description of docker volumes can be found [here](https://docs.docker.com/storage/volumes/). We can either start a container with a "reference" to a volume that does not yet exist (will automatically be created for us), or we can manually create the volume before creating the container.
```shell script
docker volume create username-backend
docker run -it --rm --name backend -p 8001:8080 -v username-backend:/usr/app/configs app-backend-rest-java
```