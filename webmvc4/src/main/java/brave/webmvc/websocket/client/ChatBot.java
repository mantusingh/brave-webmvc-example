package brave.webmvc.websocket.client;


import brave.webmvc.model.Message;
import com.google.gson.Gson;

import java.io.StringReader;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * ChatBot
 * @author Mantu
 */
public class ChatBot {

    /**
     * main
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
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
