package Entites;

import java.util.Date;

class FineTransaction extends Fines {
    private Date creationDate;

    public FineTransaction(double amount, Date creationDate) {
        super(amount);
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean initiateTransaction() {

        return true;
    }
}
