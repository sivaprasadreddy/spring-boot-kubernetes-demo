#!/bin/bash

declare project_dir=$(dirname $0)
declare project_version='0.0.1'
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
#    ./mvnw spring-boot:build-image -pl url-metadata-service
#    ./mvnw spring-boot:build-image -pl bookmarks-service
#    ./mvnw spring-boot:build-image -pl api-gateway
#    ./mvnw spring-boot:build-image -pl bookmarks-ui

    ./mvnw clean package jib:build -pl url-metadata-service -DskipTests
    ./mvnw clean package jib:build -pl bookmarks-service -DskipTests
    ./mvnw clean package jib:build -pl api-gateway -DskipTests
    ./mvnw clean package jib:build -pl bookmarks-ui -DskipTests
}

function pushImages() {
    buildImages

    docker tag sivaprasadreddy/url-metadata-service sivaprasadreddy/url-metadata-service:${project_version}
    docker tag sivaprasadreddy/bookmarks-service sivaprasadreddy/bookmarks-service:${project_version}
    docker tag sivaprasadreddy/api-gateway sivaprasadreddy/api-gateway:${project_version}
    docker tag sivaprasadreddy/bookmarks-ui sivaprasadreddy/bookmarks-ui:${project_version}

    docker push sivaprasadreddy/url-metadata-service --all-tags
    docker push sivaprasadreddy/bookmarks-service --all-tags
    docker push sivaprasadreddy/api-gateway --all-tags
    docker push sivaprasadreddy/bookmarks-ui --all-tags
}

function k8s_deploy() {
    kubectl apply -f k8s
}

function k8s_undeploy() {
    kubectl delete -f k8s
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
