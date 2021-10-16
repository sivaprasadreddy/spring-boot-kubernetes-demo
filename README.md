# SpringBoot Kubernetes Demo


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
192.168.99.105 my.bookmarks.com
192.168.99.105 api.my.bookmarks.com
```

Override API Path:
```shell
$ minikube ip
192.168.99.105

Browser Console:
localStorage.setItem("API_BASE_URL", "http://192.168.99.105:30099");
```

```shell
./run.sh buildImages
./run.sh pushImages
kubectl create ns k8sdemo
kubectl config set-context --current --namespace=k8sdemo
./run.sh k8sDeploy
./run.sh k8sUndeploy
```


