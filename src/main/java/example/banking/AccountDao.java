package example.banking;

public interface AccountDao {
	Account create(int id, double balance);
	Account find(int id);
}
