-- Initialize FX Deals Database for MySQL

-- Grant all privileges to the user
GRANT ALL PRIVILEGES ON fxdeals.* TO 'fxdeals_user'@'%';
FLUSH PRIVILEGES;

-- The fx_deals table will be created automatically by Hibernate
