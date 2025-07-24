# acme-solicitacao-seguros





``` bash
docker compose up -d
```

``` bash
docker compose down
```

``` bash
sudo rm -Rf ~/volumes/insurance-order-volume
```


``` bash
docker run -d \
--name insurance-order-db \
-e POSTGRES_USER=dbo \
-e POSTGRES_PASSWORD=abc@123 \
-e POSTGRES_DB=insurance-order \
-e PGDATA=/var/lib/postgresql/data/pgdata \
-v ~/volumes/insurance-order-volume:/var/lib/postgresql/data \
-p 5432:5432 \
postgres:16.3
```



docker exec -it rabbitmq sh -c "rabbitmqctl"
