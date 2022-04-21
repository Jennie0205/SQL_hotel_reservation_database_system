DROP TABLE IF EXISTS Guest;
CREATE TABLE Guest( 
guestID int,
guestName text,
Is_Active bool,
hotelID int,
roomID int,
updatedAt TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
PRIMARY KEY(guestID),
FOREIGN KEY(hotelID) REFERENCES Hotel(hotelID)
);

DROP TABLE IF EXISTS Country;
CREATE TABLE Country(
countryID varchar(50), 
countryName varchar(50),
PRIMARY KEY (countryID)
);

DROP TABLE IF EXISTS City;
CREATE TABLE City(
cityName varchar(50),
countryID varchar(50),
PRIMARY KEY(cityName),
FOREIGN KEY(countryID) REFERENCES Country(countryID)
);

DROP TABLE IF EXISTS Hotel;
CREATE TABLE Hotel(
hotelID int,
hotelName varchar(50), 
cityName varchar(50), 
starsRating int,
PRIMARY KEY(hotelID),
FOREIGN KEY(cityName) REFERENCES City(cityName)
);

DROP TABLE IF EXISTS Room;
CREATE TABLE Room(
hotelID int,
roomID int,
roomType varchar(50),
roomStatus varchar(50),
price int not null,
PRIMARY KEY(roomID),
FOREIGN KEY(hotelID) REFERENCES Hotel (hotelID)
);

DROP TABLE IF EXISTS Archive;
CREATE TABLE Archive(
guestID int, 
guestName text,
Is_Active bool, 
HotelID int,
RoomID int,
updatedAt date,
PRIMARY KEY(guestID)
);

insert into Guest values(101, 'Ann', 0, 7, 101, NOW());
insert into Guest values(102, 'James', 1, 2, 105, NOW());
insert into Guest values(103, 'Betty', 1, 1, 105, NOW());
insert into Guest values(104, 'Inez', 1, 3, 105, NOW());
insert into Guest values(105, 'Taylor', 1, 6, 103, NOW());
insert into Guest values(106, 'Este', 0, 2, 107, NOW());	
insert into Guest values(107, 'May', 1, 5, 102, NOW());
insert into Guest values(108, 'Dean', 0, 11, 110, NOW());

/*
used information from: https://datahub.io/core/country-list
*/
insert into Country values('AT', 'Austria');
insert into Country values('BR', 'Brazil');
insert into Country values('CA', 'Canada');
insert into Country values('CN', 'China');
insert into Country values('HK', 'HongKong');
insert into Country values('JP', 'Japan');
insert into Country values('US', 'United States');

/*
used information from: https://datahub.io/core/world-cities
*/
insert into City values('Vienna', 'AT');
insert into City values('Salzburg', 'AT');
insert into City values('Perth', 'BR');
insert into City values('Patos', 'BR');
insert into City values('Toronto', 'CA');
insert into City values('Montreal', 'CA');
insert into City values('Beijing', 'CN');
insert into City values('Shanghai', 'CN');
insert into City values('Victoria', 'HK');
insert into City values('Tokyo', 'JP');
insert into City values('Osaka', 'JP');
insert into City values('California', 'US');
insert into City values('NewYork', 'US');

insert into Hotel values(1, 'Marriott', 'Vienna', 5);
insert into Hotel values(2, 'Hyatt', 'Salzburg', 4);
insert into Hotel values(3, 'Four Seasons', 'Perth', 2);
insert into Hotel values(4, 'Ritz-Carlton Hotel', 'Patos', 2);
insert into Hotel values(5, 'Marriott', 'Toronto', 5);
insert into Hotel values(6, 'Hyatt', 'Montreal', 3);
insert into Hotel values(7, 'Four Seasons', 'Beijing', 2);
insert into Hotel values(8, 'Four Seasons', 'Shanghai', 1);
insert into Hotel values(9, 'Ritz-Carlton Hotel','Victoria',  3);
insert into Hotel values(10, 'Ritz-Carlton Hotel', 'Tokyo', 3);
insert into Hotel values(11, 'Marriott', 'Osaka', 5);
insert into Hotel values(12, 'Hyatt', 'NewYork', 4);
insert into Hotel values(13, 'Four Seasons', 'California', 2);
insert into Hotel values(14, 'Ritz-Carlton Hotel', 'Salzburg', 2);

insert into Room values(1, 101, 'suite', 'booked', 300);
insert into Room values(2, 102, 'single', 'empty', 100);
insert into Room values(3, 103, 'suite', 'booked', 50);
insert into Room values(4, 104, 'single', 'empty', 80);
insert into Room values(5, 105, 'suite', 'booked', 350);
insert into Room values(6, 106, 'single', 'empty', 70);
insert into Room values(7, 107, 'suite', 'booked', 120);
insert into Room values(7, 108, 'single', 'empty', 100);
insert into Room values(8, 109, 'suite', 'booked', 250);
insert into Room values(8, 110, 'single', 'empty', 160);
insert into Room values(9, 111, 'suite', 'booked', 300);
insert into Room values(9, 112, 'single', 'empty', 120);
insert into Room values(10, 113, 'suite', 'booked', 180);
insert into Room values(12, 114, 'single', 'empty', 80);
insert into Room values(13, 115, 'suite', 'booked', 110);
insert into Room values(14, 116, 'single', 'empty', 90);
insert into Room values(11, 117, 'suite', 'booked', 140);
insert into Room values(12, 118, 'single', 'empty', 150);
insert into Room values(13, 119, 'suite', 'booked', 370);
insert into Room values(14, 120, 'single', 'empty', 95);

/* 
Trigger 1:
Automatically deletes guests when their status becomes inactive / when they check out.
*/
CREATE TRIGGER deleteGuests
AFTER UPDATE ON Guest
FOR EACH ROW
	DELETE FROM Guest WHERE Is_Active = false;

/*
Trigger 2: 
When a guest moves to another room, everyone in their room moves as well  
*/
CREATE TRIGGER moveGuests
AFTER UPDATE ON Guest 
FOR EACH ROW
	UPDATE Guest
	SET roomID = new.roomID
    WHERE guestID IN (SELECT guestID2 FROM Guest g2 WHERE g2.roomID = old.roomID);
    
/*
Trigger 3: 
Automatically updates guest's room attribute to null when the guest's is_active status changes to 0
*/
CREATE TRIGGER updateGuestRoom
AFTER UPDATE ON Guest
FOR EACH ROW
	UPDATE Guest
    SET roomID = NULL WHERE is_Active = false;


/* Stored Procedure: Obtain guest list */
DROP PROCEDURE IF EXISTS getGuestList;
DELIMITER //
CREATE PROCEDURE getGuestList()
BEGIN
	SELECT guestID 
	FROM Guest
	WHERE Is_Active = True;
END // 
DELIMITER ;

