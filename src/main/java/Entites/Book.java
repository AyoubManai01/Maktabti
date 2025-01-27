package Entites;

public class Book {
    public String ISBN;
    public String Title;
    public String Subject;
    public String Publisher;
    public String Language;
    public int NumberOfPages;

    public Book(int numberOfPages, String language, String publisher
            , String subject, String title, String ISBN) {
        NumberOfPages = numberOfPages;
        Language = language;
        Publisher = publisher;
        Subject = subject;
        Title = title;
        this.ISBN = ISBN;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public int getNumberOfPages() {
        return NumberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        NumberOfPages = numberOfPages;
    }

}
