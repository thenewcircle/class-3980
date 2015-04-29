package example.banking.services;

public class AccountNotFoundException extends Exception {

	private static final long serialVersionUID = 2826695155790834813L;

	public AccountNotFoundException(int accountId) {
		super(String.format("Account #%d was not found", accountId));
	}

}
