package maltdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

class MaltConnection {

	//==================================================================
	@SuppressWarnings("serial")
	class MaltConnectionException extends Exception {
		public MaltConnectionException() {
			super("Malt Connection Exception");
		}
		
		public MaltConnectionException(String message) {
			super(message);
		}		
	}
	
	//==================================================================
	
	/* Singleton null connection */
	static MaltConnection noConnection = new NoConnection();
	
	//==================================================================
	
	/* Pool of 'singleton' connections - unique connection per Malt.
	 * Uses host:port string as a key. */  
	private static HashMap<String, MaltConnection> conmap = new HashMap<>();
	protected Response connectResponse; 
	
	//instance variables
	protected String key;				//host:port - key used in connection map
	private Socket socket;
	private PrintWriter writer; 	//output to serve
	private BufferedReader reader;  //input from server
	
	//Singleton connection instance.
	//private static MaltConnection connection = null;

	/* Read and return multi-line response from server. */
	private String readlines() throws IOException {
		StringBuilder sb = new StringBuilder();
    	do {
        	sb.append(reader.readLine()).append('\n');	//receive response from server
//            System.out.println(response);  			//output on client
    	} while (reader.ready());
    	sb.deleteCharAt(sb.length()-1); //remove last <newline>
    	return sb.toString();
	}
	
	protected MaltConnection() {
		this.key = "0.0.0.0:0";
		this.socket = new Socket(); //unconnected socket
		this.writer = new PrintWriter(System.out);
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.connectResponse = new Response(this.key, "connect", "not connected");
	}
	
	private MaltConnection(String host, int port) throws MaltConnectionException {

        this.key = host + ":" + port;
        try {
        	this.socket = new Socket(host, port);
	        this.writer = new PrintWriter(this.socket.getOutputStream(), true);  //NB true=flush buffer on println
	        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	
	        //Give time to make connection
	       	do {
	       		try {Thread.sleep(100);} catch (Exception e) {}
	       	} while (!reader.ready()); 
	       	           	
	       	//read and output header from server 
//	       	System.out.println(readlines());
			connectResponse = new Response(this.key, "Malt Connection " + this.key, readlines());
	       	
	    } catch (UnknownHostException ex) {
	        throw new MaltConnectionException("Server not found: " + ex.getMessage() );
	    } catch (IOException ex) {
	        throw new MaltConnectionException("I/O error " + ex.getMessage() );
	    }	
	}
	
	/** Returns the malt connection instance.*/
	public static MaltConnection getConnection(String host, int port) {
		MaltConnection connection = noConnection;
		
		String key = host+":"+ port;
		if (conmap.containsKey(key)) {
			connection = conmap.get(key);
		}
		else {
			try {
				connection = new MaltConnection(host, port);
				conmap.put(key, connection);
			} catch (MaltConnectionException ex) {
				connection = new NoConnection(new Response(key, "Malt Connection " + key, ex.getMessage()));
//				connection.connectResponse = new Response("Malt Connection " + key, ex.getMessage());
			}
		}
		return connection;
	}
	
	public Response getConnectResponse() {
		return connectResponse;
	}

	public boolean isClosed() {
		return this.socket.isClosed();
	}
	
	public Response disconnect() {
		conmap.remove(this.key); //removes from connection map if present, otherwise does nothing.
		if (!this.socket.isClosed()) {
	        writer.println('Q');		//send to close request to sever.
	        String response = "disconnect error";
			try {
				response = readlines();
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			connectResponse = new Response(this.key, "Q", response);
			return connectResponse; //new Response(this.key,"Q", response);
		} else
			return noConnection.disconnect();
	}
	
	public Response sendCmd(Command command) {
        writer.println(command.getCommand());		//send to sever. Could use write() and flush()
		String response = "failed";
		if (!this.socket.isClosed()){
			try {
				response = readlines();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Response(this.key, command.getCommand(), response);
	}
	
}

/* Singleton null connection */
class NoConnection extends MaltConnection {
	
	protected NoConnection() {
		super();
	}
	
	protected NoConnection(Response response) {
		super();
		this.connectResponse = response;
	}
	
	public Response disconnect() {
		return new Response(this.key, "Q", "Not connected");
	}
	
	public Response sendCmd(Command command) {
		return new Response(this.key, command.getCommand(), "Not Connected");
	}
};
//==================================================================