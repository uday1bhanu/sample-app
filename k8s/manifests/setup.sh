kubectl apply -f internal-service-config.yaml
kubectl apply -f sample-app-secret.yaml

kubectl apply -f geekjoke.yaml
sleep 20
kubectl apply -f dadjoke.yaml
sleep 20
kubectl apply -f joke.yaml
sleep 20
kubectl apply -f about.yaml
sleep 20
kubectl apply -f location.yaml
sleep 20
kubectl apply -f sample-app.yaml