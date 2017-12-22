INSERT INTO tariffs (name, is_active) VALUE ('Tariff 1', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 2', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 3', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 4', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 5', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 6', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 7', TRUE);
INSERT INTO tariffs (name, is_active) VALUE ('Tariff 8', TRUE);

INSERT INTO users (full_name, tariff_id) VALUE ('Patrick Jane', 1);
INSERT INTO users (full_name, tariff_id) VALUE ('Peter Falk', 2);
INSERT INTO users (full_name, tariff_id) VALUE ('Jessica Alba', 4);
INSERT INTO users (full_name, tariff_id) VALUE ('Frank Sinatra', 3);
INSERT INTO users (full_name, tariff_id) VALUE ('LeBron James', 1);
INSERT INTO users (full_name, tariff_id) VALUE ('James Bond', 8);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-08-15', 1);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-08-15', 1);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-08-15', 2);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-10-24', 2);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-11-12', 3);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-10-22', 3);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-10-10', 4);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
    VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-11-01', 4);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-11-12', 5);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-08-15', 5);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-11-11', 6);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-10-24', 6);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-11-12', 7);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-08-23', 7);

INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('Voice call', 1.20, 'RUB', 6.43, 'RUB', '2017-10-24', 8);
INSERT INTO options (name, old_amount, old_currency, new_amount, new_currency, date_of_change, tariff_id)
  VALUE ('SMS', 2.00, 'RUB', 6.43, 'RUB', '2017-08-16', 8);