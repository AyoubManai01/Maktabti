package com.maktabti.Services;

import com.google.gson.Gson;
import com.maktabti.Entities.User;
import com.maktabti.Services.UserService;
import spark.Request;
import spark.Response;

import java.util.List;


import static spark.Spark.*;


public class UserAPI {

    private static final UserService userService = new UserService();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        // Start the SparkJava server on port 4567
        port(4567);

        // Enable CORS (Cross-Origin Resource Sharing)
        enableCORS();

        // Define API endpoints
        setupEndpoints();
    }

    private static void setupEndpoints() {
        // Get all users
        get("/users", (req, res) -> {
            List<User> users = userService.getAllUsers();
            res.type("application/json");
            return gson.toJson(users);
        });

        // Get a user by ID
        get("/users/:id", (req, res) -> {
            int userId = Integer.parseInt(req.params(":id"));
            User user = userService.getUserById(userId);
            if (user != null) {
                res.type("application/json");
                return gson.toJson(user);
            } else {
                res.status(404); // Not Found
                return "User not found";
            }
        });

        // Add a new user
        post("/users", (req, res) -> {
            User newUser = gson.fromJson(req.body(), User.class);
            boolean isAdded = userService.addUser(newUser);
            if (isAdded) {
                res.status(201); // Created
                return "User added successfully";
            } else {
                res.status(400); // Bad Request
                return "Failed to add user";
            }
        });

        // Update a user
        put("/users/:id", (req, res) -> {
            int userId = Integer.parseInt(req.params(":id"));
            User updatedUser = gson.fromJson(req.body(), User.class);
            updatedUser.setId(userId); // Ensure the ID matches the URL parameter
            boolean isUpdated = userService.updateUser(updatedUser);
            if (isUpdated) {
                return "User updated successfully";
            } else {
                res.status(400); // Bad Request
                return "Failed to update user";
            }
        });

        // Delete a user
        delete("/users/:id", (req, res) -> {
            int userId = Integer.parseInt(req.params(":id"));
            boolean isDeleted = userService.deleteUser(userId);
            if (isDeleted) {
                return "User deleted successfully";
            } else {
                res.status(400); // Bad Request
                return "Failed to delete user";
            }
        });
    }

    private static void enableCORS() {
        options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        });
    }
}