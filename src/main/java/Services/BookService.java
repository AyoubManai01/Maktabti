package Services;

import Entites.Book;
import Entites.BookItem;
import Entites.Catalog;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    private final Map<String, Book> books = new HashMap<>(); // Stores books by ISBN

    public Book createBook(Book book) {
        books.put(book.getISBN(), book);
        return book;
    }

    public Book getBook(String ISBN) {
        return books.get(ISBN);
    }

    public Book updateBook(String ISBN, Book updatedBook) {
        if (books.containsKey(ISBN)) {
            books.put(ISBN, updatedBook);
            return updatedBook;
        }
        return null;
    }

    public boolean deleteBook(String ISBN) {
        return books.remove(ISBN) != null;
    }
}