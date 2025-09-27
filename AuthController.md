# AuthController - Freelancer Platform

The `AuthController` handles all **authentication-related operations** for the freelancer platform. It manages signup, login, logout, password reset, email verification, and optional two-factor authentication (2FA).

---

## **Base URL**
/api/auth

---

## **Features & Workflow**

### 1. **Signup (`/signup`)**
- Registers a new **Freelancer** or **Client**.
- Prevents direct **ADMIN** registration.
- Generates a **verification token** and sends an email.
- User is inactive until email is verified.

**Flow:**
User submits signup → System saves user → Sends verification email → User clicks link → Email verified


---

### 2. **Login (`/login`)**
- Validates email & password.
- Checks if **email is verified**.
- If **2FA enabled**, generates a temporary OTP and sends it via email.
- If 2FA not enabled, returns **JWT access & refresh tokens**.

---

### 3. **Logout (`/logout`)**
- Invalidates the current session/token.
- Optional: Add token to blacklist if maintaining session invalidation.

---

### 4. **Refresh Token (`/refresh-token`)**
- Accepts a valid **refresh token**.
- Returns a new **access token**.

---

### 5. **Forgot Password (`/forgot-password`)**
- Generates a **password reset token**.
- Sends reset link/OTP via email.

---

### 6. **Reset Password (`/reset-password`)**
- Accepts **email, reset token, and new password**.
- Validates token and updates password.

---

### 7. **Email Verification (`/verify-email`)**
- Verifies the user’s email using the **verification token** sent during signup.
- Marks user as **verified**.

---

### 8. **Enable Two-Factor Authentication (2FA) (`/2fa/enable`)**
- Enables 2FA for **verified users only**.
- Sends confirmation email.

---

### 9. **Verify Two-Factor Authentication (`/2fa/verify`)**
- Verifies the **OTP sent to user email**.
- Issues **JWT access & refresh tokens** after successful verification.

---

## **Notes**
- All passwords are hashed using **BCrypt**.
- OTP for 2FA expires in **5 minutes**.
- JWT tokens are used for secure authentication.
- Ensures **secure, seamless authentication** for freelancers and clients.

