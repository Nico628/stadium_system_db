SET FOREIGN_KEY_CHECKS = 1;
Create table Counters(
CID integer primary key,
fanID_counter integer,
normal_seat_counter integer,
vip_seat_counter integer,
normal_parking_counter integer,
disabled_parking_counter integer,
event_counter integer,
employee_counter integer
);
Insert into Counters Values (0, 0, 0, 2000, 200, 0, 0 , 0);

Create table Fans(
Fan_ID integer,
Phone_no char(20),
Fname char(20),
CreditCardInfo char(40),
primary key (Fan_ID)
);

Create table Merchandise1(
MName char(20) ,
Availability integer,
MakingCost integer,
primary key(MName)
);


Create table Merchandise2(
MakingCost integer,
SellingPrice integer,
primary key (MakingCost)
);

Create table Food1 (
FoodName char(20),
Availability integer,
MakingCost integer,
primary key(FoodName)
);


Create table Food2 (
MakingCost integer,
SellingPrice integer,
primary key (MakingCost)
);

Create table M_buys (
Fan_ID integer,
MName char (20),
Quantity integer,
Primary key (Fan_ID, MName),
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE,
Foreign key (MName) references Merchandise1(MName) ON DELETE CASCADE
);

Create table F_buys (
Fan_ID integer,
FoodName char (20),
Quantity integer,
Primary key (Fan_ID, FoodName),
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE,
Foreign key (FoodName) references Food1(FoodName) ON DELETE CASCADE
);


Create table ParkingSpace(
Floor_no integer,
Lot_no integer,
Availability integer,
Fan_ID integer,
Disabled integer,
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE SET NULL,
Primary key (Floor_no, Lot_no)
);


Create table BookKeeping1 (
Edate Date ,
Income REAL,
Expense REAL,
Attendence integer,
NetIncome REAL,
primary key(Edate));

Create table Stadium_Events(
Event_id integer,
EventName char(20),
StartTime TIMESTAMP,
EndTime TIMESTAMP,
TicketSold integer,
Edate Date,
primary key(Event_id),
Foreign key(Edate) references BookKeeping1(Edate) ON DELETE SET NULL
);


Create table Concerts(
Event_id integer ,
Performer char(20),
Capacity integer,
primary key(Event_id),
Foreign key(Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE);


Create table Team(
HomeTeam char(20),
SportsType char(20),
primary key(HomeTeam) );


Create table Games(
Event_id integer,
HomeTeam char(20),
AwayTeam char(20),
primary key(Event_id),
Foreign key(Event_id) references Stadium_Events(Event_ID) ON DELETE CASCADE,
Foreign key(HomeTeam) references Team(HomeTeam) ON DELETE CASCADE);


Create table Sponsorship1(
CompanyName char(30) ,
Donations REAL,
PRIMARY KEY(CompanyName)
);


Create table Sponsors(
CompanyName char(30),
Event_id integer,
SizeofAd integer,
Foreign key (CompanyName) references Sponsorship1(CompanyName) ON DELETE CASCADE,
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
primary key(CompanyName, Event_id)
);


Create table Occupations(
Occupation char(20),
HourlyWage REAL,
primary key(Occupation));

Insert into Occupations Values('receptionist', 18);
Insert into Occupations Values('manager', 25);
Insert into Occupations Values('janitor', 15);
Insert into Occupations Values('cook', 17);
Insert into Occupations Values('cameraman', 23);

Create table Employee(
Employee_id integer,
Ename char(20),
Phone_no integer,
HourWorked integer,
Occupation char(20),
primary key(Employee_id),
Foreign key(Occupation) references Occupations(Occupation) ON DELETE CASCADE
);

Create table WorksAt(
Employee_id integer,
Event_id integer,
Availability integer,
Foreign key (Employee_id) references Employee(Employee_id) ON DELETE CASCADE,
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
primary key(Employee_id, Event_id));

Create table Tickets (
Event_id integer,
Seat_no integer,
Section_no integer,
Cost integer,
Availability integer,
Fan_ID integer,
Primary Key(Event_ID, Seat_no, Section_no),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
Foreign key (Fan_ID) references Fans(Fan_ID) ON DELETE CASCADE
);


Create table M_sells (
Event_Id integer,
MName char (20),
Quantity integer,
Primary key (Event_id, MName),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
Foreign key (MName) references Merchandise1(MName) ON DELETE CASCADE
);

Create table F_sells (
Event_Id integer,
FoodName char(20),
Quantity integer,
Primary key (Event_id,FoodName),
Foreign key (Event_id) references Stadium_Events(Event_id) ON DELETE CASCADE,
Foreign key (FoodName) references Food1(FoodName)
);
