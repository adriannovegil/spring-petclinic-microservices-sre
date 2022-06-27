# Starting Services Locally with Kubernetes (Minikube)

This get a little bit more complicated when deploying to Kubernetes, since we need to manage Docker images, exposing services and more yaml. But we can pull through!

## Start Minikube

Start the cluster

```bash
minikube start --memory 16384 --cpus 2 
```

Then, run a kubectl command to verify it looks good:

```bash
→ kubectl get nodes
NAME           STATUS   ROLES                  AGE   VERSION
minikube       Ready    control-plane,master   51s   v1.22.2
```

Launch the Kubernetes dashboard

```bash
minikube dashboard
```

## Build the Docker images and push into the docker registry

When using a container or VM driver (all drivers except none), you can reuse the Docker daemon inside minikube cluster. 

This means you don’t have to build on your host machine and push the image into a docker registry. You can just build inside the same docker daemon as minikube which speeds up local experiments.

To point your terminal to use the docker daemon inside minikube run this:

```bash
eval $(minikube docker-env)
```

Now any `docker` command you run in this current terminal will run against the docker inside minikube cluster.

So if you do the following commands, it will show you the containers inside the minikube, inside minikube’s VM or Container.

```bash
docker ps
```

One of the neat features in Spring Boot 2.3 is that it can leverage [Cloud Native Buildpacks](https://buildpacks.io) and [Paketo Buildpacks](https://paketo.io) to build production-ready images for us.

Since we also configured the `spring-boot-maven-plugin` to use `layers`, we'll get optimized layering of the various components that build our Spring Boot app for optimal image caching. 

What this means in practice is that if we simple change a line of code in our app, it would only require us to push the layer containing our code and not the entire uber jar. 

To build all images and pushing them to your registry, run:

```bash
./mvnw spring-boot:build-image -Pk8s
```

Since these are standalone microservices, you can also `cd` into any of the project folders and build it individually (as well as push it to the registry).

You should now have all your images in your Docker registry. It might be good to make sure you can see them available.

## Setting things up in Kubernetes

Create the `spring-petclinic` namespace for Spring petclinic:

```bash
kubectl apply -f k8s/00-init-namespace/
```

Create the Kubernetes services that will be used later on by our deployments:

```bash
kubectl apply -f k8s/01-init-configmaps/
kubectl apply -f k8s/02-init-roles/
kubectl apply -f k8s/03-init-services/
```

Verify the services are available:

```bash
→ kubectl get svc -n spring-petclinic
NAME                TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
api-gateway         LoadBalancer   10.106.43.28     <pending>     80:31544/TCP   12s
customers-service   ClusterIP      10.111.82.119    <none>        8080/TCP       12s
vets-service        ClusterIP      10.110.246.215   <none>        8080/TCP       12s
visits-service      ClusterIP      10.111.109.244   <none>        8080/TCP       12s
```

## Settings up databases with helm

We'll now need to deploy our databases. For that, we'll use helm. You'll need helm 3 and above since we're not using Tiller in this deployment.

Make sure you have a single `default` StorageClass in your Kubernetes cluster:

```bash
→ kubectl get sc
NAME                 PROVISIONER                RECLAIMPOLICY   VOLUMEBINDINGMODE   ALLOWVOLUMEEXPANSION   AGE
standard (default)   k8s.io/minikube-hostpath   Delete          Immediate           false                  25m

```

Deploy the databases. 

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm install vets-db-mysql bitnami/mysql --namespace spring-petclinic --version 8.8.8 --set auth.database=service_instance_db
helm install visits-db-mysql bitnami/mysql --namespace spring-petclinic  --version 8.8.8 --set auth.database=service_instance_db
helm install customers-db-mysql bitnami/mysql --namespace spring-petclinic  --version 8.8.8 --set auth.database=service_instance_db
```

## Deploying the application

Our deployment YAMLs have a placeholder called `REPOSITORY_PREFIX` so we'll be able to deploy the images from any Docker registry. 

Sadly, Kubernetes doesn't support environment variables in the YAML descriptors. We have a small script to do it for us and run our deployments:

```bash
export REPOSITORY_PREFIX=springcommunity
sh ./scripts/deploy-to-k8s.sh
```

Verify the pods are deployed:

```bash
→ kubectl get pods -n spring-petclinic 
NAME                                 READY   STATUS    RESTARTS   AGE
api-gateway-6764c947cf-t2phs         1/1     Running   0          25m
customers-db-mysql-0                 1/1     Running   0          46m
customers-service-687ffbd4c8-7nqmk   1/1     Running   0          25m
vets-db-mysql-0                      1/1     Running   0          46m
vets-service-5d48b74994-4r79l        1/1     Running   0          25m
visits-db-mysql-0                    1/1     Running   0          46m
visits-service-6b6cfd74fd-f9wqp      1/1     Running   0          25m
```

## Expose the service

To expose the `api-gateway` service in minikube you have to execute the following command:

```bash
minikube service -n spring-petclinic --url api-gateway                                                     
http://192.168.49.2:31544
```

## Update a Deployment image in Kubernetes

When changing the image I found this to be the most straightforward way to update it:

```bash
kubectl rollout restart deploy api-gateway -n spring-petclinic
kubectl rollout restart deploy customers-service -n spring-petclinic
kubectl rollout restart deploy vets-service -n spring-petclinic
kubectl rollout restart deploy visits-service -n spring-petclinic
```

## References

- https://github.com/spring-petclinic/spring-petclinic-cloud
- https://minikube.sigs.k8s.io/docs/handbook/pushing/
- https://minikube.sigs.k8s.io/docs/handbook/dashboard/
- https://medium.com/codex/setup-kuberhealthy-with-prometheus-and-grafana-on-minikube-b2f6da21dc2e
- https://medium.com/@jeanmorais/monitorando-aplica%C3%A7%C3%B5es-spring-boot-de-forma-escal%C3%A1vel-no-kubernetes-com-prometheus-operator-e-326f63bb5b00
- https://github.com/prometheus-operator/prometheus-operator/blob/main/Documentation/user-guides/getting-started.md
- https://blog.marcnuri.com/instalar-prometheus-grafana-minikube
- https://github.com/jeanmorais/springboot-prometheus-k8s-sample
- https://github.com/thomasdarimont/spring-boot-k8s-example-app-conference
- https://blog.container-solutions.com/prometheus-operator-beginners-guide
- https://github.com/loliksamuel/spring-decoupling-dockerized-k8s-hpa/tree/master
- https://levelup.gitconnected.com/observability-of-springboot-services-in-k8s-with-prometheus-and-grafana-61c4e7a9d814
