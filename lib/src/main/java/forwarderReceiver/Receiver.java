package forwarderReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.concurrent.Executors;

public class Receiver<S, R> {
  private Forwarder<S> forwarder;
  private Peer<S, R> peer;

  public Receiver(Forwarder forwarder, Peer peer) {
    this.forwarder = forwarder;
    this.peer = peer;
  }

  public void listen() throws IOException {
    var serverSocket = new ServerSocket(peer.getPort());
    System.out.println("Receiver is listening on port " + peer.getPort());

    var executor = Executors.newCachedThreadPool();
    executor.submit(() -> {
      while(true) {
        var clientSocket = serverSocket.accept();
        var in = new ObjectInputStream(clientSocket.getInputStream());

       try {
         var message = (Message<R>) in.readObject();
         
         if(message.getData().equals("message received")) {
           System.out.println(message.getData());
           continue;
         }

         peer.newEntry(message.getHost(), message.getPort());
         peer.onNewMessage(message.getData());

         var response = "message received";
         forwarder.sendStatusMessage(message.getHost(), message.getPort(), peer.getHost(), peer.getPort(), response);
       } catch (Exception e) {
         e.printStackTrace();
       }
      }
    });

  }
}
