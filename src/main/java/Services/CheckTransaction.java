package Services;

import Entites.FineTransaction;

class CheckTransaction extends FineTransaction {
    private String bankName;
    private String checkNumber;

    public CheckTransaction(double amount, String bankName, String checkNumber) {
        super(amount);
        this.bankName = bankName;
        this.checkNumber = checkNumber;
    }

    @Override
    public boolean initiateTransaction() {
        System.out.println("Processing check transaction: " +
                "Bank = " + bankName + ", Check # = " + checkNumber + ", Amount = $" + amount);
        return amount > 0 && checkNumber != null && !checkNumber.isEmpty();
    }
}

