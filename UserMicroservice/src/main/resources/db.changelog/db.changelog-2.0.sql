--liquibase formatted sql


--changeset Gleb:2

INSERT INTO clients.customer (first_name, second_name, email, phone_number, address, registration_time, password)
VALUES ('John', 'Doe', 'john.doe@example.com', '+1234567890', '123 Main St, Springfield', now(),1234567),
       ('Jane', 'Smith', 'jane.smith@example.com', '+1234567891', '456 Elm St, Metropolis', now(),1234567),
       ('Michael', 'Johnson', 'michael.johnson@example.com', '+1234567892', '789 Oak St, Gotham', now(),1234567),
       ('Emily', 'Davis', 'emily.davis@example.com', '+1234567893', '321 Maple St, Star City', now(),1234567),
       ('William', 'Brown', 'william.brown@example.com', '+1234567894', '654 Pine St, Central City', now(),1234567)
ON CONFLICT (email) DO NOTHING;