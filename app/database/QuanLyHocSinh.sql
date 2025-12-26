CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    sid VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    status INTEGER NOT NULL,
    address TEXT,
    contact TEXT,
    image BYTEA,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subjects (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    credits INTEGER,
    status INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE registrations (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    semester VARCHAR(20) NOT NULL,
    academic_year INTEGER NOT NULL,
    status INTEGER NOT NULL DEFAULT 1,
    grade NUMERIC(5,2),
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id),
	UNIQUE (student_id, subject_id, semester, academic_year)
);

-- Add indexes for better query performance UWU
CREATE INDEX idx_registrations_student ON registrations(student_id);
CREATE INDEX idx_registrations_subject ON registrations(subject_id);
CREATE INDEX idx_registrations_academic_year ON registrations(academic_year);

-- Insert Students OWO
INSERT INTO students (sid, email, name, date_of_birth, status, address, contact) VALUES
('SID202501', 'john.doe@email.com', 'John Doe', '2000-03-15', 1, '123 Main St', '+1234567890'),
('SID202502', 'jane.smith@email.com', 'Jane Smith', '2001-07-22', 1, '456 Oak Ave', '+1234567891'),
('SID202503', 'bob.wilson@email.com', 'Bob Wilson', '2000-11-30', 1, '789 Pine Rd', '+1234567892'),
('SID202504', 'alice.jones@email.com', 'Alice Jones', '2001-04-05', 1, '321 Elm St', '+1234567893'),
('SID202505', 'charlie.brown@email.com', 'Charlie Brown', '2000-08-12', 1, '654 Maple Dr', '+1234567894'),
('SID202506', 'diana.prince@email.com', 'Diana Prince', '2001-01-18', 1, '987 Cedar Ln', '+1234567895'),
('SID202507', 'edward.stark@email.com', 'Edward Stark', '2000-06-25', 1, '147 Birch Rd', '+1234567896'),
('SID202508', 'fiona.green@email.com', 'Fiona Green', '2001-09-14', 1, '258 Willow Ave', '+1234567897'),
('SID202509', 'george.blue@email.com', 'George Blue', '2000-12-20', 1, '369 Ash St', '+1234567898'),
('SID202510', 'hannah.white@email.com', 'Hannah White', '2001-05-08', 1, '741 Pine Ct', '+1234567899'),
('SID202511', 'ian.black@email.com', 'Ian Black', '2000-02-28', 1, '852 Oak Ln', '+1234567900'),
('SID202512', 'julia.red@email.com', 'Julia Red', '2001-10-17', 1, '963 Maple Ave', '+1234567901'),
('SID202513', 'kevin.gray@email.com', 'Kevin Gray', '2000-07-09', 1, '159 Cedar St', '+1234567902'),
('SID202514', 'laura.pink@email.com', 'Laura Pink', '2001-03-26', 1, '357 Elm Rd', '+1234567903'),
('SID202515', 'mike.orange@email.com', 'Mike Orange', '2000-11-11', 1, '486 Birch Ave', '+1234567904');

-- Insert Subjects OWO
INSERT INTO subjects (name, description, credits, status) VALUES
('Mathematics', 'Fundamental mathematics including algebra and calculus', 3, 1),
('Physics', 'Basic principles of physics and mechanics', 4, 1),
('Chemistry', 'Introduction to chemical principles and reactions', 4, 1),
('Biology', 'Study of living organisms and life processes', 4, 1),
('Computer Science', 'Introduction to programming and algorithms', 3, 1),
('Literature', 'Analysis of classic and modern literature', 3, 1),
('History', 'World history and civilizations', 3, 1),
('Geography', 'Study of Earth and human geography', 3, 1),
('Art', 'Visual arts and creative expression', 2, 1),
('Music', 'Music theory and appreciation', 2, 1),
('Physical Education', 'Sports and physical fitness', 2, 1),
('Economics', 'Principles of micro and macroeconomics', 3, 1),
('Psychology', 'Introduction to human behavior and mind', 3, 1),
('Foreign Language', 'Second language acquisition', 4, 1),
('Environmental Science', 'Study of environment and ecosystems', 3, 1);

-- Insert Registrations (Random distribution) OWO
INSERT INTO registrations (student_id, subject_id, semester, academic_year, status, grade) VALUES

(1, 1, 'Fall', 2024, 2, 85.5),
(1, 3, 'Fall', 2024, 1, 92.0),
(1, 5, 'Fall', 2024, 1, 88.5),
(1, 7, 'Fall', 2024, 1, 91.0),
(2, 2, 'Fall', 2024, 1, 78.5),
(2, 4, 'Fall', 2024, 1, 88.0),
(2, 6, 'Fall', 2024, 1, 95.5),
(2, 8, 'Fall', 2024, 1, 82.0),
(2, 10, 'Fall', 2024, 1, 90.0),
(2, 12, 'Fall', 2024, 1, 87.5),
(3, 1, 'Fall', 2024, 1, 91.5),
(3, 4, 'Fall', 2024, 1, 85.0),
(3, 7, 'Fall', 2024, 1, 88.0),
(4, 3, 'Fall', 2024, 1, 79.5),
(4, 6, 'Fall', 2024, 1, 94.0),
(4, 9, 'Fall', 2024, 1, 88.5),
(4, 12, 'Fall', 2024, 1, 91.0),
(5, 2, 'Fall', 2024, 1, 86.5),
(5, 5, 'Fall', 2024, 1, 92.0),
(5, 8, 'Fall', 2024, 1, 89.5),
(6, 1, 'Fall', 2024, 1, 93.0),
(6, 4, 'Fall', 2024, 1, 87.5),
(6, 7, 'Fall', 2024, 1, 90.0),
(7, 3, 'Fall', 2024, 1, 84.5),
(7, 6, 'Fall', 2024, 1, 89.0),
(7, 9, 'Fall', 2024, 1, 92.5),
(8, 2, 'Fall', 2024, 1, 88.0),
(8, 5, 'Fall', 2024, 1, 91.5),
(8, 8, 'Fall', 2024, 1, 86.0),
(9, 1, 'Fall', 2024, 1, 90.5),
(9, 4, 'Fall', 2024, 1, 85.0),
(9, 7, 'Fall', 2024, 1, 93.0),
(10, 3, 'Fall', 2024, 1, 87.0),
(10, 6, 'Fall', 2024, 1, 92.5),
(10, 9, 'Fall', 2024, 1, 89.0),
(11, 1, 'Fall', 2024, 1, 86.0),
(11, 5, 'Fall', 2024, 1, 91.0),
(12, 2, 'Fall', 2024, 1, 88.5),
(12, 6, 'Fall', 2024, 1, 93.5),
(12, 10, 'Fall', 2024, 1, 87.0),
(13, 3, 'Fall', 2024, 1, 92.0),
(13, 7, 'Fall', 2024, 1, 85.5),
(13, 11, 'Fall', 2024, 1, 90.0),
(14, 4, 'Fall', 2024, 1, 89.5),
(14, 8, 'Fall', 2024, 1, 94.0),
(15, 5, 'Fall', 2024, 1, 86.5),
(15, 9, 'Fall', 2024, 1, 91.5),
(15, 13, 'Fall', 2024, 1, 88.0);