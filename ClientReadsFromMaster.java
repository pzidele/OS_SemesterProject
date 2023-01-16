package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReadsFromMaster extends Thread {
	Socket socket;
	BufferedReader reader;

	public ClientReadsFromMaster(Socket socket) {
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		try {
			String serverResponse;

			while ((serverResponse = reader.readLine()) != null) {
				System.out.println("Master responds: \"" + serverResponse + "\"");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
