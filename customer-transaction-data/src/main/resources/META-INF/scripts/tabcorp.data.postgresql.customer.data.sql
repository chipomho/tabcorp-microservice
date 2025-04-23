INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Tony', 'Stark', 'tony.stark@gmail.com', 'Australia') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Bruce', 'Banner', 'bruce.banner@gmail.com', 'US') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Steve', 'Rogers', 'steve.rogers@gmail.com', 'Australia') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Wanda', 'Maximoff', 'wanda.maximoff@gmail.com', 'US') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;
INSERT INTO customer (first_name, last_name, email_address, location) VALUES ( 'Natasha', 'Romanoff', 'natasha.romanoff@gmail.com', 'Canada') ON CONFLICT (EMAIL_ADDRESS) DO NOTHING;


INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_001', 50.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_002', 100.00, 'INACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_003', 200.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_004', 10.00, 'INACTIVE') ON CONFLICT (CODE) DO NOTHING;
INSERT INTO product (code, product_cost, product_status) VALUES ('PRODUCT_005', 500.00, 'ACTIVE') ON CONFLICT (CODE) DO NOTHING;


