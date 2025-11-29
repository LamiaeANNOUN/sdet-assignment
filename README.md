# FX Deals Warehouse (no Docker)

## Requirements
- Java 17+
- Maven
- PostgreSQL (for dev profile) OR use test profile (H2) for local testing

## Run locally using H2 (quick start)
1. `mvn clean package`
2. `mvn spring-boot:run` (uses default `application.yml` with H2)

## Run with PostgreSQL (assignment requirement: actual DB)
1. Install Postgres locally (Ubuntu example):
   - `sudo apt-get install postgresql postgresql-contrib`
   - create DB and user:
     ```sql
     CREATE DATABASE fx_deals;
     CREATE USER fx_user WITH ENCRYPTED PASSWORD 'fx_pass';
     GRANT ALL PRIVILEGES ON DATABASE fx_deals TO fx_user;
     ```
2. Update `src/main/resources/application-dev.yml` if your credentials differ.
3. Start app with:
   - `make run-dev`  (or `SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run`)

## API
`POST /api/deals/import`
- Accepts JSON array of deals:
```json
[
  {
    "dealUniqueId": "D1",
    "orderingCurrency": "USD",
    "counterCurrency": "EUR",
    "dealTimestamp": "2025-11-28T12:00:00Z",
    "amount": 1000.0
  }
]
```
- Returns an array of `RowResult` objects with success/errors. Partial success allowed; each accepted row is saved immediately.

## Tests & Coverage
- Run `make test` to execute unit tests.
- Run `make coverage` to run `verify` including JaCoCo check (note: you may need to exclude non-production code; update README if you do).
