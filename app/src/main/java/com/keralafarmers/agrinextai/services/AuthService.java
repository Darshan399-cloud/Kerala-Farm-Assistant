package com.keralafarmers.agrinextai.services;

import android.content.Context;
import android.os.AsyncTask;
import com.keralafarmers.agrinextai.database.AppDatabase;
import com.keralafarmers.agrinextai.database.UserDao;
import com.keralafarmers.agrinextai.models.User;
import com.keralafarmers.agrinextai.utils.SecurityUtils;
import com.keralafarmers.agrinextai.utils.SessionManager;

/**
 * Authentication Service for handling user registration, login, and profile management
 */
public class AuthService {
    
    private Context context;
    private UserDao userDao;
    private SessionManager sessionManager;
    
    public AuthService(Context context) {
        this.context = context;
        this.userDao = AppDatabase.getInstance(context).userDao();
        this.sessionManager = new SessionManager(context);
    }
    
    /**
     * Interface for authentication callbacks
     */
    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }
    
    /**
     * Interface for registration callbacks
     */
    public interface RegistrationCallback {
        void onSuccess(User user);
        void onError(String error);
    }
    
    /**
     * Register a new user (overloaded method for RegisterActivity)
     * @param fullName Full name of the farmer
     * @param email User email
     * @param phoneNumber Phone number
     * @param farmLocation Farm location
     * @param password User password
     * @param callback Authentication callback
     */
    public void registerUser(String fullName, String email, String phoneNumber, 
                           String farmLocation, String password, AuthCallback callback) {
        registerUser(email, password, fullName, phoneNumber, farmLocation, 0.0, new RegistrationCallback() {
            @Override
            public void onSuccess(User user) {
                callback.onSuccess(user);
            }
            
            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }
    
    /**
     * Register a new user
     * @param email User email
     * @param password User password
     * @param farmerName Farmer name
     * @param phoneNumber Phone number
     * @param farmLocation Farm location
     * @param farmSize Farm size in acres
     * @param callback Registration callback
     */
    public void registerUser(String email, String password, String farmerName, 
                           String phoneNumber, String farmLocation, double farmSize,
                           RegistrationCallback callback) {
        
        new AsyncTask<Void, Void, String>() {
            private User newUser;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Validate input
                    if (!SecurityUtils.isValidEmail(email)) {
                        return "Invalid email format";
                    }
                    
                    if (!SecurityUtils.isValidPassword(password)) {
                        return "Password must be at least 6 characters";
                    }
                    
                    if (!SecurityUtils.isValidPhoneNumber(phoneNumber)) {
                        return "Invalid phone number format";
                    }
                    
                    if (farmerName == null || farmerName.trim().isEmpty()) {
                        return "Farmer name is required";
                    }
                    
                    // Check if email already exists
                    if (userDao.checkEmailExists(email) > 0) {
                        return "Email already registered";
                    }
                    
                    // Create new user
                    newUser = new User();
                    newUser.setEmail(SecurityUtils.sanitizeInput(email.toLowerCase().trim()));
                    newUser.setPasswordHash(SecurityUtils.hashPassword(password));
                    newUser.setFarmerName(SecurityUtils.sanitizeInput(farmerName));
                    newUser.setPhoneNumber(SecurityUtils.sanitizeInput(phoneNumber));
                    newUser.setFarmLocation(SecurityUtils.sanitizeInput(farmLocation));
                    newUser.setFarmSize(farmSize);
                    newUser.setCreatedAt(System.currentTimeMillis());
                    newUser.setActive(true);
                    
                    // Insert user into database
                    long userId = userDao.insertUser(newUser);
                    newUser.setId((int) userId);
                    
                    return "SUCCESS";
                    
                } catch (Exception e) {
                    return "Registration failed: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    callback.onSuccess(newUser);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Login user with email and password
     * @param email User email
     * @param password User password
     * @param callback Authentication callback
     */
    public void loginUser(String email, String password, AuthCallback callback) {
        
        new AsyncTask<Void, Void, String>() {
            private User user;
            
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Validate input
                    if (!SecurityUtils.isValidEmail(email)) {
                        return "Invalid email format";
                    }
                    
                    if (password == null || password.trim().isEmpty()) {
                        return "Password is required";
                    }
                    
                    // Get user by email
                    User storedUser = userDao.getUserByEmail(email.toLowerCase().trim());
                    if (storedUser == null) {
                        return "User not found";
                    }
                    
                    // Verify password
                    if (!SecurityUtils.verifyPassword(password, storedUser.getPasswordHash())) {
                        return "Invalid password";
                    }
                    
                    // Check if user is active
                    if (!storedUser.isActive()) {
                        return "Account is deactivated";
                    }
                    
                    // Update last login time
                    userDao.updateLastLogin(storedUser.getId(), System.currentTimeMillis());
                    storedUser.setLastLoginAt(System.currentTimeMillis());
                    
                    this.user = storedUser;
                    return "SUCCESS";
                    
                } catch (Exception e) {
                    return "Login failed: " + e.getMessage();
                }
            }
            
            @Override
            protected void onPostExecute(String result) {
                if ("SUCCESS".equals(result)) {
                    // Create session
                    sessionManager.createLoginSession(user);
                    callback.onSuccess(user);
                } else {
                    callback.onError(result);
                }
            }
        }.execute();
    }
    
    /**
     * Logout current user
     */
    public void logoutUser() {
        sessionManager.logoutUser();
    }
    
    /**
     * Get current logged in user
     * @return SessionManager instance
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }
    
    /**
     * Check if user is logged in
     * @return True if user is logged in
     */
    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn() && !sessionManager.isSessionExpired();
    }
    
    /**
     * Get user by ID
     * @param userId User ID
     * @param callback Callback to return user
     */
    public void getUserById(int userId, AuthCallback callback) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                return userDao.getUserById(userId);
            }
            
            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("User not found");
                }
            }
        }.execute();
    }
}
