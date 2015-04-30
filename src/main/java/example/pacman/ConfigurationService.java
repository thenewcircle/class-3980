package example.pacman;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

public class ConfigurationService {

	private static final ConfigurationService INSTANCE = new ConfigurationService();

	private CustomerService customerService;
	private NetworkAddrService networkService;
	private DataSource regionalDS;
	private DataSource securityDS;
	private MyValidator validator;
	private DistributionEngine distributionEngine;
	private JmsService jmsService;
	private SecurityService securityService;
	private ConnectionFactory regionalCF;
	private RequestMessageLogService requestMessageLogService;
	
	private ConfigurationService() {
		regionalDS = JndiHelper.jndiLookup("/jdbc/regionalDS", DataSource.class);
		securityDS = JndiHelper.jndiLookup("/jdbc/securityDS", DataSource.class);
		regionalCF = JndiHelper.jndiLookup("/jms/regionalCF", ConnectionFactory.class);
		customerService = new CustomerService(regionalDS);
		networkService = new NetworkAddrService(regionalDS);
		requestMessageLogService = new RequestMessageLogService();
		validator = new MyValidator(customerService, networkService, requestMessageLogService);
		securityService = new SecurityService(securityDS);
		jmsService = new JmsService(regionalCF);
		distributionEngine = new DistributionEngine(customerService,networkService,securityService,jmsService);
	}

	public static ConfigurationService getInstance() {
		return INSTANCE;
	}
	
	public MyValidator getValidator() {
		return validator;
	}
	
	public DistributionEngine getDistributionEngine() {
		return distributionEngine;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public NetworkAddrService getNetworkService() {
		return networkService;
	}

	public DataSource getRegionalDS() {
		return regionalDS;
	}

	public DataSource getSecurityDS() {
		return securityDS;
	}

	public JmsService getJmsService() {
		return jmsService;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public ConnectionFactory getRegionalCF() {
		return regionalCF;
	}
	
}
