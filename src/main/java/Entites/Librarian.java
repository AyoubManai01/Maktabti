package Entites;

public class Librarian extends Account {

    public Librarian(String id, String password, AccountStatus status, Person person) {
        super(id, password, status, person);
    }

    public boolean addBookItem(Book book) {
        // Simulate adding a book to the library system
        System.out.println("Book added to system: " + book.getTitle());
        return true;
    }

    public boolean blockMember(Member member) {
        member.setStatus(AccountStatus.Closed);
        System.out.println("Member " + member.getPerson().getName() + " has been blocked.");
        return true;
    }

    public boolean unblockMember(Member member) {
        member.setStatus(AccountStatus.Active);
        System.out.println("Member " + member.getPerson().getName() + " has been unblocked.");
        return true;
    }
}


