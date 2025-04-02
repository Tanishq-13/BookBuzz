# 📚 IIIT Una Literary Application  

## Overview  
The **IIIT Una Literary Application** is a platform where **students and teachers** can:  
- **Upload** books, important notes, and study materials.  
- **Download** and access shared resources.  
- **Review** and provide feedback on uploaded materials.  

This application ensures that only authorized **students and teachers (using institute email IDs)** can access and contribute to the platform.  

---

## 🛡️ Authorization Service  

### Purpose  
The **Authorization Service** is a criticalk component that manages:  
- **Secure login** for students and teachers (via institute email).  
- **Access control** for uploading, downloading, and reviewing materials.  
- **Token-based authentication** using JWT and refresh tokens.  

### Sequence UML Diagram  
Below is the **authentication flow** detailing user signup, login, token issuance, and refresh token management:

![Authorization Service UML](LLD%20of%20Authorization%20Service.webp)

---

## 🔑 Components  

1. **Client**: Sends authentication requests.  
2. **SecurityConfig**: Manages security configurations.  
3. **JWT Filter**: Validates JWT tokens in incoming requests.  
4. **AuthController**: Handles authentication endpoints.  
5. **TokenController**: Issues and refreshes tokens.  
6. **JWTService**: Generates and validates JWT tokens.  
7. **RefreshTokenService**: Creates and verifies refresh tokens.  
8. **UserDetailsServiceImpl**: Fetches user details from the database.  
9. **EventPublisher**: Publishes authentication events.  
10. **AuthenticationManager**: Authenticates user credentials.  
11. **AuthService Database**: Stores tokens and users.  

---

## 🔄 Authentication Flow  

1. **Signup (`/api/v1/signup`)**  
   - Registers a new user using an institute email.  
   - Saves user details in the database.  
   - Publishes an event to the queue.  

2. **Login (`/api/v1/login`)**  
   - Authenticates user credentials.  
   - Generates JWT and refresh tokens.  
   - Returns tokens to the client.  

3. **Token Refresh (`/api/v1/refreshToken`)**  
   - Verifies refresh token validity.  
   - Issues a new JWT.  

4. **Authorization Check (Middleware)**  
   - Validates JWT on each request.  
   - Ensures only authenticated users can upload, download, or review materials.  

---

## 🗄️ Database Design (MySQL)  

The **ER Diagram** for the Authorization Service is showwn below:  

![ER Diagram](ER%20diagram%20of%20MYSQL.webp)  

### Key Tables  
1. **users**  
   - Stores user credentials and profile details.  

2. **users_roles**  
   - Maps users to their assigned roles.  

3. **tokens**  
   - Stores JWT refresh tokens for session management.  

---

## ⚙️ Technologies Used  

- **Spring Security** for authentication.  
- **JWT** for stateless authentication.  
- **Spring Boot** for backend services.  
- **MySQL** for storing user details and tokens.  
