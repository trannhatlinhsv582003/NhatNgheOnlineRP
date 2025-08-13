# ERD MỨC 1 - HỆ THỐNG NHATNGHEONLINE
## Sơ đồ quan hệ thực thể theo chuẩn Crow's Foot

```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                    USERS                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ UserID (PK) │ FullName │ Email │ PasswordHash │ Address │ Phone │ Gender │     │
│ BirthDay    │ Role     │ CreatedAt │ ImageURL │ LastLogin │ LastAction │ LastIP│
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ 1
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  CATEGORIES                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│ CategoryID (PK) │ CategoryName │ ParentID (FK)                                 │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ 1
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                   PRODUCTS                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ProductID (PK) │ ProductName │ Description │ BrandName │ Specifications │      │
│ Price          │ ImageURL    │ StockQuantity │ CategoryID (FK) │ CreatedAt     │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ 1
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                PRODUCTIMAGES                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ImageID (PK) │ ProductID (FK) │ ImageURL                                       │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                    CARTS                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ CartID (PK) │ UserID (FK) │ ProductID (FK) │ Quantity │ AddedAt               │
└─────────────────────────────────────────────────────────────────────────────────┘
         ▲
         │ N
         │
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                   ORDERS                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│ OrderID (PK) │ UserID (FK) │ OrderDate │ Status │ TrackingCode │              │
│ ShippingAddress │ ShipperID (FK)                                               │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ 1
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 ORDERITEMS                                      │
├─────────────────────────────────────────────────────────────────────────────────┤
│ OrderItemID (PK) │ OrderID (FK) │ ProductID (FK) │ Quantity │ Price           │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                  PAYMENTS                                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ PaymentID (PK) │ OrderID (FK) │ PaymentMethodID (FK) │ PaymentDate │          │
│ PaymentStatus  │ TransactionID │ PaymentNote                                   │
└─────────────────────────────────────────────────────────────────────────────────┘
         │
         │ N
         │
         ▼
┌─────────────────────────────────────────────────────────────────────────────────┐
│                               PAYMENTMETHODS                                    │
├─────────────────────────────────────────────────────────────────────────────────┤
│ MethodID (PK) │ Code │ Name │ Description │ IconURL                            │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                   REVIEWS                                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ ReviewID (PK) │ ProductID (FK) │ UserID (FK) │ Rating │ Comment │             │
│ CreatedAt     │ Approved                                                       │
└─────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────┐
│                                 OTP_TOKEN                                       │
├─────────────────────────────────────────────────────────────────────────────────┤
│ id (PK) │ email │ otp │ created_at │ verified                                  │
└─────────────────────────────────────────────────────────────────────────────────┘

```

## MỐI QUAN HỆ CHI TIẾT:

### 1. USERS (Người dùng)
- **1:N** với ORDERS (Một user có thể có nhiều đơn hàng)
- **1:N** với CARTS (Một user có thể có nhiều item trong giỏ hàng)
- **1:N** với REVIEWS (Một user có thể viết nhiều đánh giá)
- **1:N** với OTP_TOKEN (Một user có thể có nhiều OTP)

### 2. CATEGORIES (Danh mục)
- **1:N** với PRODUCTS (Một danh mục có thể có nhiều sản phẩm)
- **1:N** với chính nó (ParentID - danh mục con)

### 3. PRODUCTS (Sản phẩm)
- **N:1** với CATEGORIES (Nhiều sản phẩm thuộc một danh mục)
- **1:N** với PRODUCTIMAGES (Một sản phẩm có thể có nhiều hình ảnh)
- **1:N** với CARTS (Một sản phẩm có thể có trong nhiều giỏ hàng)
- **1:N** với ORDERITEMS (Một sản phẩm có thể có trong nhiều đơn hàng)
- **1:N** với REVIEWS (Một sản phẩm có thể có nhiều đánh giá)

### 4. ORDERS (Đơn hàng)
- **N:1** với USERS (Nhiều đơn hàng thuộc một user)
- **1:N** với ORDERITEMS (Một đơn hàng có thể có nhiều item)
- **1:1** với PAYMENTS (Một đơn hàng có một thanh toán)
- **N:1** với USERS (ShipperID - nhiều đơn hàng có thể được giao bởi một shipper)

### 5. PAYMENTS (Thanh toán)
- **N:1** với PAYMENTMETHODS (Nhiều thanh toán sử dụng một phương thức)

## KÝ HIỆU QUAN HỆ:
- **1** = Một
- **N** = Nhiều
- **PK** = Primary Key (Khóa chính)
- **FK** = Foreign Key (Khóa ngoại)
- **UK** = Unique Key (Khóa duy nhất) 