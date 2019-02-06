/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.*;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		//RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		//String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try{
			// TO-DO: Bind to RMIServer
			//LocateRegistry.createRegistry(1099);
			//Naming.rebind(urlServer, iRMIServer);
			//iRMIServer = (RMIServerI) Naming.lookup(urlServer);
			//LocateRegistry.getRegistry(1099);
			//registry.lookup(stuby);
			Registry registry = LocateRegistry.getRegistry(args[0]);
			//iRMIServer = (RMIServerI) registry.lookup("rmi://localhost/RMIServer");
			RMIServerI stub = (RMIServerI) registry.lookup("rmi://localhost/RMIServer");

			int tries =0;
			// TO-DO: Attempt to send messages the specified number of times
			while(tries<numMessages)
			{
				MessageInfo msginfo = new MessageInfo(numMessages, tries);
				stub.receiveMessage(msginfo);
				tries++;
			}
		} catch (Exception e){
			System.out.println("Exception in binding to RMIServer in Client "+ e);
			//System.exit(-1);
			e.printStackTrace();
		}

		
	}
}
