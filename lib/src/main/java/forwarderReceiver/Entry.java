package forwarderReceiver;

public class Entry {
  private String host;
  private int port;

  public Entry(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }
}
