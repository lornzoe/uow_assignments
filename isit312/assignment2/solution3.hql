DROP TABLE IF EXISTS employees;

-- 1. Creates an internal relational table to store information about the employees,
-- the projects they are assigned to (project name and percentage of involvement)
-- and their programming skills.

CREATE TABLE IF NOT EXISTS employees (
    emp_number STRING,
    full_name STRING,
    projects MAP<STRING, INT>,
    skills ARRAY<STRING>
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
COLLECTION ITEMS TERMINATED BY ','
MAP KEYS TERMINATED BY ':'
STORED AS TEXTFILE;

-- 2. Include into the script INSERT statements that load sample data into the table. (at least 5)

-- Condition 1: Two employees must participate in few projects and must know few programming languages
INSERT INTO employees SELECT '1', 'Employee 1', map('Project1', 30, 'Project2', 25, 'Project3', 100), array('Java', 'C', 'C++');
INSERT INTO employees SELECT '2', 'Employee 2', map('Project4', 50, 'Project5', 50), array('Python', 'Rust');

-- Condition 2: One employee must participate in few projects and must not know any programming languages
INSERT INTO employees SELECT '3', 'Employee 3', map('Project1', 70, 'Project2', 75), NULL;

-- Condition 3: One employee must know few programming languages and must not participate in any projects.
INSERT INTO employees SELECT '4', 'Employee 4', NULL, array('C', 'Rust');

-- Condition 4: One employee must not know programming languages and must not participate in the projects.
INSERT INTO employees SELECT '5', 'Employee 5', NULL, NULL;

-- 3. Include into the script SELECT statements that list the contents of the table.
SELECT * FROM employees;