-- Hospital database schema
CREATE TABLE patient (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    email VARCHAR(100)
);

CREATE TABLE doctor (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialty VARCHAR(100)
);
