package forwarderReceiver;

import java.io.Serializable;

public class Message<T> implements Serializable {
  private String host;
  private int port;
  private T data;

  public Message(String host, int port, T data) {
    this.host = host;
    this.port = port;
    this.data = data;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public T getData() {
    return data;
  }
}
