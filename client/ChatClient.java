// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }
  
  private void readCommand(String command, String message) throws IOException {
	  	
	  	ChatClient client;
	  	
	    if (command.equals("#quit")) {
	    	quit();
	    }
	    else if (command.equals("#logoff")) {
	    	closeConnection();
	    }
	    else if (command.equals("#login")) {
	    	if (isConnected()) {
	    		System.out.println("You are already connected");
	    	}
	    	else {
	    	openConnection();
	    	}
	    }
	    else if (command.equals("#gethost")) {
	    	System.out.println(getHost());
	    }
	    else if (command.equals("#getport")) {
	    	System.out.println(getPort());
	    }
	    else if (command.equals("#sethost")) {
	    	if (!isConnected() && message != null) {
	    		setHost(message);
	    	}
	    	else {
	    		System.out.println("You need to be disconnected to change host");
	    	}		
	    }
	    else if (command.equals("setport")) {
	    	if (!isConnected() && message != null) {
	    		setPort(Integer.parseInt(message));
	    	}
	    	else {
	    		System.out.println("You need to be disconnected to change port");
	    	}
	    }
  }
  
  private void getCommand(String message) throws IOException {
	  String command = message;
	  if (command.contains(" ")) {
		  command = command.split(" ")[0];
		  readCommand(command, command.split(" ")[1]);
		  message = message.substring(message.indexOf(" " + 1));
		  if (message.contains("#")) {
				getCommand(message.substring(message.indexOf("#")));
			}
	  }else {
		  readCommand(command,null);
	  }
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message) throws IOException
  {
	
    try
    {
      sendToServer(message);
      getCommand(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }    
  }
  
  
  protected void connectionClosed() {
	  System.out.println("Connection is now closed");
	}
  
  protected void connectionException(Exception exception) {
	  System.out.println("Server is no longer connected. " + " Connection is now closed.");
	  quit();
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
