package com.maktabti.Services;

import com.maktabti.Entities.Book;
import com.maktabti.Utils.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            // Make sure your "books" table has a column named "cover_url"
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

    public boolean borrowBookByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            // Check if the book is available
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT id, available_copies FROM books WHERE title = ? AND available_copies > 0"
            );
            checkStmt.setString(1, bookName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("id");
                int availableCopies = rs.getInt("available_copies");

                if (availableCopies > 0) {
                    // Update the available copies
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

    public int getBookIdByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM books WHERE title = ?"
            );
            ps.setString(1, bookName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Return the book ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the book is not found
    }


    public boolean returnBookByName(String bookName) {
        try (Connection conn = DBUtil.getConnection()) {
            // Get the book ID
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT id FROM books WHERE title = ?"
            );
            checkStmt.setString(1, bookName);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("id");

                // Update the available copies
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


}
