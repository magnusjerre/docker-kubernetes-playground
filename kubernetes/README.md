# Getting playground-db to run
In order to get the database to run on kubernetes using the command line do the following:
1. Upload the locally built image to dockerhub, image name `jerre/playground-db:v1.0.0`.
2. Postgresql requires the env-variable `POSTGRES_PASSWORD` to be exist during startup, therefore we create a `ConfigMap` named `db-config`like so:
```shell script
# contents of db.properties
# POSTGRES_PASSWORD=password

kubectl create configmap db-config --from-file=db.properties
> configmap/db-config created
```

