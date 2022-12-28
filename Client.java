package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) {

		args = new String[] {"127.0.0.1", "3012"};

		if (args.length != 2) {
			System.err.println(
					"Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		Scanner keyboard = new Scanner(System.in);
		
//		Thread ClientToMaster = new ClientThread(hostName, portNumber);
//		//working on 
//		Job job = new Job("A","ddfgs");
//		Thread ClientFromMaster = new ServerThread(portNumber, job);
//		ClientFromMaster.start();



		try (
				Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter requestWriter = // stream to write text requests to server
						new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader responseReader= // stream to read text response from server
						new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream())); 
				BufferedReader stdIn = // standard input stream to get user's requests
						new BufferedReader(
								new InputStreamReader(System.in))
				) {

			System.out.println("Please enter ID and job type (A/B): ");
	
			String userInput;


			String serverResponse;
			while ((userInput = stdIn.readLine()) != null) {
				System.out.println("Sending job to master");
				requestWriter.println(userInput + " client1" + " sending"); // send request to server
				// send client id 

				serverResponse = responseReader.readLine();
				System.out.println("SERVER RESPONDS: \"" + serverResponse + "\"");
			
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
			System.exit(1);
		} 

	}

}