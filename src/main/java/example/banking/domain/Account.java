package example.banking.domain;

public class Account {

	private Integer id;
	private double balance;

	public Account(Integer id, double balance) {
		this.id = id;
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

}
