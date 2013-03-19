package gawekar.test;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class CamelFileCopy {
	public static void main(String args[]) throws Exception {
		CamelContext camelContext = new DefaultCamelContext();
		camelContext.addRoutes(new RouteBuilder() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.apache.camel.builder.RouteBuilder#configure()
			 */
			@Override
			public void configure() throws Exception {
				from("file:data/inbox?noop=true").choice()
						.when(header("CamelFileName").endsWith("txt"))
						.to("direct:xmlfile")
						.when(header("CamelFileName").endsWith(".xml"))
						.to("direct:txtfile");

				from("direct:xmlfile").to("file:data/outbox/xml");
				from("direct:txtfile").to("file:data/outbox/txt");

				from("direct:somemessage").to("stream:out");
			}

		});
		camelContext.start();
		// for (String name : camelContext.getComponentNames()) {
		// System.out.println("Name>>> " + name);
		// System.out.println(camelContext.getComponent(name));
		// }
		// Thread.sleep(10000);
		ProducerTemplate p = camelContext.createProducerTemplate();
		p.sendBody("direct:somemessage", "hello world");
		Thread.currentThread().join();
		camelContext.stop();
	}
}
