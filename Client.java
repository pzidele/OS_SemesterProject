package project;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) {

		String fileName1 = "Client1Jobs";
		String fileName2 = "Client2Jobs";
		File clientFileOfJobs;
		Scanner myReader;

		args = new String[] { "127.0.0.1", "30121" };

		if (args.length != 2) {
			System.err.println("Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		Scanner keyboard = new Scanner(System.in);

		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

			System.out.println("Please enter client ID (1 or 2): ");
			String clientID = keyboard.nextLine();
			while (!(clientID.equals("1")) && !(clientID.equals("2"))) {
				System.out.println("Please enter client ID (1 or 2): ");
				clientID = keyboard.nextLine();
			}

			String jobUserInput;

			if (clientID.equals("1")) {
				clientFileOfJobs = new File(fileName1);
			} else {
				clientFileOfJobs = new File(fileName2);
			}

			myReader = new Scanner(clientFileOfJobs);

			ClientReadsFromMaster clientReadsFromMaster = new ClientReadsFromMaster(clientSocket);
			clientReadsFromMaster.start();

			while (true) {

				if (myReader.hasNextLine()) {
					System.out.println("Do you want to enter another job? ");
					String response = keyboard.nextLine();
					while (!(response.equalsIgnoreCase("yes") && (!(response.equalsIgnoreCase("no"))))) {
						System.out.println("Do you want to enter another job? ");
						response = keyboard.nextLine();
					}
					if (!(response.equalsIgnoreCase("yes"))) {
						break;
					}

					jobUserInput = myReader.nextLine();
					System.out.println("Sending job to master");
					requestWriter.println(jobUserInput + " client" + clientID + " sending "); // send request to server

					if (!(myReader.hasNext())) {
						System.out.println("All jobs sent");
					}
				}
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}

	}

}
