package Services;

import Entites.FineTransaction;

class CreditCardTransaction extends FineTransaction {
    private String nameOnCard;
    private String cardNumber;

    public CreditCardTransaction(double amount, String nameOnCard, String cardNumber) {
        super(amount);
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean initiateTransaction() {
        System.out.println("Processing credit card transaction: " +
                "Cardholder = " + nameOnCard + ", Card # = " + cardNumber + ", Amount = $" + amount);
        return amount > 0 && cardNumber != null && !cardNumber.isEmpty();
    }
}
