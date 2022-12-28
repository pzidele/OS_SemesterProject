package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SlaveA  {
	public static void main(String args[]) {
		// connection:  we are the master accepting from the client "master"
		/// port num 4444
		args = new String[] { "3012" };

		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		

		try (
				Socket clientSocket = new Socket("127.0.0.1", portNumber);
				PrintWriter requestWriter = // stream to write text requests to server
						new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader responseReader= // stream to read text response from server
						new BufferedReader(
								new InputStreamReader(clientSocket.getInputStream())); 
				BufferedReader stdIn = // standard input stream to get user's requests
						new BufferedReader(
								new InputStreamReader(System.in))
		) 
		
		{
			
			ReadThread masterToSlave = new ReadThread(clientSocket);
			masterToSlave.start();

			while(true){
				// read from master
				// sleep/change status / write
				String usersRequest;
				while ((usersRequest = responseReader.readLine()) != null) {
					System.out.println("Receiving job from master");

					Job job = new Job(usersRequest);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					job.setStatus("complete");
					requestWriter.println(job.toString());
				//	requestWriter.println(job.getId() + " is done by Slave A");
					// make sure master receives this
					// search for this job id and set status to done!
				}
		
		}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}


	}

}
