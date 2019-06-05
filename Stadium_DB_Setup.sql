SET FOREIGN_KEY_CHECKS = 1;
DROP TABLE BookKeeping1

Create table BookKeeping1 (
Edate Date primary key,
Income REAL,
Expense REAL,
Attendence int,
NetIncome REAL
);

Insert into BookKeeping1 Values('2019-05-01',20000,1000,25, 19000);
Insert into BookKeeping1 Values('2019-05-02', 50000,8800,32, 41200);
Insert into BookKeeping1 Values('2019-05-03',60000.5, 1000,40, 59000.5);
Insert into BookKeeping1 Values('2019-05-04',50000, 4000, 30, 46000);
Insert into BookKeeping1 Values('2019-05-05',90000, 10000, 40, 80000);

Create table Stadium_Events(
Event_id char(20) primary key,
StartTime TIME,
EndTime TIME,
TicketSold int,
Edate Date,
Foreign key(Edate) references BookKeeping1(Edate) ON DELETE SET NULL );

Insert into Stadium_Events Values('aaa', '08:00:00', '09:00:00', 50, '2019-05-01');
Insert into Stadium_Events Values('bbb', '10:00:00', '11:00:00', 45, '2019-05-02');
Insert into Stadium_Events Values('ccc', '12:00:00', '13:00:00', 50, '2019-05-03');
Insert into Stadium_Events Values('ddd', '15:00:00', '16:00:00', 33, '2019-05-04');
Insert into Stadium_Events Values('eee', '16:00:00', '17:00:00', 50, '2019-05-05');

Create table Concerts(
Event_id char(20) primary key,
Performer char(20),
Capacity int,
Foreign key(Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE);

Insert into Concerts Values('aaa', 'Cathy',60 );
Insert into Concerts Values('bbb', 'Evan',61);
Insert into Concerts Values('ccc', 'Linda',62);

Create table Games(
Event_id char(20) primary key,
HomeTeam char(20),
AwayTeam char(20),
Foreign key(Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE);

Insert into Games Values('ddd', 'teamA', 'teamS' );
Insert into Games Values('eee', 'teamB', 'teamK');



Create table Team(
HomeTeam char(20) primary key,
SportsType char(20));

Insert into Team Values('teamA', 'Hockey');
Insert into Team Values('teamB', 'Tennis');
Insert into Team Values('teamC', 'PingPong');
Insert into Team Values('teamD', 'Soccer');
Insert into Team Values('teamE', 'Rugby');

Create table Sponsorship1(
	CompanyName char(30) PRIMARY KEY,
	Donations REAL
);

Insert into Sponsorship1 Values('BMO', 10000);
Insert into Sponsorship1 Values('Google', 8880);
Insert into Sponsorship1 Values('Facebook', 9999);
Insert into Sponsorship1 Values('Amazon', 7777);
Insert into Sponsorship1 Values('Apple', 6780);

Create table Sponsors(
CompanyName char(30),
Event_id char(20),
SizeofAd int,
Foreign key (CompanyName) references Sponsorship1(CompanyName) ON DELETE CASCADE,
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
primary key(CompanyName, Event_id)
);

Insert into sponsors Values('BMO', 'aaa', 3);
Insert into sponsors Values('Google', 'bbb', 3);
Insert into sponsors Values('Facebook', 'ccc', 3);
Insert into sponsors Values('Amazon', 'ddd', 3);
Insert into sponsors Values('Apple', 'eee', 3);


Create table Sponsorship2(
	Donations int PRIMARY KEY,
	NumOfEvents int NOT NULL
);
Insert into Sponsorship2 Values(10000, 1);
Insert into Sponsorship2 Values(8880, 1);
Insert into Sponsorship2 Values(9999, 1);
Insert into Sponsorship2 Values(7777, 1);
Insert into Sponsorship2 Values(6780, 1);



Create table Occupations(
Occupation char(20) primary key,
HourlyWage REAL);

Insert into Occupations Values('receptionist', 18);
Insert into Occupations Values('manager', 25);
Insert into Occupations Values('janitor', 15);
Insert into Occupations Values('cook', 17);
Insert into Occupations Values('cameraman', 23);


Create table Employee(
Employee_id char(20) primary key,
Ename char(20),
Phone_no int,
Occupation char(20),
Foreign key(Occupation) references Occupations(Occupation) ON DELETE CASCADE,
HourWorked int
) ;

Insert into Employee Values('001', 'Adan', '666777888' , 'receptionist', 6);
Insert into Employee Values('002', 'Cindy', '888999888' , 'receptionist', 6);
Insert into Employee Values('003', 'Elen', '666999888' , 'receptionist', 6);
Insert into Employee Values('004', 'Bob', '999777888' , 'receptionist', 6);
Insert into Employee Values('005', 'Ava', '666777999' , 'manager', 6);


Create table WorksAt(
Employee_id char(20),
Event_id char(20),
Availability bit,
Foreign key (Employee_id) references Employee(Employee_id) ON DELETE CASCADE,
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
primary key(Employee_id, Event_id)
);

Insert into WorksAt Values('001', 'aaa', 1);
Insert into WorksAt Values('002', 'bbb', 1);
Insert into WorksAt Values('003', 'ccc', 1);
Insert into WorksAt Values('004', 'ddd', 1);
Insert into WorksAt Values('005', 'eee', 0);

Create table ParkingSpace(
Floor_no int,
Lot_no int,
Availability bit,
Type char(20),
Fan_ID char(20),
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE SET NULL,
Primary key (Floor_no, Lot_no)
);

Insert into ParkingSpace Values (1, 1, 1, 'private', NULL);
Insert into ParkingSpace Values (2, 2, 0, 'private', NULL);
Insert into ParkingSpace Values (3, 1, 1, 'reserved', 'F12345');
Insert into ParkingSpace Values (4, 7, 0, 'disabled', NULL);
Insert into ParkingSpace Values (3, 2, 1, 'normal', NULL);


Create table Fans(
Fan_ID char(20) primary key,
Phone_no char(20),
Fname char(20),
CreditCardInfo char(40)
);

Insert into Fans Values ('F12345', '1112223333', 'David Jones', 'MasterCard 1111222233334444');
Insert into Fans Values ('F11111', '2223331111', 'John Johns', 'Visa 2222333344441111');
Insert into Fans Values ('F22222', '3334441111', 'James Jame', 'Amex 3333444411112222');
Insert into Fans Values ('F33333', '4445556666', 'Don Dons', 'Visa 1111777788889999');
Insert into Fans Values ('F66666', '6661112222', 'Same Sames', 'MasterCard 1919919119199191');


Create table Merchandise1(
MName char(20) primary key,
Availability bit,
MakingCost int
);

Insert into Merchandise1 Values ('Cushion', 1, 2);
Insert into Merchandise1 Values ('Wallet', 1, 20);
Insert into Merchandise1 Values ('Tshirt', 0, 15);
Insert into Merchandise1 Values ('Bottle', 1, 4);
Insert into Merchandise1 Values ('Jacket', 0, 30);

Create table Merchandise2(
MakingCost int primary key,
SellingPrice int
);

Insert into Merchandise2 Values (10, 20);
Insert into Merchandise2 Values (20, 40);
Insert into Merchandise2 Values (30, 60);
Insert into Merchandise2 Values (40, 80);
Insert into Merchandise2 Values (50, 100);

Create table Food1 (
FoodName char(20) primary key,
Availability bit,
MakingCost int
);

Insert into food1 values ('Hotdog', 1, 2);
Insert into food1 values ('Hamburger', 1, 5);
Insert into food1 values ('French Fries', 0, 1);
Insert into food1 values ('Pizza', 1, 3);
Insert into food1 values ('Ice Cream', 0, 6);

Create table Food2 (
MakingCost int primary key,
SellingPrice int
);

Insert into food2 values (1, 2);
Insert into food2 values (2, 4);
Insert into food2 values (3, 6);
Insert into food2 values (4, 8);
Insert into food2 values (5, 10);

Create table Tickets (
Event_id char(20),
Seat_no int,
Section_no int,
Cost int,
Availability bit,
Fan_ID char(20),
Primary Key(Event_ID, Seat_no, Section_no),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE
);

Insert into Tickets values ('aaa', 1, 1, 10, 1, 'F12345');
Insert into Tickets values ('bbb', 2, 7, 20, 1, 'F11111');
Insert into Tickets values ('ccc', 18, 23, 200, 1,'F22222');
Insert into Tickets values ('ddd', 21, 5, 150, 1, 'F33333');
Insert into Tickets values ('eee', 17, 6, 75, 1, 'F66666');


Create table M_buys (
Fan_ID char (20),
MName char (20),
AccountNumUsed char(20),
Primary key (Fan_ID, MName),
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE,
Foreign key (MName) references Merchandise1(MName) ON DELETE CASCADE
);

Insert into M_buys values ('F12345', 'Tshirt', '111111');
Insert into M_buys values ('F11111', 'Jacket', '222222');
Insert into M_buys values ('F22222', 'Cushion','333333');
Insert into M_buys values ('F33333', 'Bottle', '444444');
Insert into M_buys values ('F66666', 'Bottle', '555555');



Create table F_buys (
Fan_ID char (20),
FoodName char (20),
AccountNumUsed char(20),
Primary key (Fan_ID, FoodName),
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE,
Foreign key (FoodName) references Food1(FoodName) ON DELETE CASCADE
);

Insert into F_buys values ('F12345', 'Hotdog', '111111');
Insert into F_buys values ('F11111', 'Hamburger', '222222');
Insert into F_buys values ('F22222', 'French Fries', '333333');
Insert into F_buys values ('F33333', 'Pizza', '444444');
Insert into F_buys values ('F66666', 'Ice Cream', '555555');


Create table M_sells (
Event_Id char(20),
SoldAtEvent bit,
Primary key (Event_id),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE
);

Insert into M_sells values ('aaa', 1);
Insert into M_sells values ('bbb', 0);
Insert into M_sells values ('ccc', 1);
Insert into M_sells values ('ddd', 0);
Insert into M_sells values ('eee', 1);


Create table F_sells (
Event_Id char(20),
SoldAtEvent bit,
Primary key (Event_id),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE
);

Insert into F_sells values ('aaa', 1);
Insert into F_sells values ('bbb', 0);
Insert into F_sells values ('ccc', 1);
Insert into F_sells values ('ddd', 0);
Insert into F_sells values ('eee', 1);
