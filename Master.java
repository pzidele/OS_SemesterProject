package project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Master {

	public static void main(String args[]) {
		args = new String[] { "3012" };
		String completedJobFromSlave = null;


		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);

		// lock object for thread
		Object lock = new Object();
		ArrayList<String> jobs = new ArrayList<String> ();
		ArrayList<String> completedJobs = new ArrayList<String> ();

		int slaveACount = 0;
		int slaveBCount = 0;
		String sentToSlave = null;

		try (
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

				Socket client1Socket = serverSocket.accept();
				PrintWriter client1ResponseWriter = new PrintWriter(client1Socket.getOutputStream(), true);
				BufferedReader client1RequestReader = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));

				Socket client2Socket = serverSocket.accept();
				PrintWriter client2ResponseWriter = new PrintWriter(client2Socket.getOutputStream(), true);
				BufferedReader client2RequestReader = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));

				Socket slaveASocket = serverSocket.accept();
				PrintWriter slaveAResponseWriter = new PrintWriter(slaveASocket.getOutputStream(), true);
				BufferedReader slaveARequestReader = new BufferedReader(new InputStreamReader(slaveASocket.getInputStream()));

				Socket slaveBSocket = serverSocket.accept();
				PrintWriter slaveBResponseWriter = new PrintWriter(slaveBSocket.getOutputStream(), true);
				BufferedReader slaveBRequestReader = new BufferedReader(new InputStreamReader(slaveBSocket.getInputStream()));

		)
		{

			ReadThread client1ToMaster = new ReadThread(client1Socket, lock, jobs);
			ReadThread client2ToMaster = new ReadThread(client2Socket, lock, jobs);


			// endless loop getting jobs
			System.out.println("befre while m"+jobs);
			client1ToMaster.start();
			client2ToMaster.start();

			while (true) {
				// synchronize here long enough to read bool
				boolean jobsIsEmpty;
				String stringFromClient = null;
				int size = 0;

				synchronized(lock) {
					jobsIsEmpty = jobs.isEmpty();
				}

				if (!jobsIsEmpty) {
					synchronized(lock) {
						stringFromClient = jobs.get(0);
						System.out.println("Processing job from client " + stringFromClient);
					}
					synchronized(lock) {
						size = jobs.size();
					}
				}

				try {
					Thread.sleep(7000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//				synchronized(lock) {
				if (!jobsIsEmpty) {
					//System.out.println("jobs is not empty");

					Job job = new Job(stringFromClient);
					//
					//
					for (int i = 0; i < size; i++) {
//						synchronized(lock) {
//							if (jobs.get(i).contains(job.getId())) {
//																// this is the job we wnat ot chaneg the stat of to complete
//																// get the slave who did it
//								jobs.remove(i);
//								job.setStatus("complete");
//								//jobs.get(i)
//																// and decrement count
//								client1ResponseWriter.println("Job " + job.getId() + " is complete");
//
//							}
//						}

						if (job.getStatus().equals("sending")) {
							// logic of assigning to right slave
							// and send over
							if (job.getType().equals("A")) {
								// if slave a count <= slaveb count + 5, then send to a, o/w send to b
								if((slaveACount - slaveBCount) >= 5){
									System.out.println("slave A count is bigger than slave B count");
									System.out.println("Sending job " + job.getId() + " to Slave B");
									job.setSentToSlave("B");
									slaveBCount++;
									slaveBResponseWriter.println(job.getId() + " " + job.getType() +" " + job.getClientID() +" " + job.getStatus());
								}
								else {

									System.out.println("Sending job " + job.getId() + " to Slave A");
									job.setSentToSlave("A");
									slaveACount++;
									slaveAResponseWriter.println(job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus());
								}
							}
							else {
								if ((slaveBCount - slaveACount) >= 5) {
									System.out.println("slave B count is bigger than slave A count");
									System.out.println("Sending job " + job.getId() + " to Slave A");
									job.setSentToSlave("A");
									slaveACount++;
									slaveAResponseWriter.println(job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus());
								}
								else {
									System.out.println("Sending job " + job.getId() + " to Slave B");
									job.setSentToSlave("B");
									slaveBCount++;
									slaveBResponseWriter.println(job.getId() + " " + job.getType() + " " + job.getClientID() + " " + job.getStatus());
								}

							}

						}
						else {
							// completed
							// send message to client that it's completed
							System.out.println("job is complete");

                            if(job.getClientID().equals("1")){
                                client1ResponseWriter.println("Job " + job.getId() + " is complete.");
                            }
                            else{
                                client2ResponseWriter.println("Job " + job.getId() + " is complete.");

                            }

//							if (job.getSentToSlave().equals("A")) {
//								slaveACount--;
//							}
//							else {
//								slaveBCount--;
//							}

//							synchronized(lock) {
//								for (int j = 0; j < size; j++) {
//									Job jobFromList = new Job(jobs.get(j));
//									if (jobFromList.getId().equals(job.getId())) {
//
//										jobs.remove(jobFromList);
//										System.out.println("Jobs from list was removed " +jobFromList);
//									}
//								}
//
//							}
						}

						// remove from list
						synchronized(lock) {
							jobs.remove(0);
						}
						System.out.println(jobs);
					}
					//
					//							System.out.println("Job contains complete");
					//						}
					//					}
					//


					//
					//
					//
					//				}
					//				System.out.println("after while m " +jobs);
					//
					//
					//				//				job.setType(type);
					//				//				job.setId(id);
					//				}
					//				client1ToMaster.start();
					////				client2ToMaster.start();
					//
					//
					//
					//				try {
					//					client1ToMaster.join();
					//				} catch (InterruptedException e) {
					//					// TODO Auto-generated catch block
					//					e.printStackTrace();
					//				}
					//
					//			}


					//thread here
					ReadThread slaveToMaster = new ReadThread(slaveASocket, lock, jobs);
					slaveToMaster.start();
					//MasterReadsFromSlave masterReadsFromSlave = new MasterReadsFromSlave(slaveASocket, lock, completedJobFromSlave);
					//masterReadsFromSlave.start();

					//synchronized(lock) {
						//System.out.println("Job completed from slave read thread: " + completedJobFromSlave);
					//}
					//System.out.println("Jobs after slaveToMaster read thread "+jobs);
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


