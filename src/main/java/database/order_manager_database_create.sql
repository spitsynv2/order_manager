-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-12-03 19:15:28.087

-- tables
-- Table: Dishes
CREATE TABLE Dishes (
    Id integer NOT NULL CONSTRAINT Dishes_pk PRIMARY KEY AUTOINCREMENT,
    Name varchar(250) NOT NULL,
    Ingredients varchar(750) NOT NULL,
    Additional_info varchar(400) NOT NULL,
    Price double NOT NULL
);

-- Table: Orders
CREATE TABLE Orders (
    Id integer NOT NULL CONSTRAINT Orders_pk PRIMARY KEY AUTOINCREMENT,
    Date datetime NOT NULL,
    Payment_type varchar(150) NOT NULL,
    Status varchar(200) NOT NULL
);

-- Table: Orders_Dishes
CREATE TABLE Orders_Dishes (
    Orders_Id integer NOT NULL,
    Dishes_Id integer NOT NULL,
    Dish_Name varchar(250) NOT NULL,
    CONSTRAINT Orders_Dishes_pk PRIMARY KEY (Orders_Id,Dishes_Id),
    CONSTRAINT Orders_Dishes FOREIGN KEY (Orders_Id)
    REFERENCES Orders (Id),
    CONSTRAINT Orders_Dishes_Dishes FOREIGN KEY (Dishes_Id)
    REFERENCES Dishes (Id)
);

-- Table: Orders_Restaurant
CREATE TABLE Orders_Restaurant (
    Orders_Id integer NOT NULL,
    Restaurant_Id integer NOT NULL,
    CONSTRAINT Orders_Restaurant_pk PRIMARY KEY (Orders_Id,Restaurant_Id),
    CONSTRAINT Orders_Restaurant FOREIGN KEY (Orders_Id)
    REFERENCES Orders (Id),
    CONSTRAINT Orders_Restaurant_Restaurant FOREIGN KEY (Restaurant_Id)
    REFERENCES Restaurant (Id)
);

-- Table: Print_details
CREATE TABLE Print_details (
    Restaurant_Id integer NOT NULL CONSTRAINT Print_details_pk PRIMARY KEY,
    Paper_size varchar(25) NOT NULL,
    Additional_info text,
    CONSTRAINT Print_details_Restaurant FOREIGN KEY (Restaurant_Id)
    REFERENCES Restaurant (Id)
);

-- Table: Restaurant
CREATE TABLE Restaurant (
    Id integer NOT NULL CONSTRAINT Restaurant_pk PRIMARY KEY AUTOINCREMENT,
    Name varchar(150) NOT NULL,
    Address text NOT NULL,
    Phone varchar(100) NOT NULL,
    Email varchar(100) NOT NULL,
    Tax double NOT NULL
);

-- Table: Restaurant_Dishes
CREATE TABLE Restaurant_Dishes (
    Restaurant_Id integer NOT NULL,
    Dishes_Id integer NOT NULL,
    Dish_status varchar(150) NOT NULL,
    CONSTRAINT Restaurant_Dishes_pk PRIMARY KEY (Restaurant_Id,Dishes_Id),
    CONSTRAINT Restaurant_Dishes_Restaurant FOREIGN KEY (Restaurant_Id)
    REFERENCES Restaurant (Id),
    CONSTRAINT Restaurant_Dishes_Dishes FOREIGN KEY (Dishes_Id)
    REFERENCES Dishes (Id)
);

-- Table: Restaurant_Users
CREATE TABLE Restaurant_Users (
    User_Id integer NOT NULL,
    Restaurant_Id integer NOT NULL,
    CONSTRAINT Restaurant_Users_pk PRIMARY KEY (User_Id,Restaurant_Id),
    CONSTRAINT Users_Restaurant FOREIGN KEY (User_Id)
    REFERENCES Users (Id),
    CONSTRAINT Restaurant_Users_Restaurant FOREIGN KEY (Restaurant_Id)
    REFERENCES Restaurant (Id)
);

-- Table: Users
CREATE TABLE Users (
    Id integer NOT NULL CONSTRAINT Users_pk PRIMARY KEY AUTOINCREMENT,
    Name varchar(250) NOT NULL,
    Password varchar(250) NOT NULL,
    Permission integer NOT NULL
);

-- Table: Users_Orders
CREATE TABLE Users_Orders (
    Users_Id integer NOT NULL,
    Orders_Id integer NOT NULL,
    CONSTRAINT Users_Orders_pk PRIMARY KEY (Users_Id,Orders_Id),
    CONSTRAINT Users_Orders FOREIGN KEY (Users_Id)
    REFERENCES Users (Id),
    CONSTRAINT Orders_Users FOREIGN KEY (Orders_Id)
    REFERENCES Orders (Id)
);

-- End of file.

