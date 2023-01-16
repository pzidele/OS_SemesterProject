package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ReadThread extends Thread {
	Socket socket;
	BufferedReader reader;
	Object lock;

	ArrayList<String> jobs;

	public ReadThread(Socket socket, Object lock, ArrayList<String> jobs) {
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.lock = lock;
		this.jobs = jobs;
	}

	@Override
	public void run() {

		try {

			String input;
			while ((input = reader.readLine()) != null) {

				synchronized (lock) {
					jobs.add(input);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
