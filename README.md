# Forwarder-Receiver-Library

### Prerequisites

- Java JDK version 11 or higher. If don't  have it, you can get one here [https://adoptopenjdk.net/](https://adoptopenjdk.net/)

- Your favorite IDE, for this tutorial I'm going to use VSCode

  

### Installation

**Step 1.** [Download the jar file](https://github.com/alanmoraales/forwarder-receiver-library/releases/tag/0.1) of this library.

**Step 2.** Add the jar to the classpath of your Java project. I'm gonna show you how to do it in VSCode.



###### Adding the library in VSCode

**Step 1.** Install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) in VSCode.

**Step 2.** Activate the extension by creating a new **.java** (for example *Client.java*)file in your project. You will notice that a "Java Projects" tab is now showing in your side bar. 

![image-20210115021458428](https://i.imgur.com/rdK0KXg.pngg)

**Step 3.** Create a *lib* folder in the root directory of your project and drop the jar file there. This will automatically add the library to you project's classpath.

![image-20210115021940947](https://i.imgur.com/omkqeiV.png)



###### Adding the library in VSCode - Alternative

If for any reason the first method didn't worked, you can try the manual installation.

**Step 1.** Install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) in VSCode.

**Step 2.** Open the Java projects tab, you'll see your project name inside.

**Step 3.** Open your project, you'll see a Referenced Libraries section and a plus sign, click it and find the jar file.

![alternative-installation-step2](https://i.imgur.com/eaWItik.png)



This is it for the installation part, you should be ready to use the library.



### Usage

In this section we'll build a simple example using the library to send a "Hello World" message from the client to the server.

###### The Peer class

In order to start using this library, the only thing you need to do is create a new class that extends of Peer. Notice that you also have to indicate the data type that this peer is going to send and receive. It's like when you create a ArrayList, you have to indicate the type it contains.

````java
public class YourClassName extends Peer</*Type to send*/, /*Type to receive*/> {}
````

For example, if you want a class that sends and receives String.

````java
public class YourClassName extends Peer<String, String> {}
````

Behind the scenes, the Peer class uses the Forwarder and Receiver classes to send and receive your messages.  When a new message comes, the Receiver passes the message to the Peer through a onNewMessage method, this message object is going to be cast to the class you've declared. Actually, when you extend from Peer, the compiler is gonna ask you to implement this onNewMessage method, as well as the Peer's constructor.    



###### Hello World - ClientPeer

**Step 1.** To start, create a *peers* folder and inside of it create a ClientPeer.java file.

**Step 2.** Extend the ClientPeer class from Peer, in this example we're going to send and receive String.

````java
package peers;

import forwarderReceiver.Peer;

public class ClientPeer extends Peer<String, String> {}
````

**Step 3.** The compiler it's gonna ask you to generate the Peer's constructor and implement the onNewMessage method. Notice how this method parameter it's declared with the type to receive.

````java
package peers;

import forwarderReceiver.Peer;

public class ClientPeer extends Peer<String, String> {
	public ClientPeer(String host, int port) {
    	super(host, port);
	}

	@Override
  	public void onNewMessage(String message) {
  	}
}
````

**Step 4.** Implement the ClientPeer as you need. For this example we're gonna need the follow implementation.

````java
package peers;

import java.io.IOException;

import forwarderReceiver.Peer;

public class ClientPeer extends Peer<String, String> {
  	private String serverHost;
  	private int serverPort;

	public ClientPeer(String host, int port, String serverHost, int serverPort) {
    	super(host, port);
    	this.serverHost = serverHost;
    	this.serverPort = serverPort;
	}

	@Override
  	public void onNewMessage(String message) {
    	System.out.println(message);
  	}
  
  	public void sendTestMessage(String message) throws IOException {
    	this.forwarder.sendMessage(serverHost, serverPort, this.getHost(), 	this.getPort(), message);
    } 
}
````

Notice how we're accessing the forwarder through the *this* reference. The Peer also have the capability of send a message to the all the peers that have send messages before, you can do this by using the sendMessageToAllEntries method, but we're not gonna use it in this example.



###### Hello World - Client

Now we're gonna build a client to consume the ClientPeer. I recommend you this pattern to have a better and more scalable code.

````java
import java.io.IOException;

import peers.ClientPeer;

public class Client {
  public static void main(String[] args) {
    var serverClientHost = "localhost";
    var serverPort = 1214;
    var clientPort = 1212;

    var clientPeer = new ClientPeer(serverClientHost, clientPort, serverClientHost, serverPort);

    try {
      clientPeer.listen();
      clientPeer.sendTestMessage("Hello World");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
````

The Peer constructor needs to have first two parameters (the and the port of the peer you're creating), it is necessary to create the connection and send the messages. The last two are the server configuration, it is now always necessary, but in this case we use it since we're writing the client side of our example.

Once you create your peer instance, you only need to fire the listen method, this will start the receiver with the specified host and port.



###### Hello World - ServerPeer

Now we're gonna write the ServerPeer, the process it's the same as with the ClientPeer.

**Step 1.** Create a ServerPeer.java file inside the *peers* folder. Write the following code inside.

````java
package peers;

import forwarderReceiver.Peer;

public class ServerPeer extends Peer<String, String>{
	public ServerPeer(String host, int port) {
		super(host, port);
	}

	@Override
	public void onNewMessage(String message) {
		System.out.println(message);
	}

}
````

As you can see, the server it's more simple in this case. When the message from the client arrives, we're just gonna print it.



###### Hello World - Server

To end the example, we need a Server.java file to consume the ServerPeer. You can copy and paste the code inside the file.

````java
import java.io.IOException;

import peers.ServerPeer;

public class Server {
  public static void main(String[] args) {
    var host = "localhost";
    var port = 1214;
    var serverPeer = new ServerPeer(host, port);

    try {
      serverPeer.listen();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
}
````

It's almost the same as the Client implementation but without the extra configuration.



This is it, you should end with a tree like this:

![image-20210115113433043](https://i.imgur.com/jYRcgI8.png)



###### Running the example

To run the example, simply click run in the main method using the VSCode interface. The Server should run before the Client.

![yGLUfmR7tX](https://i.imgur.com/aFQafYw.png)

After you run the Client, a "Hello World" message should appear in the server instance.

![image-20210115114914950](https://i.imgur.com/ngeqVl1.png)

And the Client will receiver a confirmation message from the server saying "message received".

![image-20210115115019685](https://i.imgur.com/oX0p6IZ.png)