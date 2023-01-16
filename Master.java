package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Master {

	public static void main(String args[]) {
		args = new String[] { "30121" };

		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		Object lock = new Object();
		ArrayList<String> jobs = new ArrayList<String>();
		Job job;

		int slaveATimeToComplete = 0;
		int slaveBTimeToComplete = 0;
		String sentToSlave = null;

		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

				Socket client1Socket = serverSocket.accept();
				PrintWriter client1ResponseWriter = new PrintWriter(client1Socket.getOutputStream(), true);
				BufferedReader client1RequestReader = new BufferedReader(
						new InputStreamReader(client1Socket.getInputStream()));

				Socket client2Socket = serverSocket.accept();
				PrintWriter client2ResponseWriter = new PrintWriter(client2Socket.getOutputStream(), true);
				BufferedReader client2RequestReader = new BufferedReader(
						new InputStreamReader(client2Socket.getInputStream()));

				Socket slaveASocket = serverSocket.accept();
				PrintWriter slaveAResponseWriter = new PrintWriter(slaveASocket.getOutputStream(), true);
				BufferedReader slaveARequestReader = new BufferedReader(
						new InputStreamReader(slaveASocket.getInputStream()));

				Socket slaveBSocket = serverSocket.accept();
				PrintWriter slaveBResponseWriter = new PrintWriter(slaveBSocket.getOutputStream(), true);
				BufferedReader slaveBRequestReader = new BufferedReader(
						new InputStreamReader(slaveBSocket.getInputStream()));

		) {

			ReadThread client1ToMaster = new ReadThread(client1Socket, lock, jobs);
			ReadThread client2ToMaster = new ReadThread(client2Socket, lock, jobs);

			// endless loop getting jobs
			client1ToMaster.start();
			client2ToMaster.start();
			client1ResponseWriter.println("connected to client 1");
			client2ResponseWriter.println("connected to client 2");

			while (true) {
				boolean jobsIsEmpty;
				String stringFromClient = null;

				synchronized (lock) {
					jobsIsEmpty = jobs.isEmpty();
				}

				if (!jobsIsEmpty) {

					try {
						Thread.sleep(6000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					synchronized (lock) {
						stringFromClient = jobs.get(0);
						System.out.println("Processing job from client " + stringFromClient);
					}

					sentToSlave = String.valueOf(stringFromClient.charAt(stringFromClient.length() - 1));

					if (stringFromClient.contains("sending")) {
						job = new Job(stringFromClient);
					} else {
						job = new Job(stringFromClient, sentToSlave);
					}

					if (job.getStatus().equals("sending")) {
						// assigning job to right slave
						if (job.getType().equals("A")) {
							if ((slaveATimeToComplete - slaveBTimeToComplete) >= 8) {
								System.out.println("slave A count is bigger than slave B count");
								slaveBTimeToComplete = sendToSlaveB(slaveBTimeToComplete, slaveBResponseWriter, job);
							} else {
								slaveATimeToComplete = sendToSlaveA(slaveATimeToComplete, slaveAResponseWriter, job);

							}
						} else {
							if ((slaveBTimeToComplete - slaveATimeToComplete) >= 8) {
								System.out.println("slave B count is bigger than slave A count");
								slaveATimeToComplete = sendToSlaveA(slaveATimeToComplete, slaveAResponseWriter, job);
							} else {
								slaveBTimeToComplete = sendToSlaveB(slaveBTimeToComplete, slaveBResponseWriter, job);
							}
						}

					} else {

						System.out.println("job " + job.getId() + job.getType() + " is complete");

						if (job.getClientID().contains("1")) {
							client1ResponseWriter.println("Job " + job.getId() + " is complete.");
						} else {
							client2ResponseWriter.println("Job " + job.getId() + " is complete.");
						}

						if (job.getSentToSlave().contains("A")) {
							if (job.getType().equals(job.getSentToSlave())) {
								slaveATimeToComplete -= 2;
							} else {
								slaveATimeToComplete -= 4;
							}

						} else {

							if (job.getType().equals(job.getSentToSlave())) {
								slaveBTimeToComplete -= 2;
							} else {
								slaveBTimeToComplete -= 4;
							}
						}
					}

					synchronized (lock) {
						jobs.remove(0);
					}
					System.out.println(jobs);

					ReadThread slaveAToMaster = new ReadThread(slaveASocket, lock, jobs);
					ReadThread slaveBToMaster = new ReadThread(slaveBSocket, lock, jobs);
					slaveAToMaster.start();
					slaveBToMaster.start();
				}
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}

	}

	private static int sendToSlaveB(int slaveBTimeToComplete, PrintWriter slaveBResponseWriter, Job job) {
		job.setSentToSlave("B");
		System.out.println("Sending job " + job.getId() + " to Slave " + job.getSentToSlave());

		if (job.getType().equals(job.getSentToSlave())) {
			slaveBTimeToComplete += 2;
		} else {
			slaveBTimeToComplete += 4;
		}

		slaveBResponseWriter
				.println(job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus());
		return slaveBTimeToComplete;
	}

	private static int sendToSlaveA(int slaveATimeToComplete, PrintWriter slaveAResponseWriter, Job job) {

		job.setSentToSlave("A");
		System.out.println("Sending job " + job.getId() + " to Slave " + job.getSentToSlave());

		if (job.getType().equals(job.getSentToSlave())) {
			slaveATimeToComplete += 2;
		} else {
			slaveATimeToComplete += 4;
		}

		slaveAResponseWriter
				.println(job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus());
		return slaveATimeToComplete;
	}
}
