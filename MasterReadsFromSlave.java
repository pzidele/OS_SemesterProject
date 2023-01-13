import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterReadsFromSlave extends Thread{
    Socket socket;
    BufferedReader reader;
    Object lock;

    public String getCompletedJobFromSlave() {
        return completedJobFromSlave;
    }

    String completedJobFromSlave;

    public MasterReadsFromSlave(Socket socket,  Object lock, String completedJobFromSlave) {

        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lock = lock;
        this.completedJobFromSlave = completedJobFromSlave;
    }

    @Override
    public void run() {
        try {

            String input;
            // endless loop to accept jobs
            while ((input = reader.readLine()) != null){
                    System.out.println("from slave to master thread: " + input);

                synchronized(lock) {
                    completedJobFromSlave = input;
                    System.out.println("completedJobFromSlave: " + completedJobFromSlave);
                }
            }

            } catch (IOException ex) {
            ex.printStackTrace();
            }
        }
}

