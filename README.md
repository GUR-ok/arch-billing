Микросервис Биллинга

Сборка и установка в minikube
1) `gradle build`
2) `docker build -t gurok/arch_billing .`
3) `docker push gurok/arch_billing`
4) `kubectl create namespace arch-gur`
5) `helm install arch-billing ./deployment/app/`
   `kubectl get pods -n arch-gur`

---
### Очистка пространства:

- `helm uninstall arch-billing`
- `kubectl delete namespace arch-gur`