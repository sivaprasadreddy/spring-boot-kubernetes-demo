#!/bin/bash

declare project_dir=$(dirname $0)
declare project_version='0.0.2'
declare dc_app=${project_dir}/docker/docker-compose.yml


function start() {
    build_api
    echo "Starting docker containers...."
    docker-compose -f ${dc_app} up --build --force-recreate -d
    docker-compose -f ${dc_app} logs -f
}

function stop() {
    echo "Stopping docker containers...."
    docker-compose -f ${dc_app} stop
    docker-compose -f ${dc_app} rm -f
}

function restart() {
    stop
    start
}
function build_api() {
    ./mvnw clean package -DskipTests
}

function buildImages() {
#    ./mvnw spring-boot:build-image -pl votes-service
#    ./mvnw spring-boot:build-image -pl bookmarks-service
#    ./mvnw spring-boot:build-image -pl service-registry
#    ./mvnw spring-boot:build-image -pl api-gateway
#    ./mvnw spring-boot:build-image -pl bookmarks-ui

    ./mvnw clean package jib:build -pl votes-service
    ./mvnw clean package jib:build -pl bookmarks-service
    ./mvnw clean package jib:build -pl service-registry
    ./mvnw clean package jib:build -pl api-gateway
    ./mvnw clean package jib:build -pl bookmarks-ui
}

function pushImages() {
    buildImages

    docker tag sivaprasadreddy/bookmarks-service sivaprasadreddy/bookmarks-service:${project_version}
    docker tag sivaprasadreddy/votes-service sivaprasadreddy/votes-service:${project_version}
    docker tag sivaprasadreddy/service-registry sivaprasadreddy/service-registry:${project_version}
    docker tag sivaprasadreddy/api-gateway sivaprasadreddy/api-gateway:${project_version}
    docker tag sivaprasadreddy/bookmarks-ui sivaprasadreddy/bookmarks-ui:${project_version}

    docker push sivaprasadreddy/bookmarks-service --all-tags
    docker push sivaprasadreddy/votes-service --all-tags
    docker push sivaprasadreddy/service-registry --all-tags
    docker push sivaprasadreddy/api-gateway --all-tags
    docker push sivaprasadreddy/bookmarks-ui --all-tags
}

function k8sDeploy() {
    kubectl apply -f k8s/1-config.yaml
    sleep 3
    kubectl apply -f k8s/2-bookmarks-postgresdb.yaml
    sleep 3
    kubectl apply -f k8s/3-votes-postgresdb.yaml
    sleep 3
    kubectl apply -f k8s/4-service-registry.yaml
    sleep 3
    kubectl apply -f k8s/5-bookmarks-service-app.yaml
    sleep 3
    kubectl apply -f k8s/6-votes-service-app.yaml
    sleep 3
    kubectl apply -f k8s/7-api-gateway.yaml
    sleep 3
    kubectl apply -f k8s/8-bookmarks-ui-app.yaml
#    sleep 3
#    kubectl apply -f k8s/9-ingress.yaml
}

function k8sUndeploy() {
    kubectl delete -f k8s/9-ingress.yaml
    kubectl delete -f k8s/8-bookmarks-ui-app.yaml
    kubectl delete -f k8s/7-api-gateway.yaml
    kubectl delete -f k8s/6-votes-service-app.yaml
    kubectl delete -f k8s/5-bookmarks-service-app.yaml
    kubectl delete -f k8s/4-service-registry.yaml
    kubectl delete -f k8s/3-votes-postgresdb.yaml
    kubectl delete -f k8s/2-bookmarks-postgresdb.yaml
    kubectl delete -f k8s/1-config.yaml
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
