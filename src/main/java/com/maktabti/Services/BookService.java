package com.maktabti.Services;

import com.maktabti.Entities.Book;
import com.maktabti.Utils.DBUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json?title=";

    // Fetch all books from the database
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("available_copies"),
                        rs.getString("cover_url")  // Fetch the cover URL
                );
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Add a new book to the database
    public void addBook(Book book) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO books (title, author, isbn, available_copies, cover_url) VALUES (?, ?, ?, ?, ?)"
            );
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getAvailableCopies());
            ps.setString(5, book.getCoverUrl()); // Insert cover URL
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove a book from the database by its ID
    public boolean removeBook(int bookId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id = ?");
            ps.setInt(1, bookId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Borrow a book by its name (reduce available copies by 1)
    public boolean borrowBookByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT id, available_copies FROM books WHERE title = ? AND available_copies > 0"
            );
            checkStmt.setString(1, bookName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("id");
                int availableCopies = rs.getInt("available_copies");

                if (availableCopies > 0) {
                    PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE books SET available_copies = available_copies - 1 WHERE id = ?"
                    );
                    updateStmt.setInt(1, bookId);
                    int affectedRows = updateStmt.executeUpdate();
                    return affectedRows > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get the ID of a book by its name
    public int getBookIdByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM books WHERE title = ?"
            );
            ps.setString(1, bookName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the book is not found
    }

    // Return a book by its name (increase available copies by 1)
    public boolean returnBookByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT id FROM books WHERE title = ?"
            );
            checkStmt.setString(1, bookName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("id");

                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?"
                );
                updateStmt.setInt(1, bookId);
                int affectedRows = updateStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Fetch book details from Open Library API
    // Fetch book details from Open Library API and check if it exists in the local database
    public String fetchBookDetailsAndCheckAvailability(String bookTitle) {
        try {
            // Fetch book details from Open Library API
            String apiUrl = OPEN_LIBRARY_API_URL + bookTitle.replace(" ", "%20");
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject response = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray docs = response.getAsJsonArray("docs");
            if (docs != null && docs.size() > 0) {
                JsonObject book = docs.get(0).getAsJsonObject();
                String title = book.has("title") ? book.get("title").getAsString() : "Unknown Title";
                String author = book.has("author_name") ? book.getAsJsonArray("author_name").get(0).getAsString() : "Unknown Author";
                String isbn = book.has("isbn") ? book.getAsJsonArray("isbn").get(0).getAsString() : "ISBN Not Available";

                // Check if the book exists in the local database
                int availableCopies = getAvailableCopiesByTitle(title);
                if (availableCopies != -1) {
                    return String.format("Title: %s\nAuthor: %s\nISBN: %s\nAvailable Copies: %d", title, author, isbn, availableCopies);
                } else {
                    return String.format("Title: %s\nAuthor: %s\nISBN: %s\nStatus: Not available in the library.", title, author, isbn);
                }
            } else {
                return "No results found for the book.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching book information from Open Library API.";
        }
    }

    // Get available copies of a book by its title from the local database
    public int getAvailableCopiesByTitle(String bookTitle) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT available_copies FROM books WHERE title = ?"
            );
            ps.setString(1, bookTitle);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("available_copies");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the book is not found in the database
    }
}