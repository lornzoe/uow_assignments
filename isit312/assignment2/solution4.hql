-- Create External Table for PART
CREATE EXTERNAL TABLE IF NOT EXISTS part (
    P_PARTKEY STRING,     
    P_NAME STRING,        
    P_MFGR STRING,        
    P_BRAND STRING,       
    P_TYPE STRING,        
    P_SIZE INT,           
    P_CONTAINER STRING,   
    P_RETAILPRICE DOUBLE, 
    P_COMMENT STRING      
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE
LOCATION '/user/hive/warehouse/task4/part'
TBLPROPERTIES ("skip.header.line.count"="1");

-- Create External Table for SUPPLIER
CREATE EXTERNAL TABLE IF NOT EXISTS supplier (
    S_SUPPKEY STRING,
    S_NAME STRING,   
    S_ADDRESS STRING,
    S_PHONE STRING,  
    S_ACCTBAL DOUBLE,
    S_COMMENT STRING 
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE
LOCATION '/user/hive/warehouse/task4/supplier'
TBLPROPERTIES ("skip.header.line.count"="1");

-- Create External Table for PART-SHIPPED-BY (PARTSUPP)
CREATE EXTERNAL TABLE IF NOT EXISTS partsupp (
    PS_PARTKEY STRING,
    PS_SUPPKEY STRING,
    PS_AVAILQTY INT,     
    PS_SUPPLYCOST DOUBLE,
    PS_COMMENT STRING    
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
STORED AS TEXTFILE
LOCATION '/user/hive/warehouse/task4/partsupp'
TBLPROPERTIES ("skip.header.line.count"="1");

-- Output 5 rows from each table and the total count

SELECT * FROM part LIMIT 5;
SELECT COUNT(*) AS total_parts FROM part;

SELECT * FROM supplier LIMIT 5;
SELECT COUNT(*) AS total_suppliers FROM supplier;

SELECT * FROM partsupp LIMIT 5;
SELECT COUNT(*) AS total_partsupp FROM partsupp;