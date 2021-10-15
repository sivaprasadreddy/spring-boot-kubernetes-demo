# SpringBoot Kubernetes Demo

```shell
./run.sh buildImages
./run.sh pushImages
kubectl create ns k8sdemo
kubectl config set-context --current --namespace=k8sdemo
./run.sh k8sDeploy
./run.sh k8sUndeploy
```

$ minikube ip
192.168.99.105

Browser Console:
localStorage.setItem("API_BASE_URL", "http://192.168.99.105:30099");
