-- Chuyển sang chế độ đơn người dùng để xóa DB nếu đang có kết nối
USE master;
ALTER DATABASE nhatNgheOnlineDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;

-- Xóa database nếu tồn tại
DROP DATABASE IF EXISTS nhatNgheOnlineDB;
GO

-- Tạo lại database
CREATE DATABASE nhatNgheOnlineDB;
GO

-- Sử dụng database
USE nhatNgheOnlineDB;
GO

---------------------------------------------------------
-- 1. Bảng Người Dùng
---------------------------------------------------------
CREATE TABLE Users (
    UserID INT PRIMARY KEY IDENTITY(1,1),
    FullName NVARCHAR(100) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    PasswordHash NVARCHAR(255) NOT NULL,
    Address NVARCHAR(255),
    Phone NVARCHAR(20),
    Gender BIT DEFAULT 1,
    BirthDay DATE,
    Role NVARCHAR(20) NOT NULL CHECK (Role IN ('Customer', 'Staff', 'Admin')),
    CreatedAt DATETIME DEFAULT GETDATE(),
    ImageURL NVARCHAR(255),
    LastLogin DATETIME NULL,
    LastAction NVARCHAR(255),
    LastIP NVARCHAR(45)
);

---------------------------------------------------------
-- 2. Bảng Danh Mục
---------------------------------------------------------
CREATE TABLE Categories (
    CategoryID INT PRIMARY KEY IDENTITY(1,1),
    CategoryName NVARCHAR(100) NOT NULL,
    ParentID INT NULL,
    FOREIGN KEY (ParentID) REFERENCES Categories(CategoryID)
);

---------------------------------------------------------
-- 3. Bảng Sản Phẩm
---------------------------------------------------------
CREATE TABLE Products (
    ProductID INT PRIMARY KEY IDENTITY(1,1),
    ProductName NVARCHAR(200) NOT NULL,
    Description NVARCHAR(MAX),
    BrandName NVARCHAR(255),
    Specifications NVARCHAR(MAX),
    Price DECIMAL(18,2) NOT NULL,
    ImageURL NVARCHAR(255),
    StockQuantity INT NOT NULL DEFAULT 0,
    CategoryID INT NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (CategoryID) REFERENCES Categories(CategoryID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 4. Bảng Hình ảnh sản phẩm
---------------------------------------------------------
CREATE TABLE ProductImages (
    ImageID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT NOT NULL,
    ImageURL NVARCHAR(255) NOT NULL,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 5. Bảng Phương Thức Thanh Toán (Moved up!)
---------------------------------------------------------
CREATE TABLE PaymentMethods (
    MethodID INT PRIMARY KEY IDENTITY(1,1),
    Code NVARCHAR(50) UNIQUE NOT NULL,
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(255),
    IconURL NVARCHAR(255)
);

---------------------------------------------------------
-- 6. Bảng Giỏ Hàng
---------------------------------------------------------
CREATE TABLE Carts (
    CartID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    AddedAt DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 7. Bảng Đơn Hàng
---------------------------------------------------------
CREATE TABLE Orders (
    OrderID INT PRIMARY KEY IDENTITY(1,1),
    UserID INT NOT NULL,
    OrderDate DATETIME DEFAULT GETDATE(),
    Status NVARCHAR(50) NOT NULL DEFAULT N'Chờ xử lý',
    TrackingCode NVARCHAR(100),
    ShippingAddress NVARCHAR(255),
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 8. Bảng Chi Tiết Đơn Hàng
---------------------------------------------------------
CREATE TABLE OrderItems (
    OrderItemID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT NOT NULL,
    ProductID INT NOT NULL,
    Quantity INT NOT NULL CHECK (Quantity > 0),
    Price DECIMAL(18,2) NOT NULL,
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 9. Bảng Thanh Toán (sau khi có PaymentMethods)
---------------------------------------------------------
CREATE TABLE Payments (
    PaymentID INT PRIMARY KEY IDENTITY(1,1),
    OrderID INT NOT NULL UNIQUE,
    PaymentMethodID INT NOT NULL,
    PaymentDate DATETIME DEFAULT GETDATE(),
    PaymentStatus NVARCHAR(50) NOT NULL DEFAULT N'Chưa thanh toán',
    TransactionID NVARCHAR(100),
    PaymentNote NVARCHAR(500),
    FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (PaymentMethodID) REFERENCES PaymentMethods(MethodID)
);

---------------------------------------------------------
-- 10. Bảng Đánh Giá
---------------------------------------------------------
CREATE TABLE Reviews (
    ReviewID INT PRIMARY KEY IDENTITY(1,1),
    ProductID INT NOT NULL,
    UserID INT NOT NULL,
    Rating INT NOT NULL CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    CreatedAt DATETIME DEFAULT GETDATE(),
    Approved BIT DEFAULT 0,
    FOREIGN KEY (ProductID) REFERENCES Products(ProductID) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------------------------------
-- 11. Indexes
---------------------------------------------------------
CREATE INDEX IX_Products_CategoryID ON Products(CategoryID);
CREATE INDEX IX_Carts_UserID ON Carts(UserID);
CREATE INDEX IX_Carts_ProductID ON Carts(ProductID);
CREATE INDEX IX_OrderItems_OrderID ON OrderItems(OrderID);
CREATE INDEX IX_Reviews_ProductID ON Reviews(ProductID);

---------------------------------------------------------
-- 12. Dữ liệu mẫu: Phương thức thanh toán
---------------------------------------------------------
INSERT INTO PaymentMethods (Code, Name, Description, IconURL) VALUES
('cod', N'Thanh toán khi giao hàng', N'Thanh toán tiền mặt khi nhận hàng', '/images/icons/cod.png'),
('vnpay', 'VNPay', N'Thanh toán qua cổng VNPay', '/images/icons/vnpay.png'),
('qrpay', N'Thanh toán bằng QR', N'Sử dụng mã QR để thanh toán qua ngân hàng hoặc ví', '/images/icons/qr.png');



--THÊM DỮ LIỆU
-- Bảng Người Dùng


-- Bảng Danh Mục
INSERT INTO Categories (CategoryName) VALUES 
(N'Laptop'), (N'Laptop Gaming'), (N'PC'),
(N'Main, CPU, VGA'), (N'Ổ cứng, RAM'), (N'Màn hình'),
(N'Bàn phím'), (N'Chuột & Lót chuột'), (N'Tai nghe'), (N'Phụ kiện');

-- Bảng Sản Phẩm
INSERT INTO Products (ProductName, Description, BrandName, Specifications, Price, ImageURL, StockQuantity, CategoryID) VALUES
-- Danh mục 1: Laptop (CategoryID=1)
('Dell XPS 13 9320', N'Laptop cao cấp mỏng nhẹ', 'Dell', '13.4″ FHD+, Core i7-1250U, 16GB, 512GB SSD', 32990000, '1.png', 10, 1),
('Dell Inspiron 15 3511', N'Laptop văn phòng phổ thông', 'Dell', '15.6″ FHD, Core i5-1135G7, 8GB, 256GB SSD', 18990000, '2.png', 15, 1),
('HP Pavilion 14-eg2073TU', N'Laptop học tập nhẹ nhàng', 'HP', '14″ FHD, Core i5-1235U, 8GB, 512GB SSD', 20990000, '3.png', 12, 1),
('Acer Swift 3 SF314-512', N'Mỏng nhẹ, pin lâu', 'Acer', '14″ FHD, Ryzen 7-7730U, 16GB, 512GB SSD', 22990000, '4.png', 8, 1),
('Lenovo IdeaPad Flex 5 14', N'2-in-1 cảm ứng tiện lợi', 'Lenovo', '14″ FHD, Ryzen 5-7530U, 16GB, 512GB SSD', 21990000, '5.png', 10, 1),
('MacBook Air M1', N'Apple siêu nhẹ', 'Apple', '13.3″ Retina, M1, 8GB, 256GB SSD', 27990000, '6.png', 6, 1),
('Asus Vivobook 15 X1500', N'Giá rẻ học sinh', 'Asus', '15.6″ FHD, Core i3-1115G4, 8GB, 256GB SSD', 14990000, '7.png', 20, 1),
('MSI Modern 14 C12M', N'Công sở mỏng nhẹ', 'MSI', '14″ FHD, Core i5-1235U, 8GB, 512GB SSD', 25990000, '8.png', 9, 1),

-- Danh mục 2: Laptop Gaming (CategoryID=2)
('Razer Blade 14 2025', N'Gaming cao cấp', 'Razer', '14″ 2.8K OLED, Ryzen 9-6900HX, RTX 3070 Ti, 16GB, 1TB SSD', 52990000, '9.png', 5, 2),
('Asus ROG Strix G15 G513', N'Gaming mạnh mẽ', 'Asus', '15.6″ QHD 300Hz, Ryzen 7-6800H, RTX 3070, 16GB, 1TB SSD', 42990000, '10.png', 7, 2),
('Lenovo Legion Pro 5 16IAH7H', N'Gaming hiệu năng cao', 'Lenovo', '16″ QHD, Core i7-12700H, RTX 3070 Ti, 16GB, 1TB SSD', 46990000, '11.png', 6, 2),
('Acer Nitro 5 AN515-58', N'Gaming phổ thông', 'Acer', '15.6″ FHD 144Hz, Core i5-12500H, RTX 3050 Ti, 8GB, 512GB SSD', 22990000, '12.png', 12, 2),
('MSI Katana GF66', N'Gaming tầm trung', 'MSI', '15.6″ FHD 144Hz, Ryzen 7-6800H, RTX 3060, 16GB, 512GB SSD', 27990000, '13.png', 10, 2),
('Dell G15 5520', N'Gaming bền bỉ', 'Dell', '15.6″ FHD 165Hz, Core i7-12700H, RTX 3060, 16GB, 512GB SSD', 31990000, '14.png', 8, 2),
('HP Victus 15-fa0253TX', N'Gaming giá trị tốt', 'HP', '15.6″ FHD 144Hz, Core i5-12500H, RTX 3050 Ti, 8GB, 512GB SSD', 21990000, '15.png', 11, 2),
('Gigabyte G5 GD-51VN124SH', N'Gaming bền', 'Gigabyte', '15.6″ FHD 144Hz, Core i7-11800H, RTX 3060, 16GB, 512GB SSD', 28990000, '16.png', 9, 2),

-- Danh mục 3: PC (CategoryID=3)
('Asus TUF Gaming GT502', N'PC gaming tản nhiệt tốt', 'Asus', 'Core i5-11400, RTX 3060, 16GB, 512GB SSD', 27990000, '17.png', 5, 3),
('MSI MAG Codex R5', N'PC đa năng', 'MSI', 'Core i7-12700F, RTX 3060 Ti, 16GB, 1TB SSD', 39990000, '18.png', 4, 3),
('Acer Predator Orion 3000', N'PC mạnh', 'Acer', 'Core i7-12700F, RTX 3070, 16GB, 1TB SSD', 44990000, '19.png', 3, 3),
('HP Victus TG01-2003d', N'PC phổ thông', 'HP', 'Core i5-12400F, GTX 1660 Super, 16GB, 512GB SSD', 23990000, '20.png', 7, 3),
('Lenovo Legion Tower 5', N'PC chơi game', 'Lenovo', 'Ryzen 7 5700G, RTX 3060, 16GB, 1TB SSD', 32990000, '21.png', 6, 3),
('Dell Inspiron Desktop DT1700', N'PC văn phòng', 'Dell', 'Core i5-12400, Intel UHD730, 8GB, 512GB SSD', 18990000, '22.png', 8, 3),
('CyberPowerPC Gamer Xtreme GXi700', N'PC gaming giá tốt', 'CyberPower', 'Core i5-11400F, GTX 1660 Super, 16GB, 500GB SSD', 21990000, '23.png', 5, 3),
('SkyTech Blaze II', N'PC gaming nhập khẩu', 'SkyTech', 'Ryzen 5 3600, RTX 2060, 16GB, 1TB SSD', 26990000, '24.png', 4, 3),

-- Danh mục 4: Main/CPU/VGA (CategoryID=4)
('Asus ROG Strix B550-F', N'Mainboard AM4 ROG', 'Asus', 'AM4, B550, ATX', 4990000, '25.png', 10, 4),
('MSI MPG Z690 Edge', N'Mainboard LGA1700', 'MSI', 'LGA1700, Z690, ATX', 6490000, '26.png', 7, 4),
('Gigabyte B760M DS3H', N'Mainboard LGA1700 giá rẻ', 'Gigabyte', 'LGA1700, B760, mATX', 3190000, '27.png', 12, 4),
('Intel Core i5-13400', N'CPU phổ thông', 'Intel', '6P+4E, 20 luồng', 5490000, '28.png', 15, 4),
('AMD Ryzen 5 7600X', N'CPU gaming', 'AMD', '6 nhân/12 luồng', 7290000, '29.png', 9, 4),
('NVIDIA GeForce RTX 4060', N'VGA tầm trung', 'NVIDIA', '8GB GDDR6', 9490000, '30.png', 6, 4),
('AMD Radeon RX 6700 XT', N'VGA gaming', 'AMD', '12GB GDDR6', 8990000, '31.png', 5, 4),
('Intel Core i7-13700K', N'CPU cao cấp', 'Intel', '8P+8E, 24 luồng', 10490000, '32.png', 4, 4),

-- Danh mục 5: Ổ cứng/RAM (CategoryID=5)
('Samsung SSD 970 EVO Plus 1TB', N'Ổ SSD NVMe', 'Samsung', '1TB, NVMe PCIe', 2790000, '33.png', 20, 5),
('WD Blue 4TB HDD', N'HDD desktop', 'Western Digital', '4TB, 5400 RPM', 2490000, '34.png', 15, 5),
('Crucial RAM 16GB DDR4 3200MHz', N'RAM máy bàn', 'Crucial', '16GB, DDR4', 1090000, '35.png', 30, 5),
('Kingston Fury Beast 32GB DDR5 5200MHz', N'RAM cao cấp', 'Kingston', '32GB, DDR5', 2090000, '36.png', 18, 5),
('Seagate Barracuda 2TB HDD', N'HDD đa dụng', 'Seagate', '2TB, 7200 RPM', 1290000, '37.png', 25, 5),
('Crucial P3 500GB NVMe SSD', N'SSD tầm trung', 'Crucial', '500GB, NVMe PCIe', 719000, '38.png', 22, 5),
('Samsung DDR4 8GB 3200MHz', N'RAM laptop', 'Samsung', '8GB, DDR4', 490000, '39.png', 27, 5),
('Kingston NV2 1TB NVMe SSD', N'SSD giá tốt', 'Kingston', '1TB, NVMe', 1490000, '40.png', 20, 5),

-- Danh mục 6: Màn hình (CategoryID=6)
('Samsung Odyssey G5 LF27G55T', N'Màn HD gaming 27″', 'Samsung', '27″, 165Hz, VA, 1ms', 6290000, '41.png', 10, 6),
('LG UltraGear 27GN800‑B', N'Gaming IPS 27″', 'LG', '27″, 144Hz, IPS 1ms', 5490000, '42.png', 8, 6),
('Dell UltraSharp U2723QE', N'4K cao cấp', 'Dell', '27″, 4K IPS', 13990000, '43.png', 5, 6),
('AOC 24G2SP', N'Gaming 24″ 165Hz', 'AOC', '24″, IPS, 165Hz', 4590000, '44.png', 12, 6),
('Asus ProArt PA278CV', N'Thiết kế chuyên nghiệp', 'Asus', '27″, WQHD', 7990000, '45.png', 7, 6),
('BenQ GW2480', N'Văn phòng 24″', 'BenQ', '24″, IPS', 2490000, '46.png', 14, 6),
('MSI Optix MAG273QPF', N'Gaming QHD', 'MSI', '27″, QHD 165Hz', 7290000, '47.png', 9, 6),
('Apple Studio Display 27″', N'Màn hi‑end', 'Apple', '27″, 5K Retina', 44990000, '48.png', 3, 6),

-- Danh mục 7: Bàn phím (CategoryID=7)
('Logitech MX Keys S', N'Bàn phím không dây cao cấp', 'Logitech', 'Wireless, RGB, MX', 3490000, '49.png', 15, 7),
('Keychron K8 Pro', N'Bàn phím cơ không dây', 'Keychron', 'Tenkeyless, Gateron', 1890000, '50.png', 10, 7),
('Razer Huntsman V3 Mini', N'Bàn phím gaming 60%', 'Razer', 'Opto‑mechanical', 2890000, '51.png', 8, 7),
('SteelSeries Apex Pro TKL', N'Gaming cơ cao cấp', 'SteelSeries', 'OmniPoint', 3990000, '52.png', 6, 7),
('AUKEY KM-G12', N'Cơ giá rẻ', 'Aukey', 'Blue switch', 490000, '53.png', 20, 7),
('Corsair K70 RGB MK.2', N'Gaming cơ LED', 'Corsair', 'Cherry MX Red', 4290000, '54.png', 7, 7),
('HyperX Alloy Origins Core', N'Cơ compact 65%', 'HyperX', 'Red switch', 2690000, '55.png', 9, 7),
('Ducky One 2 Mini', N'Bàn phím cơ 60%', 'Ducky', 'Cherry MX', 4990000, '56.png', 5, 7),

-- Danh mục 8: Chuột & Lót chuột (CategoryID=8)
('Logitech G502 X Plus', N'Chuột gaming 25K DPI', 'Logitech', 'Wireless, HERO 25K', 1890000, '57.png', 12, 8),
('Razer DeathAdder V3 Pro', N'Chuột không dây', 'Razer', 'HERO 30K', 2790000, '58.png', 10, 8),
('SteelSeries Aerox 3 Wireless', N'Chuột nhẹ', 'SteelSeries', '0.66 oz', 1990000, '59.png', 8, 8),
('Corsair M65 Pro', N'Gaming FPS', 'Corsair', '18K DPI, sniper', 1290000, '60.png', 15, 8),
('Microsoft Pro IntelliMouse', N'Chuột classic', 'Microsoft', '16000 DPI', 890000, '61.png', 14, 8),
('Glorious Model O', N'Chuột ultralight', 'Glorious', '67g', 1190000, '62.png', 11, 8),
('ASUS ROG Gladius III', N'Gaming có dây', 'Asus', '19K DPI', 1490000, '63.png', 9, 8),
('Cooler Master MM731', N'Chuột nhẹ có dây', 'Cooler Master', '59g', 1190000, '64.png', 13, 8),

-- Danh mục 9: Tai nghe (CategoryID=9)
('Sony WH-1000XM5', N'Tai nghe chống ồn cao cấp', 'Sony', 'Wireless, ANC', 7490000, '65.png', 8, 9),
('Bose QuietComfort 45', N'Tai nghe văn phòng', 'Bose', 'Wireless, ANC', 6990000, '66.png', 7, 9),
('Apple AirPods Max', N'Over-ear không dây', 'Apple', 'Spatial Audio', 12990000, '67.png', 5, 9),
('SteelSeries Arctis Nova Pro', N'Gaming cao cấp', 'SteelSeries', 'Wireless, ANC', 7990000, '68.png', 6, 9),
('HyperX Cloud Alpha S', N'Gaming wired', 'HyperX', 'Dual chamber', 2490000, '69.png', 10, 9),
('Razer BlackShark V2 Pro', N'Gaming wireless', 'Razer', 'Triforce 50mm', 3290000, '70.png', 9, 9),
('JBL Quantum 800', N'Gaming ANC', 'JBL', 'Wireless, 7.1 surround', 2790000, '71.png', 8, 9),
('Sennheiser HD 560S', N'Hi-fi open-back', 'Sennheiser', 'Open-ear', 3490000, '72.png', 5, 9),

-- Danh mục 10: Phụ kiện (CategoryID=10)
('WD My Passport 4TB', N'Ổ cứng di động', 'Western Digital', '4TB, USB-C', 2490000, '73.png', 12, 10),
('Anker PowerCore 10000PD', N'Sạc dự phòng', 'Anker', '10000mAh, Power Delivery', 899000, '74.png', 15, 10),
('Logitech C920 HD Pro', N'Webcam Full HD', 'Logitech', '1080p, autofocus', 1690000, '75.png', 10, 10),
('Elgato Stream Deck MK.2', N'Bảng điều khiển livestream', 'Elgato', '15 phím macro LCD', 3890000, '76.png', 6, 10),
('TP-Link Archer AX55', N'Router Wi-Fi 6', 'TP-Link', 'AX3000', 2290000, '77.png', 8, 10),
('Samsung T5 1TB', N'SSD di động', 'Samsung', '1TB, USB-C', 2490000, '78.png', 10, 10),
('JBL Flip 6', N'Loa di động', 'JBL', 'Bluetooth 5.3', 1290000, '79.png', 9, 10),
('Apple MagSafe Charger', N'Sạc không dây', 'Apple', 'MagSafe 15W', 1290000, '80.png', 7, 10);

