package forwarderReceiver;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Peer<S, R> {
  private String host;
  private int port;
  private ArrayList<Entry> entries;
  protected Forwarder<S> forwarder;
  private Receiver<S, R> receiver;

  public Peer(String host, int port) {
    this.host = host;
    this.port = port;
    this.forwarder = new Forwarder<>();
    this.entries = new ArrayList<>();

    this.receiver = new Receiver<>(this.forwarder, this);
  }

  public void listen() throws IOException {
    this.receiver.listen();
  }

  private boolean entryExists(String host, int port) {
    for(var entry : entries) {
      if(entry.getHost().equals(host) && entry.getPort() == port) {
        return true;
      }
    }
    return false;
  }

  public void newEntry(String host, int port) {
    if(!entryExists(host, port)) {
      var newEntry = new Entry(host, port);
      this.entries.add(newEntry);
    }
  }

  public void sendMessageToAllEntries(S messageData) throws IOException {
    for(var entry : entries) {
      forwarder.sendMessage(entry.getHost(), entry.getPort(), this.getHost(), this.getPort(), messageData);
    }
  }

  public abstract void onNewMessage(R messageData);

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}
