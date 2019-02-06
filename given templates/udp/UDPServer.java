/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;


import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private double startingTime;
	private double endingTime;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		close = false;

		try{
			pacSize = 65000;
			pacData = new byte[pacSize];
				
			recvSoc.setSoTimeout(30000);
			while(!close){
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac);
				String msg = new String(pac.getData(), 0, pac.getLength());
				processMessage(msg);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
			System.exit(-1);
		} catch (IOException e){
			System.out.println("IO:"+ e.getMessage());
			System.exit(-1);
		} 
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
		}
		
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
			close = true;
			//find the number of missing messages
			int msg_lost =0;
			for(int i=0; i<msg.totalMessages; i++)
			{
				if(receivedMessages[i]==0)
				{
					System.out.println("Missing message, expected "+ (i) );
					msg_lost++;
				}
			}
			System.out.println("Total number of messages sent = "+ msg.totalMessages);
			System.out.println("Total number of messages lost = "+msg_lost);

			endingTime = System.nanoTime();
			System.out.println("Time elapsed: " + ((endingTime - startingTime) / 1000000) + "(ms)");
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try{
			recvSoc = new DatagramSocket(rp);
		}catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
			System.exit(-1);
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer myserver = new UDPServer(recvPort);
		myserver.run();
	}

}
