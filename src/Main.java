import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws Exception {
		
		final List<PrintStream> clients = new ArrayList<PrintStream>();
		final ServerSocket ss = new ServerSocket(0);
		
		System.out.println("Server created, port: " + ss.getLocalPort());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("Starting server");
					
					while(true){
						Socket client = ss.accept();
						PrintStream ps = new PrintStream(client.getOutputStream());
						clients.add(ps);
						
						DealWithClient dwc = new DealWithClient(client.getInputStream());
						new Thread(dwc).start();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.exit(0);
				}
			}
		}).start();
		
		
		// send messages
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						if (!clients.isEmpty()) {
							
							for (int i = 0 ; i < clients.size() ; i++) {
								try {
									final PrintStream ps = clients.get(i);
									ps.write(("["+i+"] "+"aaaaaaa\n").getBytes());
									ps.flush();	
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}
						}
						Thread.sleep(3000);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}).start();
		
	}

}




class DealWithClient extends Thread implements Runnable {
	
	private InputStream cli;
	
	public DealWithClient(InputStream pcli) {
		this.cli = pcli;
	}
	
	public void run(){
		System.out.println("New connection accepted");
		Scanner s = new Scanner(this.cli);
		while(s.hasNextLine()){
			System.out.println(s.nextLine());
		}
		s.close();
	}
	
}