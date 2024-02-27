-- Insert data into the Restaurant table
INSERT INTO Restaurant (Name, Address, Phone, Email, Tax, ToPrint) VALUES
('Restaurant', 'Country, city'||char(10)||'Street name, zip', '+1 234 567', 'restaurant@example.com',23.00,'Name,Address,Phone,Email,Additional info');

-- Insert data into the Dishes table
INSERT INTO Dishes (Name, Ingredients_info, Type, Price) VALUES
('Margherita Pizza',
 'Pizza dough, ' || char(10) ||
 'Tomato sauce, ' || char(10) ||
 'Mozzarella cheese, ' || char(10) ||
 'Basil','Pizza',10.99),
('Caesar Salad',
 'Romaine lettuce, ' || char(10) ||
 'Caesar dressing, ' || char(10) ||
 'Croutons, ' || char(10) ||
 'Parmesan cheese','Salad', 8.99);

-- Insert data into the Restaurant_Dishes table
INSERT INTO Restaurant_Dishes (Dishes_Id, Restaurant_Id) VALUES
(1,1),
(2,1);

-- Insert data into the Users table
INSERT INTO Users (Name, Password, Permission) VALUES
('Admin', 'Admin', 1),
('Vadym', 'qwerty123', 1),
('UserTest2', 'password123', 1),
('UserTest3', 'password123', 1),
('UserTest4', 'password123', 1);

-- Insert data into the Restaurant_Users table
INSERT INTO Restaurant_Users (User_Id, Restaurant_Id) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(5, 1);

-- Insert data into the Orders_Restaurant table
INSERT INTO Orders_Restaurant (Order_Id, Restaurant_Id) VALUES
(1, 1),
(2, 1);

-- Insert data into the Users_Orders table
INSERT INTO Users_Orders (User_Id, Order_Id) VALUES
(1, 1),
(1, 2);

-- Insert data into the Print_details table
INSERT INTO Print_details (Restaurant_Id, Paper_size, Additional_info) VALUES
(1, '57mm x 40mm', 'Discounts,'||char(10)||'some special info');
