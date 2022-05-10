# Shop Android App 2022

Java-based e-commerce mobile application with .NET Core backend integration.

## Features
- User authentication (login/register)
- Product creation with image upload
- Products list with images
- Account management (edit/delete)

## Backend API Endpoints (.NET Core)
```
Account:
├── POST /api/Account/register   - User registration
├── GET  /api/Account/users      - Get all users  
├── POST /api/Account/login      - User login
├── POST /api/Account/edit       - Update user profile
└── POST /api/Account/delete     - Delete user account

Products:
├── POST /api/Products/create    - Create new product
└── GET  /api/Products/list      - Get all products list
```

Swagger documentation available at `http://10.0.2.2:5000/swagger` 

## Screenshots

**Products List**  
<img src="screenshots/main.png"  width="250" style="height:auto;" alt="Main View">


**Add New Product**  
<img src="screenshots/add.png"  width="250" style="height:auto;" alt="Add View">


**Login Screen**  
<img src="screenshots/login.png"  width="250" style="height:auto;" alt="Login View">


## Setup & Run

### Backend (.NET Core)
```bash
cd Shop
dotnet run
# API runs on http://localhost:5000
# Static files: /images/ for product images
```

### Real Device (WiFi)
Replace `10.0.2.2` with your **PC IP** (e.g. `192.168.1.100:5000`)

## Tech Stack
- **Frontend**: Java, AndroidX, Glide, Material Design
- **Backend**: .NET Core 8, EF Core, PostgreSQL
- **Networking**: Retrofit + OkHttp