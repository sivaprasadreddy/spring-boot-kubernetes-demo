#!/bin/bash

declare project_dir=$(dirname $0)
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
    docker-compose -f ${dc_monitoring} rm -f
}

function perf_test() {
    ./mvnw gatling:test -pl bookmarks-gatling-tests
}
function build_api() {
    ./mvnw clean package -DskipTests
}

function buildImages() {
    ./mvnw spring-boot:build-image -pl bookmark-service
}

function pushImages() {
    buildImages
    #docker tag sivaprasadreddy/bookmark-service sivaprasadreddy/bookmark-service:v2
    docker push sivaprasadreddy/bookmark-service

}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$@
fi

eval ${action}
