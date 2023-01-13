package project;
import java.io.BufferedReader;
import java.net.Socket;

public class SlaveReadsFromMaster extends Thread {
	
	Socket socket;
	Object lock;
	BufferedReader reader;
	Job job;

	public SlaveReadsFromMaster(Socket socket, Object lock) {
		this.socket = socket;
		this.lock = lock;
	}
	
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}

	@Override 
	public void run() {
	
		try {

			String input;
			// endless loop to accept jobs
			while ((input = reader.readLine()) != null) {
				synchronized(lock) {
					
					this.job = new Job(input);
					
				}

			}
		} catch (Exception e) {
			e.printStackTrace();

		}		
	}
	
}
