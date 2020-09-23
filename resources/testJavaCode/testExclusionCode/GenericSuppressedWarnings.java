import java.io.StringReader;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonReader;

@SuppressWarnings("JmsConsumer Warning Disabled")
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "/jms/queue/HsportsQueue"),
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") }, mappedName = "/jms/queue/HsportsQueue")
public class JmsConsumer implements MessageListener {

	public JmsConsumer() {
	}

	public void onMessage(Message message) {
		System.out.println("From JMS Consumer MDB:");

		try {

			String json = message.getBody(String.class);

			try (JsonReader jsonReader = Json.createReader(new StringReader(json))) {

				JsonObject jsonObject = jsonReader.readObject();
				
				JsonPointer pointer = Json.createPointer("/items/0/quantity");
				
				long quantity = Long.parseLong(pointer.getValue(jsonObject).toString());
				
				System.out.println("Quantity: " + quantity);
				
				jsonObject = Json.createPatchBuilder()
							.add("/promo", "double")
							.replace("/items/0/quantity", Long.toString(quantity * 2L))
							.remove("/storeName").build().apply(jsonObject);
				
				System.out.println(jsonObject);
			}

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
