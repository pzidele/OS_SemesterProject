import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class WriteThread extends Thread { 
	
	private String hostName;
	private int slavePortNumber;
	
	public WriteThread (String hostName, int slavePortNum) {
		this.hostName = hostName;
		this.slavePortNumber = slavePortNum;
		
	}
	
	@Override
	
	public void run() {
	try (
			Socket masterSocket = new Socket(hostName, slavePortNumber);
			PrintWriter requestWriter = // stream to write text requests to server
					new PrintWriter(masterSocket.getOutputStream(), true);
			BufferedReader responseReader= // stream to read text response from server
					new BufferedReader(
							new InputStreamReader(masterSocket.getInputStream())); 
			BufferedReader stdIn = // standard input stream to get user's requests
					new BufferedReader(
							new InputStreamReader(System.in))
			) {
		String userInput;

		String serverResponse;
		while ((userInput = stdIn.readLine()) != null) {

			requestWriter.println(userInput); // send request to server

			serverResponse = responseReader.readLine();
			System.out.println("SERVER RESPONDS: \"" + serverResponse + "\"");
		}
	} catch (UnknownHostException e) {
		System.err.println("Don't know about host " + hostName);
		System.exit(1);
	} catch (IOException e) {
		System.err.println("Couldn't get I/O for the connection to " +
				hostName);
		System.exit(1);
	} 
	}

}
	


