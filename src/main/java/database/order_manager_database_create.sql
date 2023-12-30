-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2023-12-30 05:25:28.005

-- tables
-- Table: Dishes
CREATE TABLE Dishes (
    Id integer NOT NULL CONSTRAINT Dishes_pk PRIMARY KEY AUTOINCREMENT,
    Name varchar(250) NOT NULL,
    Ingredients_info varchar(750) NOT NULL,
    Type varchar(400) NOT NULL,
    Price double NOT NULL
);

-- Table: Orders
CREATE TABLE Orders (
    Id integer NOT NULL CONSTRAINT Orders_pk PRIMARY KEY AUTOINCREMENT,
    Date text NOT NULL,
    Payment_type varchar(150) NOT NULL,
    Status varchar(200) NOT NULL
);

-- Table: Orders_Dishes
CREATE TABLE Orders_Dishes (
    Id_n integer NOT NULL CONSTRAINT Orders_Dishes_pk PRIMARY KEY AUTOINCREMENT,
    Dish_Name varchar(250) NOT NULL,
    Dish_Id integer NOT NULL,
    Order_Id integer NOT NULL,
    CONSTRAINT Orders_Dishes_Dishes FOREIGN KEY (Dish_Id)
    REFERENCES Dishes (Id),
    CONSTRAINT Orders_Dishes_Orders FOREIGN KEY (Order_Id)
    REFERENCES Orders (Id)
);

-- Table: Orders_Restaurant
CREATE TABLE Orders_Restaurant (
    Restaurant_Id integer NOT NULL,
    Order_Id integer NOT NULL,
    CONSTRAINT Orders_Restaurant_pk PRIMARY KEY (Restaurant_Id,Order_Id),
    CONSTRAINT Orders_Restaurant_Restaurant FOREIGN KEY (Restaurant_Id)
    REFERENCES Restaurant (Id),
    CONSTRAINT Orders_Restaurant_Orders FOREIGN KEY (Order_Id)
    REFERENCES Orders (Id)
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
    Tax double NOT NULL,
    ToPrint text NOT NULL
);

-- Table: Restaurant_Dishes
CREATE TABLE Restaurant_Dishes (
    Restaurant_Id integer NOT NULL,
    Dishes_Id integer NOT NULL,
    Dish_status varchar(150) NOT NULL,
    CONSTRAINT Restaurant_Dishes_pk PRIMARY KEY (Dishes_Id,Restaurant_Id),
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
    User_Id integer NOT NULL,
    Order_Id integer NOT NULL,
    CONSTRAINT Users_Orders_pk PRIMARY KEY (Order_Id,User_Id),
    CONSTRAINT Users_Orders FOREIGN KEY (User_Id)
    REFERENCES Users (Id),
    CONSTRAINT Orders_Users FOREIGN KEY (Order_Id)
    REFERENCES Orders (Id)
);

-- End of file.

