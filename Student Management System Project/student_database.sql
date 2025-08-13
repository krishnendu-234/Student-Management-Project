-- Create the database if it doesn't exist
CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- Drop the table if it already exists
DROP TABLE IF EXISTS student;

-- Create the student table
CREATE TABLE student (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    roll_number INT UNIQUE,
    grade DOUBLE,
    class VARCHAR(50),
    gender VARCHAR(10),
    email VARCHAR(100),
    phone_number VARCHAR(15)
);

-- View table structure
DESC student;

-- View data (currently empty)
SELECT * FROM student;
