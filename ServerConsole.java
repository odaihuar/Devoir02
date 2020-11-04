import common.ChatIF;
import java.io.*;
import java.util.Scanner;

import client.ChatClient;

public class ServerConsole implements ChatIF {
	
	EchoServer server;
	
	Scanner fromConsole;
	
	final public static int DEFAULT_PORT = 5555;
	
	public ServerConsole(String host, int port) {
		server = new EchoServer(port);
		fromConsole = new Scanner(System.in);
	}
	
	/**
	 ** This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 * @param message The string to be displayed.
	 */	
	public void display(String message) {
		System.out.println("SERVER MSG>" + message);
	}
	/**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
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
	private void readCommand(String command, String message) throws IOException {
	  	
	  	ServerConsole server;
	  	
	  	//comprends pas quoi faire ici et pour le reste des commandes
	  	
	    if (command.equals("#quit")) {
	    	close();
	    }
	    else if (command.equals("#stop")) {
	    	serverStopped();
	    }
	    else if (command.equals("#close")){
	    	serverStopped();
	    	stopListening();
	    }
	    else if (command.equals("#getport")){
	    	System.out.println(getPort());
	    }
	    else if (command.equals("#setport")) {
	    	if (!isConnected() && message != null) {
	    		setPort(Integer.parseInt(message));
	    	}
	    	else {
	    		System.out.println("You need to be disconnected to change port");
	    	}
	    }
  }
	

	public void accept() 
	  {
	      try
	            {

	              String message;

	              while (true) 
	              {
	                message = fromConsole.nextLine();
	                if(message.contains("#")) {
	                    server.getCommand(message);
	                }
	                server.sendToAllClients("SERVER MSG>"+message);
	                display(message);
	              }
	            } 
	            catch (Exception ex) 
	            {
	              System.out.println
	                ("Unexpected error while reading from console!!!");
	            }

	    }
	public static void main(String[] args) {
		String host = "";
	    int port;
	    
	    try
	    { 
	    	port = Integer.parseInt(args[1]);
	    }
	    catch(ArrayIndexOutOfBoundsException t)
	    {
	    	//System.out.println("SUS");
	    	port = DEFAULT_PORT;
	    }

	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	      //System.out.println("GOSU");
	    }
	    ClientConsole chat= new ClientConsole(host, port);
	    chat.accept();  //Wait for console data
	}

}
