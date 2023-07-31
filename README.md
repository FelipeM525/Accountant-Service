# Accountant Service





The **Accountant Service** is a Spring-based project made for studying  Spring Security, Spring Data JPA, and creating RESTful APIs for web applications. It demonstrates how to implement user authentication and authorization, handle RESTful API endpoints using Spring Data JPA for database interactions, and secure the application using Spring Security.

## Features

- User registration (Sign up) with validation for name, lastname, email, and password fields.
- User authentication and login with proper error handling for failed login attempts.
- CRUD operations for managing user roles, including granting and removing roles.
- Endpoint access control and security using Spring Security.
- API endpoints for managing employee data, such as creating, updating, and retrieving employee payment information.
- Logging events and access control auditing using the Auditor role.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle(Groovy) build system



## API Endpoints

### User Registration (Sign up)

- **Endpoint:** `POST /api/auth/signup`
- **Authentication:** `Not required`
- **Request Body:**
```
{
"name": "John",
"lastname": "Doe",
"email": "johndoe@acme.com",
"password": "secret"
}
```

- **Response: 200 OK**
```
{
"name": "John",
"lastname": "Doe",
"email": "johndoe@acme.com"
}
```
- **Response: 400 Bad Request (Invalid Request)**
```
{
"timestamp": "<date>",
"status": 400,
"error": "Bad Request",
"message": "The password length must be at least 12 chars!",
"path": "/api/auth/signup"
}
```
- **Endpoint:** `POST  api/auth/changepass`
- **Authentication:** `Required (USER)`
- **Request Body:**
```
{
   "new_password": "bZPGqH7fTJWW"
}
```
- **Response: 200 OK**
```
{
    "email": "johndoe@acme.com",
    "status": "The password has been updated successfully"
}
```
- **Endpoint:** `POST  api/auth/changepass`
- **Authentication:** `Required (USER)`
- **Request Body:**
```
{
   "new_password": "bZPGqH7fTJWW"
}
```
- **Response: 400 Bad Request(The passwords must be different)**
```
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "The passwords must be different!",
    "path": "/api/auth/changepass"
}
```
### User Authentication (Login)

- **Endpoint:** `POST /api/auth/login`
- **Authentication:** `Not required`
- **Request Body:**
```
{
"email": "johndoe@acme.com",
"password": "secret"
}
```
- **Response: 200 OK**
```
{
"id": 1,
"name": "John",
"lastname": "Doe",
"email": "johndoe@acme.com"
}
```


- **Response: 400 Bad Request (Invalid Credentials)**
```
{
"timestamp": "<date>",
"status": 400,
"error": "Bad Request",
"message": "Invalid credentials",
"path": "/api/auth/login"
}
```
### Access Employee Payment Data

- **Endpoint:** `GET /api/empl/payment`
- **Authorization:** `Required (USER)`
- **Response: 200 OK**
```
{
"id": 1,
"name": "John",
"lastname": "Doe",
"email": "johndoe@acme.com"
}
```

- **Response: 401 Unauthorized**
```
{
"timestamp": "<date>",
"status": 401,
"error": "Unauthorized",
"message": "Access Denied!",
"path": "/api/empl/payment"
}
```
- **Endpoint:** `POST api/acct/payments`
- **Authentication:** `Required (Accountant)`
```
[
    {
        "employee": "johndoe@acme.com",
        "period": "01-2021",
        "salary": 123456
    },
    {
        "employee": "johndoe@acme.com",
        "period": "02-2021",
        "salary": 123456
    },
    {
        "employee": "johndoe@acme.com",
        "period": "03-2021",
        "salary": 123456
    }
]
```
- **Response 200 OK**
```
{
   "status": "Added successfully!"
}
```
- **Endpoint:** `POST api/acct/payments`
- **Authentication:** `Required (ACCOUNTANT)`
```
[
    {
        "employee": "johndoe@acme.com",
        "period": "01-2021",
        "salary": -1
    },
    {
        "employee": "johndoe@acme.com",
        "period": "13-2021",
        "salary": 123456
    },
    {
        "employee": "johndoe@acme.com",
        "period": "03-2021",
        "salary": 123456
    }
]
```
- **Response 400 Bad Request**
```
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "payments[0].salary: Salary must be non negative!, payments[1].period: Wrong date!",
    "path": "/api/acct/payments"
}
```
- **Endpoint:** `POST api/acct/payments`
- **Authentication:** `Required (ACCOUNTANT)`
```
[
    {
        "employee": "johndoe@acme.com",
        "period": "01-2021",
        "salary": 123456
    },
    {
        "employee": "johndoe@acme.com",
        "period": "01-2021",
        "salary": 123456
    }
]
```
- **Response 400 Bad Request**
```
{
    "timestamp": "<data>",
    "status": 400,
    "error": "Bad Request",
    "message": "Error!",
    "path": "/api/acct/payments"
}
```
- **Endpoint:** `PUT api/acct/payments`
- **Authentication:** `Required (ACCOUNTANT)`
```
{
    "employee": "johndoe@acme.com",
    "period": "01-2021",
    "salary": 123457
}
```
- **Response: 200 OK**
```
{
   "status": "Updated successfully!"
}
```
- **Endpoint:** `GET api/empl/payment?period=01-2021`
- **Authentication:** `Required (USER,ACCOUNTANT)`
- **Response: 200 OK**
```
{
   "name": "John",
   "lastname": "Doe",
   "period": "January-2021",
   "salary": "1234 dollar(s) 56 cent(s)"
}
```
- **Endpoint:** `GET  api/empl/payment`
- **Authentication:** `Required (USER,ACCOUNTANT)`
- **Response: 200 OK**
```
[
    {
       "name": "John",
       "lastname": "Doe",
       "period": "March-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    },
    {
       "name": "John",
       "lastname": "Doe",
       "period": "February-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    },
    {
       "name": "John",
       "lastname": "Doe",
       "period": "January-2021",
       "salary": "1234 dollar(s) 56 cent(s)"
    }
]
```
- **Endpoint:** `GET api/empl/payment?period=13-2021`
- **Authentication:** `Required (USER,ACCOUNTANT)`
- **Response: 200 OK**
```
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Error!",
    "path": "/api/empl/payment"
}
```
### The Authorization

- **Endpoint:** `PUT /api/admin/user/role`
- **Authentication:** `Required (ADMINISTRATOR)`
```
{
   "user": "ivanivanov@acme.com",
   "role": "ACCOUNTANT",
   "operation": "GRANT"
}
```
- **Response: 200 OK**
```
{
    "id": 2,
    "name": "Ivan",
    "lastname": "Ivanov",
    "email": "ivanivanov@acme.com",
    "roles": [
        "ROLE_ACCOUNTANT",
        "ROLE_USER"
    ]
}
```
- **Endpoint:** `GET api/admin/user`
- **Authentication:** `Required (ADMINISTRATOR)`
- **Reponse: 200 OK**
```
[
    {
        "id": 1,
        "name": "John",
        "lastname": "Doe",
        "email": "johndoe@acme.com",
        "roles": [
            "ROLE_ADMINISTRATOR"
        ]
    },
    {
        "id": 2,
        "name": "Ivan",
        "lastname": "Ivanov",
        "email": "ivanivanov@acme.com",
        "roles": [
            "ROLE_ACCOUNTANT",
            "ROLE_USER"
        ]
    }
]
```
- **Endpoint:** `PUT /api/admin/user/role`
- **Authentication:** `Required (ADMINISTRATOR)`
```
{
   "user": "ivanivanov@acme.com",
   "role": "ACCOUNTANT",
   "operation": "REMOVE"
}
```
- **Response 200 OK**
```
{
    "id": 2,
    "name": "Ivan",
    "lastname": "Ivanov",
    "email": "ivanivanov@acme.com",
    "roles": [
        "ROLE_USER"
    ]
}
```
- **Endpoint:** `PUT /api/admin/user/role` with the correct authentication under the User role
- **Authentication:** `Required (ADMINISTRATOR)`
- **Response: 403 FORBIDDEN**
```
{
    "timestamp": "<date>",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied!",
    "path": "/api/admin/user/"
}
```
- **Endpoint:** `DELETE /api/admin/user/ivanivanov@acme.com`
- **Authentication:** `Required (ADMINISTRATOR)`
- **Response 200 OK**
```
{
    "user": "ivanivanov@acme.com",
    "status": "Deleted successfully!"
}
```
- **Endpoint:** `DELETE /api/admin/user/johndoe@acme.com`
- **Authentication:** `Required (ADMINISTRATOR)`
- **Response 400 Bad Request**
```
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't remove ADMINISTRATOR role!",
    "path": "/api/admin/user/johndoe@acme.com"
}
```
- **Endpoint:** `DELETE /api/admin/user/ivanivanov@acme.com`
- **Authentication:** `Required (Administrator)`
- **Response 404 Not Found**
```
{
    "timestamp": "<date>",
    "status": 404,
    "error": "Not Found",
    "message": "User not found!",
    "path": "/api/admin/user/ivanivanov@acme.com"
}
```
- **Endpoint:** `PUT api/admin/user/access`
- **Authentication:** `Required (ADMINISTRATOR)`
```
{
   "user": "administrator@acme.com",
   "operation": "LOCK" 
}
```
- **Response 400 Bad Request**
```
{
    "timestamp": "<date>",
    "status": 400,
    "error": "Bad Request",
    "message": "Can't lock the ADMINISTRATOR!",
    "path": "/api/admin/user/access"
}
```
- **Endpoint:** `PUT api/admin/user/access`
- **Authentication:** `Required (ADMINISTRATOR)`
```
{
   "user": "user@acme.com",
   "operation": "LOCK" 
}
```
- **Response 200 OK**
```
{
    "status": "User user@acme.com locked!"
}
```
