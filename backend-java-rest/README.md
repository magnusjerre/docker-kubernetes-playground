# Application description
This application is a REST service that connects to a ``postgres``-database and reads/writes the contents of a file named `description.txt`. The database must be available when starting this application.
#### Env-variables
- __STORAGE_DIR__ [required] - specify the folder location for the ``description.txt``-file
- __DB_HOST__ [optional] - specify a different `postgres`-host than `localhost`

## Endpoints
### GET ``/persons``
This endpoint returns a list of the names of the persons stored in the postgres-database. The database must contain a table named `person` with the following information.

| Column | Type | Modifier |
| :---: | :---: | :---: |
| id | Integer | Primary key, Not null |
| name | Text | Not null |

### GET `/description`
The contents of the file named `description.txt` located in the `STORAGE_DIR` path (defined as an env-variable) 

### POST `/description`
Replace the contents of the file named `description.txt` located in the `STORAGE_DIR` path (defined as an env-variable)

# Building the docker `image`
This will build a docker image named `backend-java`, use either ``Dockerfile`` or ``Dockerfile.multistage``.
```shell script
# Must have jar-file availble locally before running this version of 'docker build' 
docker build -t backend-java .

# The multistage build isn't dependent on a locally available maven installation  
docker build -t backend-java -f Dockerfile.multistage .
```
Before building an image using the `Dockerfile`, the project must have been locally packaged using maven so that the necessary jar-file has been generated. If using `Dockerfile.multistage` we won't have to package the jar file locally.

# Running the container
This application requires a running postgres-database to be available on startup. When starting up our container we must consider how/where the database is made available (see [Different database urls](#different-database-urls)) and how/where the `description.txt` file should med made available (see [Persisting the text file](#persisting-the-text-file)).

The following examples assume that the built `docker image` is named `backend-java`.  
```shell script
# Create the docker network (named backend-network) that the postgres-container and backend-container can communicate through 
docker network create backend-network

# Run the postgres-container, name it 'postgres-db', connect it to 'backend-network'
# An empty database will be created, should manually create the correct table with data 
docker run --name postgres-db --net backend-network -p 5432:5432 -e POSTGRES_PASSWORD=password postgres:latest

# Create our volume
docker volume create backend-volume

# Start the container, connect it to the database and bind it to our volume
docker run --name backend \
--network backend-network \
-e DB_HOST=postgres-db \
-v backend-volume:/usr/app/configs \
-p 8001:8080
backend-java
```
By running the above commands in the `terminal` or `command prompt`, the app will be exposed on the docker host at port 8001, i.e use `curl localhost:8001/persons`.

## Different database urls
### Postgres server on localhost
If we are running the postgres-server on our local machine we can't use the default `localhost`-host defined in `application.properties` since the docker container doesn't have access to the docker host's `localhost`. To access the docker host machine's `localhost` (when using Docker Desktop) we can use `host.docker.internal`, see the docs [documentation](https://docs.docker.com/docker-for-mac/networking/#use-cases-and-workarounds) (only works in production for Mac and Windows). For Linux see [documentation](https://docs.docker.com/network/host/).

Example 
```shell script
# Docker Desktop for Mac and Windows
docker run -e DB_HOST=host.docker.internal -p 8001:8080 backend-java

# Docker on Linux
docker run --network host -e DB_HOST=127.0.0.1 -p 8001:8080 backend-java
```

### Postgres server hosted on a different machine
If we are running the postgres-server on a different machine we must override the env-variable `DB_HOST` with the correct ip-address / hostname, i.e: 
```shell script
docker run -e DB_HOST=other.machine.host backend-java
```

### Postgres server hosted on the local docker network
If we are running the postgres-server through docker in a `docker container` the postgres-container must be made available on a `docker network`, its port must be exposed and our backend application must be added to the same network and reference the database-container name.

Example:
```shell
# Create the docker network (named backend-network) that the postgres-container and backend-container can communicate through 
docker network create backend-network

# Run the postgres-container, name it 'postgres-db', connect it to 'backend-network' 
docker run --name postgres-db --net backend-network -p 5432:5432 -e POSTGRES_PASSWORD=password postgres:latest

# Run the backend-container, connect it to 'backend-network', use the postgres-container name 'postgres-db' as DB_HOST
docker run --name backend --net backend-network -e DB_HOST=postgres-db -p 8001:8080 backend-java
```

The "`docker host`" will correctly route the traffic from `backend-java` to `postgres-db` since we are using the container name of our postgres-container when making our calls to the database.

## Persisting the text file

## Persisting the `description.txt` in the container only
By persisting the `description.txt` in the `docker container` only (i.e not using `-v` or `--mount`), the file and its changes will not be shared between containers based on the same image. If the container is killed and removed the changes to the file are lost.
```shell script
docker run --name backend -p 8001:8080 -e DB_HOST=other.machine.host backend-java
```
To access the file in the container we can use `docker exec backend less /usr/app/configs` and it will print its contents to the terminal. THe path `/usr/app/configs` is specified in the Dockerfile.

## Persisting the `description.txt` on the docker host
Persisting the `description.txt` in a file stored on the docker-host. The file changes are persisted between container start-ups and is accessible on the docker host. If we edit the file on the docker host the changes will be visible in the container.

The following snippet binds the docker host's `/Users/Magnus/Documents` folder to the container folder `/usr/app/configs` and thereby lets us persist the file between container startup. This can also be useful when we need to store a database file on the docker host. 
```shell script
# using -v
docker run --name backend -v /Users/Magnus/Documents:/usr/app/configs -p 8001:8080 -e DB_HOST=other.machine.host backend-java

# or using --mount
docker run --name backend --mount type=bind,source=/Users/Magnus/Documents,target=/usr/app/configs -p 8001:8080 -e DB_HOST=other.machine.host backend-java
```

## Persisting `description.txt` using Docker's `volume`
Docker `volume` is the platform agnostic way of persisting files between container startups, a description of docker volumes can be found [here](https://docs.docker.com/storage/volumes/).

We can either start a container with a "reference" to a volume that does not yet exist (will automatically be created for us), or we can manually create the volume before creating the container. First we ensure that we have a docker volume available, then we can mount the volume to our container.

```shell script
# first create our volume
docker volume create backend-volume

# start the container and bind the backend-volume to our container's configs-folder
docker run --name backend -v backend-volume:/usr/app/configs -p 8001:8080 -e DB_HOST=other.machine.host backend-java
```
