# Book Review Spring Boot Project with Admin Panel

This is a book review project built using **Spring Boot** with an **Admin Panel** designed using **Thymeleaf** for rendering HTML views. The project includes JWT-based authentication, Spring Data JPA for database interaction, and Spring Security for securing the application.

## Features

- **Admin Panel with Thymeleaf UI**: A fully designed admin panel using Thymeleaf, allowing for easy management of users and other resources.
- **JWT Authentication**: Secure the API with JWT tokens for user authentication.
- **Spring Data JPA**: Integrates with MySQL to handle database operations, including storing and retrieving user data.
- **Spring Security**: Provides authentication and authorization features to secure the admin panel and API endpoints.
- **Lombok**: Utilized to reduce boilerplate code for entity classes.
- **Spring Validation**: Built-in validation for input forms to ensure data integrity.

## Application Overview

### Admin Panel (Thymeleaf UI)
The **Admin Panel** is designed using **Thymeleaf** templates, where admins can manage both books and genres. It provides an interface for admins to:

- **Add New Books**: Admins can insert new books into the system, including details such as title, author, publication year, and genre.
- **Modify Existing Books**: Admins can update information about existing books.
- **Delete Books**: Admins can remove books from the system.
- **Manage Genres**: Admins can add new genres, modify existing genres, or delete genres.

Key functionalities include:
- **Book Management**: Display, add, update, and delete book details.
- **Genre Management**: Admins can manage genres associated with books.

### JWT Authentication
The application uses **JWT** for authenticating users. Upon login, a JWT token is issued, which must be included in the `Authorization` header for all subsequent requests. This ensures that only authenticated users can access protected resources, including the admin panel.

### Database Interaction (Spring Data JPA)
The backend uses **Spring Data JPA** to interact with a **MySQL** database. It handles book and genre data storage, as well as user reviews. The admin panel allows managing both books and genres stored in the database.

### Spring Security
**Spring Security** ensures that only authorized users can access the admin panel. Admins must log in using valid credentials, and their roles will determine whether they have the appropriate access rights.

### Validation
All user inputs, including book and genre data, are validated using **Spring Validation** annotations like `@NotNull`, `@Size`, and others to ensure that only valid data is submitted.

## Admin Panel Screenshots

Here are some screenshots of the Admin Panel:

### Login Page

This is the **Login Page**, where admins can log in to access the admin panel.

![Login Page](https://github.com/user-attachments/assets/f1cad307-8a35-426c-8065-12dad632973f)

### Dashboard

This is the main **Admin Dashboard**, where the admin can see an overview of the app, including options to manage books and genres.

![Admin Dashboard](https://github.com/user-attachments/assets/c82a0e99-1f7c-4259-97c3-606f30f30b7c)

### Add Book Page

This page allows the admin to add a new book to the database, including details like the title, author, and publication year.

![Add Book Page](https://github.com/user-attachments/assets/f7e0a64f-8aa1-49a9-ae0b-0c17ce99edae)

### View Book Page

This page displays the details of a specific book. Admins can view, edit, or delete the book from here.

![View Book Page](https://github.com/user-attachments/assets/12d7f70c-2a41-481b-aae0-f74466830de0)

### Add Genre Page

Admins can add new genres for books using this page. This allows for organizing books based on their genre.

![Add Genre Page](https://github.com/user-attachments/assets/031e1bbc-4e21-4694-95bc-c2fa86fffff9)

### View Genre Page

Here, admins can view and manage existing genres. Genres can be edited or deleted as needed.

![View Genre Page](https://github.com/user-attachments/assets/6ef6179e-60cb-41ed-b65c-58be56653e4e)
