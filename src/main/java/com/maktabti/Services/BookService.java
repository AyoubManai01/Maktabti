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

    public boolean borrowBook(int bookId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE books SET available_copies = available_copies - 1 WHERE id = ? AND available_copies > 0"
            );
            ps.setInt(1, bookId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean returnBook(int bookId) {
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE books SET available_copies = available_copies + 1 WHERE id = ?"
            );
            ps.setInt(1, bookId);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
