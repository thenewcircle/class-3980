package example.banking.dao;

public class AccountNotFoundException extends Exception {

	private static final long serialVersionUID = -1629232289067405862L;

	private int accountId;

	public AccountNotFoundException(int accountId) {
		super(String.format("Account #%d was not found", accountId));
		this.accountId = accountId;
	}

	public int getAccountId() {
		return accountId;
	}

}
