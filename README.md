# banking-api-for-interview
Coding case



A small REST API for basic banking operations as:

- Create accounts
- Deposit money
- Transfer money between accounts
- Get account balance
- Convert DKK to USD (using the ExchangeRate-API)

## Quick Start

### 1) Requirements
- Java 25

### 2) Configure API key
In `src/main/resources/application.properties`, set:

```properties
exchange.rate.api.key=YUOR_REAL_API_KEY_HERE
```

### 3) Run the app
Windows:

```powershell
mvnw.cmd quarkus:dev
```

Mac/Linux:

```bash
./mvnw quarkus:dev
```


## API Endpoints (cURL + Invoke-RestMethod)

Base URL:

```text
http://localhost:8080
```

### 1) Create Account
`POST /accounts`

```bash
curl -X POST http://localhost:8080/accounts \
	-H "Content-Type: application/json" \
	-d '{"accountNumber":"Account1","firstName":"Peter","lastName":"Petersen","initialBalance":500}'
```

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/accounts" -ContentType "application/json" -Body '{"accountNumber":"Account1","firstName":"Peter","lastName":"Petersen","initialBalance":500}'
```

### 2) Deposit
`POST /accounts/{accountNumber}/deposit`

```bash
curl -X POST http://localhost:8080/accounts/Account1/deposit \
	-H "Content-Type: application/json" \
	-d '{"amount":250.00}'
```

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/accounts/Account1/deposit" -ContentType "application/json" -Body '{"amount":250.00}'
```

### 3) Transfer
`POST /accounts/transfer`

```bash
curl -X POST http://localhost:8080/accounts/transfer \
	-H "Content-Type: application/json" \
	-d '{"fromAccountNumber":"Account1","toAccountNumber":"Account2","amount":100.00}'
```

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/accounts/transfer" -ContentType "application/json" -Body '{"fromAccountNumber":"Account1","toAccountNumber":"Account2","amount":100.00}'
```

### 4) Get Balance
`GET /accounts/{accountNumber}/balance`

```bash
curl http://localhost:8080/accounts/Account1/balance
```

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/accounts/Account1/balance"
```

### 5) Convert DKK to USD
`GET /accounts/exchange-rate/{dkkAmount}`

```bash
curl http://localhost:8080/accounts/exchange-rate/100
```

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/accounts/exchange-rate/100"
```

## Typical Error Response

```json
{
	"message": "error details",
	"status": 400
}
```

## Notes
- i use H2 in-memory database as requsted and the data resets on restart.
- Account currency is DKK.
- that Exchange-rate endpoint requires a valid API key.

## Run Tests

Windows:

```powershell
mvnw.cmd test
```

Mac/Linux:

```bash
./mvnw test
```




