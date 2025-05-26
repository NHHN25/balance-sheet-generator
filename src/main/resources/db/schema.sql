CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL
);

CREATE TABLE transaction_entry (
    id SERIAL PRIMARY KEY ,
    account_id INT NOT NULL REFERENCES account(id),
    amount NUMERIC(15,2) NOT NULL,
    entry_date DATE NOT NULL,
    description VARCHAR(255)
);