package example.banking.domain;

import example.banking.services.InsufficientBalanceException;

public class Account {

	private Integer id;
	private double balance;
	private String owner;

	public Account(Integer id, String owner, double balance) {
		this.id = id;
		this.owner = owner;
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getOwner() {
		return owner;
	}
	
	public void withdraw(double amount) throws InsufficientBalanceException {
		if ( balance < amount) throw new InsufficientBalanceException(id,balance,amount);
		this.balance -= amount;
	}

	public void deposit(double amount) {
		this.balance += amount;
	}

}
