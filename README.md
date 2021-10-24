# SpringBoot Kubernetes Demo

## Running using docker-compose

```shell
$ ./run.sh start
$ ./run.sh stop
```

## Minikube Setup

```shell
$ brew install minikube
$ minikube start --memory 4096 --driver=virtualbox
$ minikube addons enable ingress
$ minikube ip
192.168.99.105
```

Add host entries in /etc/hosts files

```shell
192.168.99.105 mybookmarks.com
192.168.99.105 api.mybookmarks.com
```

Override API Path:
```shell
$ minikube ip
192.168.99.105

Browser Console:
localStorage.setItem("API_BASE_URL", "http://api.mybookmarks.com");
```

```shell
./run.sh buildImages
./run.sh pushImages
kubectl create ns k8sdemo
kubectl config set-context --current --namespace=k8sdemo
./run.sh k8s_deploy
./run.sh k8s_undeploy
```

## Running services using Skaffold

```shell
brew install skaffold
skaffold dev --port-forward --skip-tests
```

## Swagger UI

* Aggregated API Gateway Swagger UI http://localhost:9090/swagger-ui.html


## ArgoCD
https://argo-cd.readthedocs.io/en/stable/

```shell
kubectl create namespace argocd
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml
kubectl apply -f argocd/application.yaml
# login with admin user and below token (as in documentation):
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 --decode && echo
kubectl port-forward svc/argocd-server 8989:443 -n argocd
```
