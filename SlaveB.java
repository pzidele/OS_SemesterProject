package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SlaveB {
	public static void main(String args[]) {
		args = new String[] { "30121" };

		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		try (Socket clientSocket = new Socket("127.0.0.1", portNumber);
				PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader responseReader =

						new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			String usersRequest;

			while ((usersRequest = responseReader.readLine()) != null) {
				System.out.println("Slave B receiving " + usersRequest + " from master");

				String sentToSlave = "B";

				Job job = new Job(usersRequest, sentToSlave);
				if (job.getType() == "B") {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

				job.setStatus("complete");
				System.out.println("Job status: " + job.getStatus());
				requestWriter.println(
						job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus() + " B"); // server
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}

	}

}
