package example.banking.services;

public class InsufficientBalanceException extends Exception {

	private static final long serialVersionUID = -53258166007868592L;

	private int accountId;
	private double balance;
	private double withdrawAmount;

	public InsufficientBalanceException(int accountId, double balance,
			double withdrawAmount) {
		super(String.format(
				"Unable to withdraw %s from Account id=%s, balance=%s",
				withdrawAmount, accountId, balance));
		this.accountId = accountId;
		this.balance = balance;
		this.withdrawAmount = withdrawAmount;
	}

	public int getAccountId() {
		return accountId;
	}

	public double getBalance() {
		return this.balance;
	}

	public double getWithdrawAmount() {
		return this.withdrawAmount;
	}

}
