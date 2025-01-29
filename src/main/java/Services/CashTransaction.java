package Services;

import Entites.FineTransaction;

class CashTransaction extends FineTransaction {
    private double cashTendered;

    public CashTransaction(double amount, double cashTendered) {
        super(amount);
        this.cashTendered = cashTendered;
    }

    @Override
    public boolean initiateTransaction() {
        if (cashTendered >= amount) {
            System.out.println("Processing cash transaction: Paid $" + cashTendered + " for $" + amount);
            return true;
        }
        System.out.println("Insufficient cash. Required: $" + amount + ", Provided: $" + cashTendered);
        return false;
    }
}
