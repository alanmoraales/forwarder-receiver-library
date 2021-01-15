package forwarderReceiver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Forwarder<S> {
  private void deliver(String host, int port, Object message) throws IOException {
    var socket = new Socket(host, port);
    var out = new ObjectOutputStream(socket.getOutputStream());
    out.writeObject(message);
    out.flush();
    out.close();
    socket.close();
  }

  public void sendMessage(String receiverHost, int receiverPort, String senderHost,
                          int senderPort, S messageData) throws IOException {
    var message = new Message<>(senderHost, senderPort, messageData);
    deliver(receiverHost, receiverPort, message);
  }

  public void sendStatusMessage(String receiverHost, int receiverPort, String senderHost,
                                int senderPort, String messageData) throws IOException {
    var message = new Message<>(senderHost, senderPort, messageData);
    deliver(receiverHost, receiverPort, message);
  }
}
