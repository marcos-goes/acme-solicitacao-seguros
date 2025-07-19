
https://www.baeldung.com/java-openapi-generator-server

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
