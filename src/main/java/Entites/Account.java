package Entites;

public abstract class Account {
    protected String id;
    protected String password;
    protected AccountStatus status;
    protected Person person;

    public Account(String id, String password, AccountStatus status, Person person) {
        this.id = id;
        this.password = password;
        this.status = status;
        this.person = person;
    }

    public boolean resetPassword(String newPassword) {
        if (newPassword.length() >= 8) {
            this.password = newPassword;
            System.out.println("Password reset successfully.");
            return true;
        }
        System.out.println("Password reset failed. Minimum length: 8 characters.");
        return false;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }
}


