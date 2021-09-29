# Issuer Banking

This project contains RESTful APIs which handle request messages from POS devices.

Endpoints:
- Make Payment Or Adjustment
- Get Balance

Transaction has one of two message types: Payment and Adjustment

## **Restrictions**

Payment cannot be performed if :

- Account does not exist
- Transaction id not null
- Payment amount is zero or negative
- Payment amount is greater than balance

Adjustment cannot be performed if :

- Account does not exist
- Transaction id null
- Transaction id does not match with the one in db
- Adjustment amount is zero
- Adjustment amount is greater than balance if it is positive
- Adjustment amount's absolute value is greater than old payment amount including sum of old adjustment amounts if it is negative

## **Libraries & Plugins used**

- Spring Boot
- Hibernate Validator
- Lombok
- MapStruct
- Spring Boot Test
- Swagger

## **Database**

This service uses H2 database. When service starts, a few sample account data in data.sql file will be inserted into db.

## **RESTful Api**

## ***Account***

| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /v1/accounts/:accountId | `GET` | Empty | Get balance. |

## **Transaction**

| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /v1/transactions/ | `POST` | { "transactionId": "", "messageType": "PAYMENT", "accountId": 4755, "origin": "VISA", "amount": 100 } | Create a new transaction. |

enjoy!