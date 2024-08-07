-- This file contains the SQL statements to populate the database with some initial data.

-- If you want to use UUID instead of SERIAL for the id column, you can use the following SQL statements.
-- Install uuid-ossp extension and update the id column to use the uuid_generate_v4() function.
-- Execute it if your not using a remote database that already has the extension installed.
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP EXTENSION IF EXISTS "uuid-ossp";
ALTER TABLE category ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE supplier ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE product ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE product ALTER COLUMN category_id SET DEFAULT uuid_generate_v4();
ALTER TABLE product ALTER COLUMN supplier_id SET DEFAULT uuid_generate_v4();

-- @GeneratedValue(strategy = GenerationType.
-- UUID: generated automatically if using above statements
-- SEQUENCE: needs to be manually set
-- IDENTIY (SERIAL): generated automatically

-- V1
-- category (id, description)
INSERT INTO category VALUES (1, 'Video Games');
INSERT INTO category VALUES (2, 'Movies');
INSERT INTO category VALUES (3, 'Books');

-- supplier (id, name)
INSERT INTO supplier VALUES (1, 'Game Supplier Co.');
INSERT INTO supplier VALUES (2, 'Movie Supplier Inc.');
INSERT INTO supplier VALUES (3, 'Book Supplier Ltd.');

-- product (id, category_id, name, quantity_available, supplier_id)
INSERT INTO product VALUES (1, 1, 'The Legend of Zelda: Breath of the Wild', 15, 1);
INSERT INTO product VALUES (2, 1, 'Super Mario Odyssey', 20, 1);
INSERT INTO product VALUES (3, 2, 'Inception', 10, 2);
INSERT INTO product VALUES (4, 2, 'The Dark Knight', 8, 2);
INSERT INTO product VALUES (5, 3, '1984 by George Orwell', 25, 3);
INSERT INTO product VALUES (6, 3, 'To Kill a Mockingbird by Harper Lee', 30, 3);

-- V2
INSERT INTO category VALUES (1000, 'Comic Books');
INSERT INTO category VALUES (1001, 'Movies');
INSERT INTO category VALUES (1002, 'Books');

INSERT INTO supplier VALUES (1000, 'Comic Supplier Co.');
INSERT INTO supplier VALUES (1001, 'Movie Supplier Inc.');
INSERT INTO supplier VALUES (1002, 'Book Supplier Ltd.');

INSERT INTO product VALUES (1000, 1000 ,'The Amazing Spider Man', 15, 1000);
INSERT INTO product VALUES (1001, 1000, 'The Avengers', 20, 1000);
INSERT INTO product VALUES (1002, 1001, 'Inception', 10, 1001);
INSERT INTO product VALUES (1003, 1001, 'The Dark Knight', 8, 1001);
INSERT INTO product VALUES (1004, 1002, '1984 by George Orwell', 25, 1002);
INSERT INTO product VALUES (1005, 1002, 'To Kill a Mockingbird by Harper Lee', 30, 1002);

-- Clean up
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS supplier CASCADE;
DROP SEQUENCE IF EXISTS category_seq;
DROP SEQUENCE IF EXISTS supplier_seq;
DROP SEQUENCE IF EXISTS product_seq;

-- Drop all lines from all tables
TRUNCATE TABLE product CASCADE;
TRUNCATE TABLE category CASCADE;
TRUNCATE TABLE supplier CASCADE;


