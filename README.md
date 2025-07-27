# acme-solicitacao-seguros





``` bash
docker compose up -d
```

``` bash
docker compose down
```

``` bash
sudo rm -Rf ./postgresql-data
```






docker exec -it rabbitmq sh -c "rabbitmqctl"


PUT /api/exchanges/{vhost}/{name}/publish


### Columns' prefixes
| Purpose                  | Prefix | Datatype        |
|--------------------------|--------|-----------------|
| Identification           | id     | INTEGER, BIGINT |
| Alphanumeric code        | cd     | VARCHAR, CHAR   |
| Date                     | dt     | DATE            |
| Date and time            | ts     | TIMESTAMP       |
| Decimal value            | vl     | DECIMAL         |
| Integer number           | nb     | INTEGER, BIGINT |
| Character for flags      | ch     | CHAR            |
| Alphanumeric name        | nm     | VARCHAR         |
| Alphanumeric description | ds     | VARCHAR         |
| Long text                | tx     | TEXT            |