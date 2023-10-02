# Purchase Transaction System

This is a simple Purchase Transaction System that allows you to store purchase transactions with specific requirements and retrieve them in a specified country's currency using the Treasury Reporting Rates of Exchange API.

## Prerequisites

Before you begin, make sure you have the following installed on your system:"

- Java 17
- Spring Boot 3.1.4

## Requirements

### Requirement #1: Store a Purchase Transaction

Your application must be able to accept and store (i.e., persist) a purchase transaction with the following details:

- Description: The transaction description must not exceed 50 characters.
- Transaction Date: The transaction date must be in a valid date format.
- Purchase Amount: The purchase amount must be a valid positive amount rounded to the nearest cent.
- Unique Identifier: Each stored purchase transaction should be assigned a unique identifier.

### Requirement #2: Retrieve a Purchase Transaction in a Specified Country's Currency

Based on previously submitted and stored purchase transactions, your application must provide a way to retrieve the stored purchase transactions converted to currencies supported by the Treasury Reporting Rates of Exchange API. The conversion should be based on the exchange rate active for the date of the purchase.

You can access the Treasury Reporting Rates of Exchange API here: [Treasury Reporting Rates of Exchange API](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange)

The retrieved purchase transaction should include the following information:

- Identifier: The unique identifier of the purchase transaction.
- Description: The description of the purchase transaction.
- Transaction Date: The original transaction date.
- Original US Dollar Purchase Amount: The purchase amount in United States dollars.
- Exchange Rate Used: The exchange rate used for the currency conversion.
- Converted Amount: The converted purchase amount based on the specified currency's exchange rate for the date of the purchase.

### Currency Conversion Requirements

When converting between currencies, you must adhere to the following rules:

- Currency conversion does not require an exact date match, but you must use a currency conversion rate less than or equal to the purchase date from within the last 6 months.
- If no currency conversion rate is available within 6 months equal to or before the purchase date, an error should be returned, stating that the purchase cannot be converted to the target currency.
- The converted purchase amount to the target currency should be rounded to two decimal places (i.e., cent).

## Getting Started

Please follow the instructions provided to set up and run this Purchase Transaction System. Ensure that you meet the specified requirements to store and retrieve purchase transactions successfully.
