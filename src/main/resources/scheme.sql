CREATE TABLE IF NOT EXISTS user_account (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

TRUNCATE TABLE user_account;


CREATE TABLE IF NOT EXISTS currency (
    id BIGINT PRIMARY KEY,
    code BIGINT,
    ccy VARCHAR(100),
    ccy_nm_uz VARCHAR(100),
    rate DOUBLE PRECISION,

    date DATE
);

TRUNCATE TABLE currency;
