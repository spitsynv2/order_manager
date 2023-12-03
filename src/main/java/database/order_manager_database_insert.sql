-- Insert data into the Restaurant table
INSERT INTO Restaurant (Name, Address, Phone, Email, Tax) VALUES
('Restaurant', 'Country, city'||char(10)||'Street name, zip', '+1 234 567', 'restaurant@example.com',23.00);

-- Insert data into the Dishes table
INSERT INTO Dishes (Name, Ingredients, Additional_info, Price) VALUES
('Spaghetti Carbonara', 'Spaghetti, Eggs, Pancetta, Parmesan cheese', 'Classic Italian dish', 12.99),
('Margherita Pizza', 'Pizza dough, Tomato sauce, Mozzarella cheese, Basil', 'Traditional pizza', 10.99),
('Caesar Salad', 'Romaine lettuce, Caesar dressing, Croutons, Parmesan cheese', 'Fresh and crispy', 8.99);

-- Insert data into the Restaurant_Dishes table
INSERT INTO Restaurant_Dishes (Dishes_Name, Restaurant_Name, Dish_status) VALUES
('Spaghetti Carbonara', 'Restaurant', 'Available'),
('Margherita Pizza', 'Restaurant', 'Available'),
('Caesar Salad', 'Restaurant', 'Available');

-- Insert data into the Users table
INSERT INTO Users (Name, Password, Permission) VALUES
('SampleUser', 'password123', 1),
('UserTest1222222222222222222222222222222222222', 'password123', 1),
('UserTest2', 'password123', 1),
('UserTest3', 'password123', 1),
('UserTest4', 'password123', 1);

-- Insert data into the Restaurant_Users table
INSERT INTO Restaurant_Users (User_Id, Restaurant_Name) VALUES
(1, 'Restaurant'),
(2, 'Restaurant'),
(3, 'Restaurant'),
(4, 'Restaurant'),
(5, 'Restaurant');

-- Insert data into the Orders table
INSERT INTO Orders (Id, Date, Payment_type, Status) VALUES
(1, '2023-10-27 12:00:00', 'Credit Card', 'Completed'),
(2, '2023-10-28 13:30:00', 'Cash', 'Pending');

-- Insert data into the Orders_Dishes table
INSERT INTO Orders_Dishes (Orders_Id, Dishes_Name) VALUES
(1, 'Spaghetti Carbonara'),
(1, 'Margherita Pizza'),
(2, 'Caesar Salad');

-- Insert data into the Users_Orders table
INSERT INTO Users_Orders (Users_Id, Orders_Id) VALUES
(1, 1),
(1, 2);

-- Insert data into the Print_details table
INSERT INTO Print_details (Restaurant_Name, Paper_size, Additional_info) VALUES
('Restaurant', '55mm', 'Discounts,'||char(10)||'some special info');
