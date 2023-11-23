CREATE TABLE Dishes (
    Name varchar(250) NOT NULL CONSTRAINT Dishes_pk PRIMARY KEY,
    Ingredients varchar(750) NOT NULL,
    Additional_info varchar(400) NOT NULL,
    Price double NOT NULL
);

CREATE TABLE Orders (
    Id integer NOT NULL CONSTRAINT Orders_pk PRIMARY KEY,
    Date datetime NOT NULL,
    Payment_type varchar(150) NOT NULL,
    Status varchar(200) NOT NULL
);

-- Table: Orders_Dishes
CREATE TABLE Orders_Dishes (
    Orders_Id integer NOT NULL,
    Dishes_Name varchar(250) NOT NULL,
    CONSTRAINT Orders_Dishes_pk PRIMARY KEY (Orders_Id,Dishes_Name),
    CONSTRAINT Orders_Dishes FOREIGN KEY (Orders_Id)
    REFERENCES Orders (Id),
    CONSTRAINT Dishes_Orders FOREIGN KEY (Dishes_Name)
    REFERENCES Dishes (Name)
);

-- Table: Orders_Restaurant
CREATE TABLE Orders_Restaurant (
    Restaurant_Name varchar(150) NOT NULL,
    Orders_Id integer NOT NULL,
    CONSTRAINT Orders_Restaurant_pk PRIMARY KEY (Restaurant_Name,Orders_Id),
    CONSTRAINT Restaurant_Orders FOREIGN KEY (Restaurant_Name)
    REFERENCES Restaurant (Name),
    CONSTRAINT Orders_Restaurant FOREIGN KEY (Orders_Id)
    REFERENCES Orders (Id)
);

-- Table: Print_details
CREATE TABLE Print_details (
    Restaurant_Name varchar(150) NOT NULL CONSTRAINT Print_details_pk PRIMARY KEY,
    Paper_size varchar(25) NOT NULL,
    Additional_info text NOT NULL,
    CONSTRAINT Print_details_Restaurant FOREIGN KEY (Restaurant_Name)
    REFERENCES Restaurant (Name)
);

-- Table: Restaurant
CREATE TABLE Restaurant (
    Name varchar(150) NOT NULL CONSTRAINT Restaurant_pk PRIMARY KEY,
    Address varchar(300) NOT NULL,
    Phone varchar(100) NOT NULL,
    Email varchar(100) NOT NULL
);

-- Table: Restaurant_Dishes
CREATE TABLE Restaurant_Dishes (
    Dishes_Name varchar(250) NOT NULL,
    Restaurant_Name varchar(150) NOT NULL,
    Dish_status varchar(150) NOT NULL,
    CONSTRAINT Restaurant_Dishes_pk PRIMARY KEY (Restaurant_Name,Dishes_Name),
    CONSTRAINT Restaurant_Dishes FOREIGN KEY (Restaurant_Name)
    REFERENCES Restaurant (Name),
    CONSTRAINT Dishes_Restaurant FOREIGN KEY (Dishes_Name)
    REFERENCES Dishes (Name)
);

-- Table: Restaurant_Users
CREATE TABLE Restaurant_Users (
    User_Id integer NOT NULL,
    Restaurant_Name varchar(150) NOT NULL,
    CONSTRAINT Restaurant_Users_pk PRIMARY KEY (Restaurant_Name,User_Id),
    CONSTRAINT Users_Restaurant FOREIGN KEY (User_Id)
    REFERENCES Users (Id),
    CONSTRAINT Restaurant_Users FOREIGN KEY (Restaurant_Name)
    REFERENCES Restaurant (Name)
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

