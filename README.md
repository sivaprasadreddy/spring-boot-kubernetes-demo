# SpringBoot Monitoring

A sample application to demonstrate monitoring SpringBoot applications using Prometheus, Grafana and ELK Stack.

## How to run?

1. To run application locally: `./runlocal.sh start` 

2. To run application in docker: `./run.sh restart`

3. To run Prometheus, Grafana: `./run.sh monitoring`

4. To run ELK Stack: `./run.sh elk`

5. To run Gatling performance tests: `./run.sh perf_test`

## Important URLs:
* Actuator: http://localhost:8080/actuator
* Swagger UI: http://localhost:8080/swagger-ui/index.html
* Postgresql: `jdbc:postgresql://localhost:5432/appdb`, credentials: `siva/secret`
* Zipkin: http://localhost:9411
* Prometheus: http://localhost:9090
* Grafana: http://localhost:3000
* ElasticSearch: http://localhost:9200
* Logstash: http://localhost:5000
* Kibana: http://localhost:5601

