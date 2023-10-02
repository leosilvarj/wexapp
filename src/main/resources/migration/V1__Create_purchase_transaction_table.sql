CREATE TABLE purchase_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    description VARCHAR(50) NOT NULL,
    transaction_date DATE NOT NULL,
    purchase_amount DECIMAL(10, 2) NOT NULL
);
