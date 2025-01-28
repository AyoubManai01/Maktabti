package Entites;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Catalog implements Search {
    public Date creationDate;
    public int totalBooks;
    public Map<String, List> bookTitles;
    public Map<String, List> bookAuthors;
    public Map<String, List> bookSubjects;
    public Map<Date, List> bookPublicationDates;

    public Catalog(Map<String, List> bookAuthors, Date creationDate,
                   int totalBooks, Map<String, List> bookTitles,
                   Map<String, List> bookSubjects, Map<Date, List> bookPublicationDates) {
        this.bookAuthors = bookAuthors;
        this.creationDate = creationDate;
        this.totalBooks = totalBooks;
        this.bookTitles = bookTitles;
        this.bookSubjects = bookSubjects;
        this.bookPublicationDates = bookPublicationDates;
    }

    public Map<String, List> getBookSubjects() {
        return bookSubjects;
    }

    public void setBookSubjects(Map<String, List> bookSubjects) {
        this.bookSubjects = bookSubjects;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Map<String, List> getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(Map<String, List> bookTitles) {
        this.bookTitles = bookTitles;
    }

    public Map<String, List> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(Map<String, List> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public Map<Date, List> getBookPublicationDates() {
        return bookPublicationDates;
    }

    public void setBookPublicationDates(Map<Date, List> bookPublicationDates) {
        this.bookPublicationDates = bookPublicationDates;
    }

    public Map<String, List> searchByTitle(String title){
        return bookTitles;
        // TO DO
    };
    public Map<String, List> searchByAuthors(String title){
        return bookAuthors;
        // TO DO
    };
    public Map<String, List> searchBySubject(String title){
        return bookSubjects;
        // TO DO
    };
    public Map<Date, List> searchByPubDate(Date datetime){
        return bookPublicationDates;
        // TO DO
    };
}
