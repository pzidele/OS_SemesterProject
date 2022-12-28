package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
		//this.reader = reader;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	//	this.job = job;
		this.lock = lock;
		this.jobs = jobs;
	}

	public ReadThread(Socket clientSocket) {
		this(clientSocket, null, null);
	}

	@Override 
	public void run() {
		// read: accept socket and send message, and write thread 
		// pass in socket write and writer
		// 
		try {
//			Thread.sleep(2000);
//			System.out.println("Received job "+ job );
			// dump into array list og strings iwth incoming jobs and process
		
		
		String input;
		// endless loop to accept jobs
		while ((input = reader.readLine()) != null) {
			System.out.println("reader is not null");

			// dump on queue
			//	String j = reader.readLine();
				synchronized(lock) {


					String [] string = input.split(" ");
					String status = string[3];
					if(status.equals("sending")){

//						String jobString = jobs.get(0);
//						Job job = new Job(jobString);
//						if (jobs != null && job.getStatus().equals("sending")) {
							jobs.add(input);
						}

					else{
						this.job = input;
						System.out.println(job + " is " + input);
					}
						System.out.println("from thread: " + jobs);
					}


				
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();

	}		
	}
}

