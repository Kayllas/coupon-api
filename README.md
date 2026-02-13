# Coupon API

API simples para cadastro e consulta de cupons, seguindo regras de negócio (normalização do `code`, validação de `discountValue`, `expirationDate`, soft delete, etc.).

## Stack
- Java 17
- Spring Boot 4.0.2
- H2 (in-memory)
- Spring Data JPA
- Swagger/OpenAPI (springdoc)
- JUnit 5 + JaCoCo (>= 80%)

## Pré-requisitos
- Java 17+
- Maven 3.9+

> Observação: este projeto pode conter um `.mvn/settings.xml` para evitar depender do `~/.m2/settings.xml` da máquina.

## Rodar localmente
```bash
mvn test
mvn spring-boot:run
```

Aplicação: http://localhost:8080

## Swagger UI
- http://localhost:8080/swagger-ui/index.html

## H2 Console
- http://localhost:8080/h2-console  
  JDBC URL: `jdbc:h2:mem:coupondb`  
  User: `sa`  
  Password: (empty)

## Endpoints
- **POST** `/coupon`  
- **GET** `/coupon/{id}`  
- **DELETE** `/coupon/{id}` *(soft delete; não permite deletar duas vezes)*

## Exemplos (cURL)

### Criar cupom
```bash
curl -i -X POST "http://localhost:8080/coupon" \
  -H "Content-Type: application/json" \
  -d '{
    "code": "A@1-2#B$3C",
    "description": "Promo test",
    "discountValue": 10.5,
    "expirationDate": "2026-12-31T23:59:59Z",
    "published": true
  }'
```

### Buscar cupom por id
```bash
curl -i "http://localhost:8080/coupon/{id}"
```

### Deletar cupom (soft delete)
```bash
curl -i -X DELETE "http://localhost:8080/coupon/{id}"
```

## Regras de negócio (resumo)
- `code`:
  - aceita caracteres especiais na entrada
  - a aplicação remove caracteres não alfanuméricos antes de salvar/retornar
  - após normalização deve ter **exatamente 6** caracteres
- `discountValue`:
  - mínimo **0,5**
- `expirationDate`:
  - não pode ser no passado
  - formato recomendado: ISO-8601 `date-time` (ex.: `2026-12-31T23:59:59Z`)
- `published`:
  - opcional (default `false`)
- `redeemed`:
  - sempre inicia `false`
- `delete`:
  - soft delete via `status=DELETED`
  - não permite deletar um cupom já `DELETED`

## Testes e cobertura
```bash
mvn verify
```
- Gera relatório do JaCoCo em: `target/site/jacoco/index.html`
- Falha o build se a cobertura mínima não for atingida (>= 80%)

## Docker
```bash
docker-compose up --build
```

Aplicação: http://localhost:8080
