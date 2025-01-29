CREATE TABLE user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  grant_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (role_id) REFERENCES role (role_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
);