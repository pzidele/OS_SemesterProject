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

		int slaveACount = 0;
		int slaveBCount = 0;

		try (
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

				Socket client1Socket = serverSocket.accept();
				PrintWriter client1ResponseWriter = new PrintWriter(client1Socket.getOutputStream(), true);
				BufferedReader client1RequestReader = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));

//				Socket client2Socket = serverSocket.accept();
//				PrintWriter client2ResponseWriter = new PrintWriter(client2Socket.getOutputStream(), true);
//				BufferedReader client2RequestReader = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));

				Socket slaveASocket = serverSocket.accept();
				PrintWriter slaveAResponseWriter = new PrintWriter(slaveASocket.getOutputStream(), true);
				BufferedReader slaveARequestReader = new BufferedReader(new InputStreamReader(slaveASocket.getInputStream()));

				Socket slaveBSocket = serverSocket.accept();
				PrintWriter slaveBResponseWriter = new PrintWriter(slaveBSocket.getOutputStream(), true);
				BufferedReader slaveBRequestReader = new BufferedReader(new InputStreamReader(slaveBSocket.getInputStream()));

				) 
		{

			ReadThread client1ToMaster = new ReadThread(client1Socket, lock, jobs);
//			ReadThread client2ToMaster = new ReadThread(client2Socket, lock, jobs);

			// new



			String stringFromClient = null;

			// endless loop getting jobs

			while (true) {
				// et here

				if (!jobs.isEmpty()) {
					System.out.println("jobs is not empty");
					System.out.println(jobs);

					stringFromClient = jobs.get(0);

					Job job = new Job(stringFromClient);
					System.out.println(jobs);


					for (int i = 0; i < jobs.size(); i++) {
						if (jobs.get(i).contains(job.getId())) {
							// this is the job we wnat ot chaneg the stat of to complete
							job.setStatus("complete");
							// get the slave who did it
							
							// and decrement count
							System.out.println("JOb contains complete");
						}
					}

					if (job.getStatus().equals("sending")) {
						// logic of assigning to right slave
						// and send over
						if (job.getType().equals("A")) {
							// if slave a count <= slaveb count + 5, then send to a, o/w send to b
							System.out.println("Sending job to Slave A");
							slaveACount++;
							slaveAResponseWriter.println(job.toString());
						}
						else {
							System.out.println("Sending job to Slave B");
							slaveBCount++;
							slaveBResponseWriter.println(job.toString());	
						}
					}
					else {
						// completed
						// send message to client that it's competed
						client1ResponseWriter.println("Job " + job.getId() + " is complete.");
						System.out.println("job is complete");
					}

					// remove from list
					jobs.remove(0);



				}

				//				job.setType(type);
				//				job.setId(id);
				System.out.println("\"" + stringFromClient + "\" received");
				
				client1ToMaster.start();
//				client2ToMaster.start();
				
				System.out.println("\"" + stringFromClient + "\" received");


				try {
					client1ToMaster.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}




		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}






		//


	}
}


