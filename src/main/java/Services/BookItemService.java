package Services;

import Entites.BookItem;
import java.util.*;

public class BookItemService {
    private final Map<String, BookItem> bookItems = new HashMap<>(); // Stores book items by barcode

    public BookItem createBookItem(BookItem bookItem) {
        bookItems.put(bookItem.getBarcode(), bookItem);
        return bookItem;
    }

    public BookItem getBookItem(String barcode) {
        return bookItems.get(barcode);
    }

    public BookItem updateBookItem(String barcode, BookItem updatedBookItem) {
        if (bookItems.containsKey(barcode)) {
            bookItems.put(barcode, updatedBookItem);
            return updatedBookItem;
        }
        return null;
    }

    public boolean deleteBookItem(String barcode) {
        return bookItems.remove(barcode) != null;
    }
}
