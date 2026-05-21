CREATE TABLE IF NOT EXISTS person (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS expense (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(200) NOT NULL,
    amount DOUBLE NOT NULL,
    paid_by INT NOT NULL,
    FOREIGN KEY (paid_by) REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS expense_participant (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expense_id INT NOT NULL,
    person_id INT NOT NULL,
    FOREIGN KEY (expense_id) REFERENCES expense(id),
    FOREIGN KEY (person_id) REFERENCES person(id)
);