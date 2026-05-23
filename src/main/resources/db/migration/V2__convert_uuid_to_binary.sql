ALTER TABLE appointments DROP FOREIGN KEY appointments_ibfk_1;
ALTER TABLE appointments DROP FOREIGN KEY appointments_ibfk_2;
ALTER TABLE appointments DROP FOREIGN KEY appointments_ibfk_3;

ALTER TABLE clients MODIFY id BINARY(16) NOT NULL;
ALTER TABLE barber_services MODIFY id BINARY(16) NOT NULL;
ALTER TABLE employees MODIFY id BINARY(16) NOT NULL;

ALTER TABLE appointments MODIFY id BINARY(16) NOT NULL;
ALTER TABLE appointments MODIFY client_id BINARY(16) NOT NULL;
ALTER TABLE appointments MODIFY employee_id BINARY(16) NOT NULL;
ALTER TABLE appointments MODIFY barber_service_id BINARY(16) NOT NULL;

ALTER TABLE appointments ADD CONSTRAINT appointments_ibfk_1 FOREIGN KEY (client_id) REFERENCES clients(id);
ALTER TABLE appointments ADD CONSTRAINT appointments_ibfk_2 FOREIGN KEY (employee_id) REFERENCES employees(id);
ALTER TABLE appointments ADD CONSTRAINT appointments_ibfk_3 FOREIGN KEY (barber_service_id) REFERENCES barber_services(id);
