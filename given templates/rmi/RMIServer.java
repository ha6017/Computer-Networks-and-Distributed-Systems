/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.rmi.registry.*;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	private double startingTime;
	private double endingTime;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages != msg.totalMessages) { 
			receivedMessages = new int[msg.totalMessages];
			totalMessages = msg.totalMessages;
			startingTime = System.nanoTime();
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = (msg.messageNum+1);

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum==(msg.totalMessages-1))
		{
			//find the number of missing messages
			int msg_lost =0;
			for(int i=0; i<msg.totalMessages; i++)
			{
				if(receivedMessages[i]==0)
				{
					System.out.println("Missing message, expected "+ (i+1) );
					msg_lost++;
				}
			}
			System.out.println("Total number of messages sent = "+ msg.totalMessages);
			System.out.println("Total number of messages lost = "+msg_lost);

			endingTime = System.nanoTime();
			System.out.println("Time elapsed: " + ((endingTime - startingTime) / 1000000) + "(ms)");
		}

	}


	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null){
			System.setSecurityManager( new SecurityManager());
		}

		try{
			// TO-DO: Instantiate the server class
			rmis = new RMIServer();
			
			// TO-DO: Bind to RMI registry
			//LocateRegistry.createRegistry(1099);
			//Naming.rebind("rmi://localhost/RMIServer",rmis);
			String url = "rmi://localhost/RMIServer";
			rebindServer(url, rmis);

		} catch (Exception e){
			System.out.println("Binding error "+ e);
			System.exit(-1);
		}
		
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind(serverURL, server);
		} catch (Exception e){
			System.out.println("Server got an exception binding");
			//System.exit(-1);
			e.printStackTrace();
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.

	}
}
