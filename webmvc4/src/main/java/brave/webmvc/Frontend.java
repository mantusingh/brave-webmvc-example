package brave.webmvc;

import brave.webmvc.model.Message;
import brave.webmvc.websocket.client.ChatClientEndpoint;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.xml.ws.Response;
import java.net.URI;

@EnableWebMvc
@RestController
@Configuration
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {
  @Autowired RestTemplate restTemplate;

  @RequestMapping("/") public String callBackend() {
    return restTemplate.getForObject("http://localhost:9000/api", String.class);
  }

  @RequestMapping("/sayhi")
  public ResponseEntity<Void> sayHi() throws Exception {
    final ChatClientEndpoint clientEndPoint = new ChatClientEndpoint(new URI("ws://localhost:9000/chat/Mantu"));
    clientEndPoint.addMessageHandler(new ChatClientEndpoint.MessageHandler() {
      public void handleMessage(String message) {
        Gson g = new Gson();
        Message p = g.fromJson(message, Message.class);
        System.out.println("Message from : "+p.getFrom()+", message is : "+p.getContent());

      }
    });

    while (true) {
      clientEndPoint.sendMessage(getMessage("Hi There!!"));
      Thread.sleep(30000);
    }
  }

  /**
   * Create a json representation.
   *
   * @param message
   * @return
   */
  private static String getMessage(String message) {
    Gson g = new Gson();
    Message messageObject=new Message();
    messageObject.setFrom("Mantu");
    messageObject.setTo("EveryOne");
    messageObject.setContent(message);

    return g.toJson(messageObject);
  }
}
