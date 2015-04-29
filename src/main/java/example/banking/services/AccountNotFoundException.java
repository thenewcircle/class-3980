package example.banking.services;

public class AccountNotFoundException extends Exception {
	private static final long serialVersionUID = 5064372603574366502L;

	public AccountNotFoundException(String message) {
		super(message);
	}
}
