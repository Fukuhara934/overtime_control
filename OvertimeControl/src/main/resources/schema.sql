CREATE TABLE IF NOT EXISTS department
(
   id INT AUTO_INCREMENT PRIMARY KEY,
   department_name VARCHAR (50) NOT NULL
);
CREATE TABLE IF NOT EXISTS m_user
(
   id INT AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR (255) UNIQUE NOT NULL,
   password VARCHAR (255) NOT NULL,
   family_name VARCHAR (50) NOT NULL,
   first_name VARCHAR (50) NOT NULL,
   role INT NOT NULL,
   department_id INT NOT NULL,
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (department_id) REFERENCES department (id)
);
CREATE TABLE IF NOT EXISTS m_overtime
(
   id INT AUTO_INCREMENT PRIMARY KEY,
   department_id INT NOT NULL,
   user_id INT NOT NULL,
   pattern INT,
   request_start_time DATETIME,
   request_finish_time DATETIME,
   reason TEXT,
   approver_id INT,
   approval_time DATETIME,
   reject_reason TEXT,
   report_start_time DATETIME,
   report_finish_time DATETIME,
   breaktime INT,
   report TEXT,
   submit_time DATETIME,
   status INT,
   created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   FOREIGN KEY (department_id) REFERENCES department (id),
   FOREIGN KEY (user_id) REFERENCES m_user (id),
   FOREIGN KEY (approver_id) REFERENCES m_user (id)
); 