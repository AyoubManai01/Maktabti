package Entites;

import java.util.Date;

public class Member extends Account {
    private Date dateOfMembership;
    private int totalBooksCheckedOut;

    public Member(String id, String password, AccountStatus status, Person person, Date dateOfMembership) {
        super(id, password, status, person);
        this.dateOfMembership = dateOfMembership;
        this.totalBooksCheckedOut = 0;
    }

    public Date getDateOfMembership() {
        return dateOfMembership;
    }

    public int getTotalCheckedOutBooks() {
        return totalBooksCheckedOut;
    }

    public void checkoutBook() {
        this.totalBooksCheckedOut++;
        System.out.println(person.getName() + " has checked out a book. Total: " + totalBooksCheckedOut);
    }

    public void returnBook() {
        if (totalBooksCheckedOut > 0) {
            this.totalBooksCheckedOut--;
            System.out.println(person.getName() + " has returned a book. Total: " + totalBooksCheckedOut);
        } else {
            System.out.println("No books to return.");
        }
    }
}


