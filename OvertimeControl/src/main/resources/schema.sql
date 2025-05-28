CREATE TABLE IF NOT EXISTS department
(
   id SERIAL PRIMARY KEY,
   department_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS m_user
(
   id SERIAL PRIMARY KEY,
   email VARCHAR(255) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   family_name VARCHAR(50) NOT NULL,
   first_name VARCHAR(50) NOT NULL,
   role INT NOT NULL,
   department_id INT NOT NULL,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE IF NOT EXISTS m_overtime
(
   id SERIAL PRIMARY KEY,
   department_id INT NOT NULL,
   user_id INT NOT NULL,
   pattern INT,
   request_start_time TIMESTAMP,
   request_finish_time TIMESTAMP,
   reason TEXT,
   approver_id INT,
   approval_time TIMESTAMP,
   reject_reason TEXT,
   report_start_time TIMESTAMP,
   report_finish_time TIMESTAMP,
   breaktime INT,
   report TEXT,
   submit_time TIMESTAMP,
   status INT,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   CONSTRAINT fk_department_ot FOREIGN KEY (department_id) REFERENCES department(id),
   CONSTRAINT fk_user_ot FOREIGN KEY (user_id) REFERENCES m_user(id),
   CONSTRAINT fk_approver_ot FOREIGN KEY (approver_id) REFERENCES m_user(id)
);
