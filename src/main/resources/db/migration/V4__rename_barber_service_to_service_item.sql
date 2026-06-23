ALTER TABLE appointments
DROP FOREIGN KEY appointments_ibfk_3;

RENAME TABLE barber_services TO service_items;

ALTER TABLE appointments
RENAME COLUMN barber_service_id TO service_item_id;

ALTER TABLE appointments
ADD CONSTRAINT fk_appointments_service_item
FOREIGN KEY (service_item_id) REFERENCES service_items(id);