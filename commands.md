```sh
cd deployment
```
```sh
kubectl config use-context docker-desktop
```
```sh
docker context use default
```
```sh
kubectl delete service --all
kubectl delete deployment --all
```
```sh
kubectl apply -f postgres-config-map.yaml
kubectl apply -f postgres-secrets.yaml
kubectl apply -f vehicles-db-deployment-service.yaml
kubectl apply -f tickets-db-deployment-service.yaml
kubectl apply -f repairs-db-deployment-service.yaml
kubectl apply -f bonus-db-deployment-service.yaml
kubectl apply -f reports-db-deployment-service.yaml
```

```sh
kubectl apply -f config-server-deployment-service.yaml
```
```sh
kubectl apply -f eureka-server-deployment-service.yaml
```
```sh
kubectl apply -f gateway-server-deployment-service.yaml
```

```sh
kubectl apply -f ms-tickets-deployment-service.yaml
kubectl apply -f ms-repairs-deployment-service.yaml
kubectl apply -f ms-vehicles-deployment-service.yaml
kubectl apply -f ms-bonus-deployment-service.yaml
kubectl apply -f ms-reports-deployment-service.yaml

kubectl apply -f frontend-deployment-service.yaml
```
### Deletions

```sh
kubectl delete -f config-server-deployment-service.yaml
kubectl delete -f eureka-server-deployment-service.yaml
kubectl delete -f gateway-server-deployment-service.yaml

```

```sh
kubectl delete -f ms-vehicles-deployment-service.yaml
kubectl delete -f ms-tickets-deployment-service.yaml
kubectl delete -f ms-repairs-deployment-service.yaml
kubectl delete -f ms-bonus-deployment-service.yaml
kubectl delete -f ms-reports-deployment-service.yaml
kubectl delete -f frontend-deployment-service.yaml
```




### Docker
```sh
Docker build -t user/imagename .
Docker push user/imagename
```

### Is it working?
```sh
kubectl get pods
```
----------------

### Para crearlas desde 0
```sh
kubectl exec -it nombrepod -- /bin/bash
create database example;

kubectl cp repairs-db.sql repairs-db-deployment-7dd9d475f6-s6r2b:/home

psql -U postgres example < repairs-db.sql
```
----------------
### Minikube start using Docker
```sh
minikube start --driver=docker
```
### Minikube dashboard
```sh
minikube dashboard
```