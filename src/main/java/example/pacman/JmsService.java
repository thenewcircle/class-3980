package example.pacman;

import javax.jms.ConnectionFactory;

public class JmsService {

	private ConnectionFactory connectionFactory;

	public JmsService(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public void sendMessage(String customer, String message) {
		// use connection factory to send messages
	}

}
