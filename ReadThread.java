package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// read thread just reads in the job string and dumps on array list which is shared

public class ReadThread extends Thread {
	// port num
	Socket socket;
	BufferedReader reader;
	Object lock;
	String job;

	public void setJob(String job) {
		this.job = job;
	}



	public String getJob() {
		return job;
	}


	//	String job;
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

	public ReadThread(Socket clientSocket) {
		this(clientSocket, null, null);
	}

	@Override 
	public void run() {
	
		try {

			String input;
			// endless loop to accept jobs
			while ((input = reader.readLine()) != null) {

				// dump on queue
				synchronized(lock) {
					jobs.add(input);	
					System.out.println("from thread: " + jobs);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}		
	}
}

