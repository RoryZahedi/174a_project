CREATE TABLE Customers ( 
	name CHAR(10),
	username CHAR(20),
	password CHAR(20),
	address CHAR(30),
    state CHAR(2),
	phone CHAR(10),
	email CHAR(20),
	taxid CHAR(4),
    ssn CHAR(9),
	UNIQUE (username),
    PRIMARY KEY (taxid)
);

CREATE TABLE Market_Account (
	marketid INTEGER PRIMARY KEY AUTOINCREMENT,
	taxid CHAR(4) NOT NULL,
	balance FLOAT,
	initial FLOAT,
	FOREIGN KEY (taxid) REFERENCES Customers
		ON DELETE NO ACTION 
);

CREATE TABLE Stock_Account ( 
	taxid CHAR(4) NOT NULL,
	shares FLOAT,
    stockid CHAR(3),
	buyprice FLOAT,
	PRIMARY KEY (taxid, stockid, buyprice),
    FOREIGN KEY (taxid) REFERENCES Customers
        ON DELETE NO ACTION,
    FOREIGN KEY (stockid) REFERENCES Actor_Director_Stock 
);

CREATE TABLE Actor_Director ( 
	stockid CHAR(3), 
	name CHAR(20),
	dob CHAR(20),
	PRIMARY KEY (stockid)
);

CREATE TABLE Actor_Director_Stock ( 
	stockid CHAR(3),
    current FLOAT,
    closing FLOAT,
	PRIMARY KEY(stockid),
    FOREIGN KEY(stockid) REFERENCES Actor_Director
        ON DELETE NO ACTION 
);

CREATE TABLE Movies ( 
	movieid CHAR(4),
    title CHAR(20),
	year INTEGER,
    genre CHAR(10),
	rating FLOAT,
    revenue INTEGER,
    PRIMARY KEY (movieid) 
);

CREATE TABLE Contracted ( 
	role CHAR(10),
    year CHAR(4),
    value INTEGER,
    stockid CHAR(3),
	title CHAR(20),
    PRIMARY KEY (stockid, title),
    FOREIGN KEY (stockid) REFERENCES Actor_Director
		ON DELETE NO ACTION,
    FOREIGN KEY (title) REFERENCES Movies
		ON DELETE NO ACTION 
);

CREATE TABLE Market_Transaction ( 
	transactionid INTEGER PRIMARY KEY AUTOINCREMENT,
	transtype CHAR(9),
	taxid CHAR(4),
	amount FLOAT,
	date CHAR(10),
	month INTEGER,
	FOREIGN KEY (taxid) REFERENCES Customers
		ON DELETE NO ACTION
);

CREATE TABLE Stock_Transaction ( 
	transactionid INTEGER PRIMARY KEY AUTOINCREMENT,
	transtype CHAR (4),
	taxid CHAR(4),
	stockid CHAR(3),
	shares FLOAT,
	buyprice FLOAT,
	sellprice FLOAT,
	date CHAR(10),
	month INTEGER,
	FOREIGN KEY (taxid, stockid) REFERENCES Stock_Account
		ON DELETE NO ACTION
);

CREATE TABLE Daily_Report (
  	taxid CHAR(4),
  	balance FLOAT,
	date CHAR(10),
	month INTEGER,
  	FOREIGN KEY (taxID) REFERENCES Customers 
		ON DELETE NO ACTION
);

CREATE TABLE Current_Date (
	date CHAR(10),
	time CHAR(5),
	month INTEGER
);

INSERT INTO Customers (name, username, password, address, state, phone, email, taxid, ssn)
VALUES
	('John Admin', 'admin', 'secret', 'Stock Company SB', 'CA', '8056374632', 'admin@stock.com', '1000', '606606060'),
	('Alfred Hitchcock', 'alfred', 'hi', '6667 El Colegio #40 SB', 'CA', '(805)2574499', 'alfred@hotmail.com', '1022', '606-76-7900'),
	('Billy Clinton', 'billy', 'cl', '5777 Hollister SB', 'CA', '(805)5629999', 'billy@yahoo.com', '3045', '606-76-7903'),
	('Cindy Laugher', 'cindy', 'la', '7000 Hollister SB', 'CA', '(805)6930011', 'cindy@hotmail.com', '2034', '606-70-7900'),
	('David Copperfill', 'david', 'co', '1357 State St SB', 'CA', '(805)8240011', 'david@yahoo.com', '4093', '506-78-7900'),
	('Elizabeth Sailor', 'sailor', 'sa', '4321 State St SB', 'CA', '(805)1234567', 'sailor@hotmail.com', '1234', '436-76-7900'),
	('George Brush', 'brush', 'br', '5346 Foothill Av', 'CA', '(805)1357999', 'george@hotmail.com', '8956', '632-45-7900'),
	('Ivan Stock', 'ivan', 'st', '1235 Johnson Dr', 'NJ', '(805)3223243', 'ivan@yahoo.com', '2341', '609-23-7900'),
	('Joe Pepsi', 'joe', 'pe', '3210 State St', 'CA', '(805)5668123', 'pepsi@pepsi.com', '0456', '646-76-3430'),
	('Magic Jordon', 'magic', 'jo', '3852 Court Rd', 'NJ', '(805)4535539', 'jordon@jordon.org', '3455', '646-76-8843'),
	('Olive Stoner', 'olive', 'st', '6689 El Colegio #151', 'CA', '(805)2574499', 'olive@yahoo.com', '1123', '645-34-7900'),
	('Frank Olson', 'frank', 'ol', '6910 Whittier Dr', 'CA', '(805)3456789', 'frank@gmail.com', '3306', '345-23-2134');

INSERT INTO Market_Account (marketid, taxid, balance, initial)
VALUES
	(1, '1022', 10000, 10000),
	(2, '3045', 100000, 100000),
	(3, '2034', 50000, 50000),
	(4, '4093', 45000, 45000),
	(5, '1234', 200000, 200000),
	(6, '8956', 5000, 5000),
	(7, '2341', 2000, 2000),
	(8, '0456', 10000, 10000),
	(9, '3455', 130200, 130200),
	(10, '1123', 35000, 35000),
	(11, '3306', 30500, 30500);

INSERT INTO Stock_Account (taxid, shares, stockid, buyprice)
VALUES
	('1022', 100, 'SKB', 40.00),
	('3045', 500, 'SMD', 71.00),
	('3045', 100, 'STC', 32.50),
	('2034', 250, 'STC', 32.50),
	('4093', 100, 'SKB', 40.00),
	('4093', 500, 'SMD', 71.00),
	('4093', 50, 'STC', 32.50),
	('1234', 1000, 'SMD', 71.00),
	('8956', 100, 'SKB', 40.00),
	('2341', 300, 'SMD', 71.00),
	('0456', 500, 'SKB', 40.00),
	('0456', 100, 'STC', 32.50),
	('0456', 200, 'SMD', 71.00),
	('3455', 1000, 'SKB', 40.00),
	('1123', 100, 'SKB', 40.00),
	('1123', 100, 'SMD', 71.00),
	('1123', 100, 'STC', 32.50),
	('3306', 100, 'SKB', 40.00),
	('3306', 200, 'STC', 32.50),
	('3306', 100, 'SMD', 71.00);

INSERT INTO Actor_Director (stockid, name, dob)
VALUES 
	('SKB', 'Kim Basinger', '8 December 1958'), 
	('SMD', 'Michael Douglas', '25 September 1944'), 
	('STC', 'Tom Cruise', '3 July 1962');

INSERT INTO Actor_Director_Stock (stockid, current, closing)
VALUES
	('SKB', 40.00, NULL),
	('SMD', 71.00, NULL),
	('STC', 32.50, NULL);

INSERT INTO MOVIES (movieid, title, year, genre, rating, revenue)
VALUES 
	('2606', 'L.A. Confidential', 1997, 'Action', 7.8, 842),
	('0421', 'A Perfect Murder', 1998, 'Thriller', 8.4, 1230),
	('7612', 'Jerry Maguire', 1996, 'Comedy', 6.3, 183);

INSERT INTO CONTRACTED (role, year, value, stockid, title)
VALUES
	('Actor', '1997', 5000000, 'SKB', 'L.A. Confidential'),
	('Actor', '1998', 10000000, 'SMD', 'A Perfect Murder'),
	('Actor', '1996', 5000000, 'STC', 'Jerry Maguire');


INSERT INTO Current_Date (date, time, month)
VALUES
	('03-16-2013', '00:00', 3);