package example.pacman;

@SuppressWarnings("unused")
public class DistributionEngine {

	private CustomerService customerService;
	private NetworkAddrService networkService;
	private SecurityService securityService;
	private JmsService jmsService;

	public DistributionEngine(CustomerService customerService,
			NetworkAddrService networkService, SecurityService securityService,
			JmsService jmsService) {
		this.customerService = customerService;
		this.networkService = networkService;
		this.securityService = securityService;
		this.jmsService = jmsService;
	}

	public void buildMessages(RequestMessage request) {
		jmsService.sendMessage("verizon", "request1");
		jmsService.sendMessage("bell-atlantic", "request2");
		jmsService.sendMessage("tmobile", "request3");
		jmsService.sendMessage("siera-telephone", "request4");
	}

}
