Микросервис Биллинга

Сборка и установка в minikube
1) `gradle build`
2) `docker build -t gurok/arch_billing_2 .`
3) `docker push gurok/arch_billing_2`
4) `kubectl create namespace arch-gur`
5) `helm install arch-billing ./deployment/app/`
   `kubectl get pods -n arch-gur`
   
---

Для локального поднятия кафки: `docker-compose -f .\docker-compose-kafka.yml up`

Пример сообщения в Кафку:

{"event":"DEPOSIT_REQUEST", "accountId":"2fa85f64-5717-4562-b3fc-2c963f66afa6", "value":2.0}

---
### Очистка пространства:

- `helm uninstall arch-billing`
- `kubectl delete namespace arch-gur`
