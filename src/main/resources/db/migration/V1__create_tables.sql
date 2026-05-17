CREATE TABLE clients (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE barber_services (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(200),
    price DECIMAL(6, 2) NOT NULL,
    duration_minutes INT NOT NULL,
    active BOOLEAN NOT NULL
);

CREATE TABLE employees (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL
);

CREATE TABLE appointments (
    id VARCHAR(36) PRIMARY KEY,
    client_id VARCHAR(36) NOT NULL,
    employee_id VARCHAR(36) NOT NULL,
    barber_service_id VARCHAR(36) NOT NULL,
    appointment_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,

    FOREIGN KEY (client_id) REFERENCES clients(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (barber_service_id) REFERENCES barber_services(id)
);