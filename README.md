# FX Deals Warehouse (no Docker)

🧾 Project Overview

Spring Boot service that imports FX deal rows from CSV, validates structure and saves each valid row immediately into PostgreSQL.
Designed for SDET & QA evaluation, including unit, integration, and load tests.


## Requirements
- Java 17+
- Maven
- PostgreSQL (for dev profile) 

## Folder/File Purpose

src/main/java – main Spring Boot application code

src/test/java – all unit and integration tests

src/test/resources – sample CSV/JSON files for tests

k6-scripts – K6 performance testing scripts

README.md – instructions, API info, and screenshots


## Run with PostgreSQL 
1. Install Postgres locally (Ubuntu):
   - `sudo apt-get install postgresql postgresql-contrib`
   - create DB and user:
     ```sql
     CREATE DATABASE fx_deals;
     CREATE USER fx_user WITH ENCRYPTED PASSWORD 'fx_pass';
     GRANT ALL PRIVILEGES ON DATABASE fx_deals TO fx_user;
     ```
2. Start app with:
   - `SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run`

## API
`POST /api/deals/import`

- Request (Multipart CSV file)

Example CSV format:
dealUniqueId,orderingCurrency,counterCurrency,amount,dealTimestamp
D1,USD,EUR,1000.50,2025-11-28T12:00:00Z
D2,GBP,JPY,500.00,2025-11-29T09:30:00Z

- Returns array of RowResult objects:

[
  { "dealUniqueId": "D1", "success": true, "errors": null },
  { "dealUniqueId": "D2", "success": false, "errors": ["invalid timestamp"] }
]

## Status codes
200 OK  -->  All rows imported successfully
207     -->  Partial success (some rows failed, some saved)
400	  -->  All rows failed validation
500	  -->  Unexpected server error

## Tests 
- Run `mvn test` to execute Unit & Integration tests.
- Run `k6 run load-test.js` to execute K6 test.

## Results
- Unit & Integration tests
  
<img width="945" height="502" alt="image" src="https://github.com/user-attachments/assets/0d50ac13-7090-45b9-879e-5ac14f0e1dea" />
<img width="945" height="503" alt="image" src="https://github.com/user-attachments/assets/0210f6ce-b27a-407f-aa54-47f4d369714b" />

- API tests
  
Deals_success:
<img width="945" height="503" alt="image" src="https://github.com/user-attachments/assets/1bfef8d2-109c-4300-9d54-66d37fa7d34e" />
Deals_partial:
<img width="945" height="501" alt="image" src="https://github.com/user-attachments/assets/657f5aff-535a-4c19-b013-9e6cff8af462" />
Deals_invalid:
<img width="945" height="501" alt="image" src="https://github.com/user-attachments/assets/cab822e7-5f47-45da-83c5-5b6fe8208d8d" />

- K6 load test
  
<img width="945" height="501" alt="image" src="https://github.com/user-attachments/assets/cd7ac114-0b39-4dfd-9478-103ed4c98e6a" />
<img width="1920" height="1018" alt="image" src="https://github.com/user-attachments/assets/45150d70-c2c9-4277-a838-56f517bbef3b" />

## 👩‍💻 Maintainer
Lamiae Announ — Software Engineer (QA/Testing focus)

