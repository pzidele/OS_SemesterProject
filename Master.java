package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Master {
	static Job job;

	public static void main(String args[]) {
		args = new String[] { "3012" };


		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		
		// lock object for thread
		Object lock = new Object();
		ArrayList<String> jobs = new ArrayList<String> ();
		
		
		
	
		
		

//
//
		try (
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
				
				Socket clientSocket = serverSocket.accept();
				PrintWriter clientResponseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader clientRequestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				Socket slaveASocket = serverSocket.accept();
				PrintWriter slaveAResponseWriter = new PrintWriter(slaveASocket.getOutputStream(), true);
				BufferedReader slaveARequestReader = new BufferedReader(new InputStreamReader(slaveASocket.getInputStream()));
			
				Socket slaveBSocket = serverSocket.accept();
				PrintWriter slaveBResponseWriter = new PrintWriter(slaveBSocket.getOutputStream(), true);
				BufferedReader slaveBRequestReader = new BufferedReader(new InputStreamReader(slaveBSocket.getInputStream()));
				
				) 
		{
			
			ReadThread clientToMaster = new ReadThread(clientSocket, lock, jobs);
			// new
			clientToMaster.start();

			String stringFromClient = null;

			// endless loop getting jobs
			while (true) {
				while (!jobs.isEmpty()) {
					//if (jobs.size() > 0) {
					// et here
					// get 0 and remove  when done
					stringFromClient = jobs.get(0);
//					String type = stringFromClient.substring(stringFromClient.length()-1).toUpperCase();
//					String id = stringFromClient.substring(0,stringFromClient.length()-1).toUpperCase();

					//String status;

					job = new Job(stringFromClient);
					//send job
					//change status to started

					//while (!jobs.isEmpty()) {
					System.out.println("hi");
					if (jobs.get(0).contains("A")) {
						// send to slave a
						//slaveAResponseWriter.println(job.getType().toString() + job.getId().toString());
						slaveAResponseWriter.println(jobs.get(0));
						System.out.println("sending job to A");

					} else {
						System.out.println("job not sent");
					}
					//}

				}

//				job.setType(type);
//				job.setId(id);
				System.out.println("\"" + stringFromClient + "\" received");


//
//		
//				// depending on type, send to slave
//		
//		
				// as long as have jobs to send, want to do them

				try {
					clientToMaster.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
				
				
					
					

					//Thread masterToSlaveA = new WriteThread(hostName, slavePortNumber) ;
////					
////					Thread masterFromSlaveA = new ServerThread(slavePortNumber, job);
//				//	sendSlave(slavePortNumber, hostName);
//
//					// then send to slave a
//				}
//				else {
//					// make a connection
//					args = new String[] {"127.0.0.1", "5555"};
//
//					if (args.length != 2) {
//						System.err.println(
//								"Usage: java EchoClient <host name> <port number>");
//						System.exit(1);
//					}
//
//					String hostName2 = args[0];
//					int slavePortNumber2 = Integer.parseInt(args[1]);

				//	sendSlave(slavePortNumber, hostName);
					
//					Thread masterToSlaveB = new ClientThread(hostName2, slavePortNumber2) ;
//					
//					Thread masterFromSlaveB = new ServerThread(slavePortNumber2, job);

					// send to slave b
		//		}
				
//				Thread masterToClient = new ClientThread("127.0.0.1", 222);


				//				String response = magicEightBall.getNextAnswer();
				//				System.out.println("Responding: \"" + response + "\"");
				//				responseWriter.println(response);
				//				response = magicEightBall.getNextAnswer();
				//				System.out.println("Responding: \"" + response + "\"");

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}



	


	//

	
}
}


