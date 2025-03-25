package maltdriver;

import java.time.Instant;

import settings.CalibrationSettings;
import settings.OptionSettings;
import settings.TestSettings;

/**
 * This class is used by the MALT driver client to encapsulate the response received to a command sent to a MALT.
 * A <code>Response</code> comprises the source of the response (IP of the MALT), a time stamp, the command that 
 * was issued and the response message received from the MALT. 
 * The format of commands and corresponding response messages are detailed in MALT communication protocol documentation. 
 * The time stamp is the instant that a <code>Response</code> object is created by the MALT client.  
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public class Response {

	private String source;	//to do: upgrade to InetAddress
	private Instant timestamp;
	private String command; //should be a command?
	private String message;
	
	/**
	 * An empty command and response issued at the instant it was created. The source is the meta-address 0.0.0.0 and port zero.
	 */
	protected Response() {
		this("0.0.0.0:0", Instant.now(), "", "");
	}

	/**
	 * Creates a response time stamped with the current time.  
	 * @param source IP address and port number of the MALT connection in the format <code>hh.hh.hh.hh:nnnn</code>.
	 * @param command The MALT command as specified in the TCP protocol.
	 * @param message The response returned by the MALT after receiving the command. 
	 */
	protected Response(String source, String command, String message) {
		this(source, Instant.now(), command, message);
	}
	
	/**
	 * Creates a response with the given parameters.
	 * @param source IP address and port number of the MALT connection in the format <code>hh.hh.hh.hh:nnnn</code>.
	 * @param timestamp Instant that this response was created on the client.
	 * @param command The MALT command as specified in the TCP protocol.
	 * @param message The response returned by the MALT after receiving the command. 
	 */
	protected Response(String source, Instant timestamp, String command, String message) {
		this.source = source;
		this.timestamp = timestamp;
		this.command = command;
		this.message = message;
	}

	/**
	 * Returns the IP and communication port of the device issuing the response.
	 * @return The host and post as a string in the format host:port. For example <code>192.168.100.200:5000</code>.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source for the response.
	 * @param source A string identifying the entity that issues this response.
	 */
	protected void setSource(String source) {
		this.source = source;
	}

	/**
	 * The time that the response was created.
	 * @return The time that this <code>Response</code> was created. The time stamp is an instance of <code>java.time.Instant</code>. This is the instant the response is received by the client as opposed to the time the message was sent.
	 */
	public Instant getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the time that this response is made. 
	 * @param timestamp The time that this response is created.
	 */
	protected void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Return the command that was sent to the MALT.
	 * @return The string that was sent to the MALT server. Command strings and formats are specified in the MALT protocol documentation.
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * A record of the command to which this is a reponse.
	 * @param command The command that was issued to elicit this response.
	 */
	protected void setCommand(String command) {
		this.command = command;
	}

	/**
	 * Returns the message that was received in response to a command. 
	 * @return The message that was received in response to a command. This may be multi-line message.
	 */
	 public String getMessage() {
		return message;
	}

	/**
	 * Set the response message.  
	 * @param message The substantive message of this response.
	 */
	 protected void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns this Response in JSON format. Namely, a string denoting a JSON object with four fields: 
	 * <code>"source", "timestamp", "command", "message"</code> and their respective values.  
	 * @return A Response object as a string in JSON format. This is be multi-line message. For example:
	 * <pre>
	 * {"response":{
	 * 		"source":"192.168.0.220:5000",
	 * 		"timestamp":"2022-10-25T19:18:13.266279400Z",
	 * 		"command":"O{8:1}",
	 * 		"message":"Options"
	 * 		}
	 * }
	 * </pre> 
	 */
	public String asJSON() {
		String jsonMsg = "\"" + this.message +"\"";
		if (this.command.length()>0 && this.command.charAt(0)=='?')
			switch (this.getCommand().charAt(1)) {
			case 'O' : jsonMsg = new OptionSettings(this.message).asJSON(); break;
			case 'C' : jsonMsg = new CalibrationSettings(this.message).asJSON(); break;
			case 'P' : jsonMsg = new TestSettings(this.message).asJSON(); break;
			default :
				
			}
		
		return "{\"response\":{" +
				"\n\t\"source\":\"" + this.source + "\"," + 
		        "\n\t\"timestamp\":\"" + this.timestamp + "\"," +
		        "\n\t\"command\":\"" + this.command.replaceAll("\"", "\\\\\"").replaceAll("\\s", "\\\\s") + "\"," +
		        "\n\t\"message\":" + jsonMsg + "\n\t}"
		        +"\n}";
	}
	
	public String asJSON2() {
		String jsonMsg = "" + this.message +"";
		if (this.command.length()>0 && this.command.charAt(0)=='?')
			switch (this.getCommand().charAt(1)) {
			case 'O' : jsonMsg = new OptionSettings(this.message).asJSON(); break;
			case 'C' : jsonMsg = new CalibrationSettings(this.message).asJSON(); break;
			case 'P' : jsonMsg = new TestSettings(this.message).asJSON(); break;
			default :
				
			}
		
		return "{\"response\":{" +
				"\n\t\"source\":\"" + this.source + "\"," + 
		        "\n\t\"timestamp\":\"" + this.timestamp + "\"," +
		        "\n\t\"command\":\"" + this.command.replaceAll("\"", "\\\\\"").replaceAll("\\s", "\\\\s") + "\"," +
		        "\n\t\"message\":" + jsonMsg + "\n\t}"
		        +"\n}";
		
	}
	
	 
	/**
	 * A string representation of this <code>Response</code>. The format  of the response is: <code> host:port[timestamp in ISO-8601 format, command, response]</code>, e.g. <code>192.168.100.200:5000[2021-03-10T11:22:10.376363200Z, MS, Malt Started]</code>
	 */
	@Override
	public String toString() {
		return source + "["+timestamp + ", " + command + ", " + message + "]";
	}
	
	public String simpleString() {
		return message;
		
	}

	
	
}
