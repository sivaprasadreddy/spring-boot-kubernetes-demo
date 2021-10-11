#!/bin/bash

declare project_dir=$(dirname $0)
declare project_version='0.0.1'
declare dc_app=${project_dir}/docker/docker-compose.yml
declare dc_elk=${project_dir}/docker/docker-compose-elk.yml
declare dc_monitoring=${project_dir}/docker/docker-compose-monitoring.yml

function restart() {
    stop
    start
}

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

function elk() {
    echo 'Starting ELK....'
    docker-compose -f ${dc_elk} up --build --force-recreate -d ${elk}
    docker-compose -f ${dc_elk} logs -f
}

function elk_stop() {
    echo 'Stopping ELK....'
    docker-compose -f ${dc_elk} stop
    docker-compose -f ${dc_elk} rm -f
}

function monitoring() {
    echo 'Starting Prometheus, Grafana....'
    docker-compose -f ${dc_monitoring} up --build --force-recreate -d ${monitoring}
    docker-compose -f ${dc_monitoring} logs -f
}

function monitoring_stop() {
    echo 'Stopping Prometheus, Grafana....'
    docker-compose -f ${dc_monitoring} stop
    #docker-compose -f ${dc_monitoring} rm -f
}

function build_api() {
    ./mvnw clean package -DskipTests
}

function buildImages() {
    ./mvnw spring-boot:build-image -pl vote-service
    ./mvnw spring-boot:build-image -pl bookmark-service
    ./mvnw spring-boot:build-image -pl bookmarks-ui

    #./mvnw clean package jib:build -pl vote-service
    #./mvnw clean package jib:build -pl bookmark-service
    #./mvnw clean package jib:build -pl bookmarks-ui
}

function k8sDeploy() {
    kubectl apply -f k8s/1-config.yaml
    kubectl apply -f k8s/2-bookmarks-postgresdb.yaml
    sleep 5
    kubectl apply -f k8s/3-votes-postgresdb.yaml
    sleep 5
    kubectl apply -f k8s/4-bookmark-service-app.yaml
    sleep 5
    kubectl apply -f k8s/5-vote-service-app.yaml
    sleep 5
    kubectl apply -f k8s/6-bookmarks-ui-app.yaml
}

function k8sUndeploy() {
    kubectl delete -f k8s/6-bookmarks-ui-app.yaml
    kubectl delete -f k8s/5-vote-service-app.yaml
    kubectl delete -f k8s/4-bookmark-service-app.yaml
    kubectl delete -f k8s/3-votes-postgresdb.yaml
    kubectl delete -f k8s/2-bookmarks-postgresdb.yaml
    kubectl delete -f k8s/1-config.yaml
}

function pushImages() {
    buildImages

    docker tag sivaprasadreddy/bookmark-service sivaprasadreddy/bookmark-service:${project_version}
    docker tag sivaprasadreddy/vote-service sivaprasadreddy/vote-service:${project_version}
    docker tag sivaprasadreddy/bookmarks-ui sivaprasadreddy/bookmarks-ui:${project_version}

    docker push sivaprasadreddy/bookmark-service
    docker push sivaprasadreddy/vote-service
    docker push sivaprasadreddy/bookmarks-ui
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
