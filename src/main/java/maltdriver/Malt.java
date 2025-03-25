
package maltdriver;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import settings.CalibrationSettings;
import settings.CalibrationSettings.Channel;
import settings.OptionSettings;
import settings.TestSettings;

/**
 * The <code>Malt</code> class establishes a socket connection with a MALT device for two-way
 * communication. A <code>Malt</code> object can send commands via the underlying TCP/IP connection
 * and capture the {@link Response Responses}. The methods match the commands specified in the MALT 
 * serial communication protocol. 
 * An instance of <code>Malt</code> can  only be connected to one MALT device at any one time. A 
 * single client can connect to multiple MALT devices by creating a <code>Malt</code> instance for 
 * each one. Instances that attempt to connect to the same MALT device will share the same underlying 
 * communication socket.
 * <code>Malt</code> can send commands that operate a MALT device,namely <code>reset()</code>, 
 * <code>selectTest(index)</code>, and <code>start()</code>. Test parameters, options and calibration 
 * values can be configured using {@link #settings(TestSettings) settings(XSettings)} methods. The 
 * settings and status of the MALT device can be interrogated using the various <code>requestX
 * ()</code> commands.
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public final class Malt {
	
	private MaltConnection connection;
	private String host;
	private int port;
		
	/**
	 * Creates an unconnected instance of Malt. The host is given the 0.0.0.0 non-routable IP address and the port is set to 0. The response to this constructor can be accessed using {@link #getConnectionResponse()}. A command sent from an unconnected Malt will complete with have a 'Not Connected' <code>Response</code>.   
	 */
	public Malt() {
		this.host = "0.0.0.0";
		this.port = 0;
		this.connection = MaltConnection.noConnection;
	}

	/**
	 * Creates an instance of Malt connected to the given host and port. The response to this constructor can be accessed using {@link #getConnectionResponse()}. The success of the connection can be tested using {@link #isConnected()}.
	 * @param host The IP address of the MALT device as a string.
	 * @param port The port number for the connection.
	 */
	public Malt(String host, int port) {
		this.host = host;
		this.port = port;
		this.connection = MaltConnection.getConnection(host, port);
	}

	/**
	 * The response from an attempt to connect to a MALT device. This response is generated when a Malt constructor is called or when an existing Malt attempts to {@link #connect(String, int) connect()}.  
	 * @return The response to the most recent connection or disconnection.
	 */
	public Response getConnectionResponse() {
		return connection.getConnectResponse();
	}
	
	/**
	 * 	Attempts to connect this Malt to the given host and port. Ideally this instance of Malt should not be connected to a MALT device.
	 * @param host The IP address of the MALT device as a string.
	 * @param port The port number for the connection.
	 * @return The response to this connection attempt.
	 */
	public Response connect(String host, int port) {
		//??? if (this.isConnected()) disconnect();
		this.host = host;
		this.port = port;
		this.connection = MaltConnection.getConnection(host, port);
		return connection.getConnectResponse();
//		return new Response(host + ":" + port, this.isConnected()?"connected":"no connection");
	}
	
	/**
	 * Disconnects this Malt from its host. 
	 * @return The response to this disconnection. 
	 */
	public Response disconnect() {
		//this.sendUserCommand("-"); //Empty command to flush out any latent response. 
		Response response = connection.disconnect();
		connection = MaltConnection.noConnection;
		return response;
	}
	
	/**
	 * Checks if this Malt is connected to a MALT host. 
	 * @return True if connected to a MALT, otherwise false.
	 */
	public boolean isConnected() {
		return !this.connection.isClosed() && !(this.connection instanceof NoConnection);
	}

	/**
	 * Returns the IP address associated with this Malt instance.
	 * @return Returns the IP address of the connected MALT host, or <code>0.0.0.0</code> if not connected.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the port associated with this connection.
	 * @return Returns the port used to connected to the MALT host, or <code>0</code> if not connected.
	 */
	public int getPort() {
		return port;
	}

	
	/**
	 * Sends a <i>start</i> instruction to the connected MALT. The MALT device will attempt start the currently selected test.
	 * @return A response indicating that the start command has been processed.
	 */
	public Response start() {
		return connection.sendCmd(MaltCommandFactory.start());
			
	}
	
	/**
	 * Checks if the connected MALT is running a test.
	 * @return True is a test is currently active on the associated MALT device, 
	 * otherwise returns false. See also {@link #isReady()}.
	 */
	public boolean testRunning() {
		if (!isConnected()) return false;
		//else connected
		int indicators = Integer.parseInt(this.requestStatus().getMessage());
		return (indicators & 0b0100) == 0 ;
	}
	
	/**
	 * Checks if a connected MALT is in its <i>ready</i> state. 
	 * This is true if it is not currently running a test. For a connected MALT this 
	 * is the inverse of {@link #testRunning()}.  
	 * @return True if this MALT is connected and in an idle state, ready to start a test, 
	 * otherwise false.
	 */
	public boolean isReady() {
		if (!isConnected()) return false;
		//else connected
		int indicators = Integer.parseInt(this.requestStatus().getMessage());
		return (indicators & 0b0100) != 0 ;
	}

	/**
	 * Sends a reset instruction to the connected MALT. The MALT device will terminate a test that is running and return to its <i>ready</i> state.
	 * @return A response indicating that the reset command has been processed.
	 */
	public Response reset() {
		return connection.sendCmd(MaltCommandFactory.reset());
	}

	/**
	 * Sends the index of the test to be run by the MALT.
	 * @param test Index of the test to run. A value between 0 and 15.
	 * @return A response indicating that the given test has been selected.
	 */
	public Response selectTest(int test) {
		return connection.sendCmd(MaltCommandFactory.selectTest(test));
	}
	
	/**
	 * Instructs MALT to save all settings to its on-board memory.
	 * @return A response indicating that the save command has been processed.
	 */
	public Response saveSettings() {
		return connection.sendCmd(MaltCommandFactory.saveSettings());
	}

	/**
	 * Instructs MALT to the overwrite its settings with those stored in its on-board memory.
	 * @return A response indicating that the load command has been processed.
	 */
	public Response loadSettings() {
		return connection.sendCmd(MaltCommandFactory.loadSettings());
	}
	
	/**
	 * Exports the MALT settings to a file on its memory card.  
	 * @param filename A destination file name in DOS 8.3 format. No path should be given as part of the filename. 
	 * @return A response indicating that the export command has been processed.
	 */
	public Response exportSettings(String filename) { //Should this be a java Path or File?
		return connection.sendCmd(MaltCommandFactory.exportSettings(filename));
	}

	/**
	 * imports the MALT settings from a file on its memory card.  
	 * @param filename A file name in DOS 8.3 format. No path should be given as part of the filename.
	 * @return A response indicating that the import command has been processed.
	 */
	public Response importSettings(String filename) { //Should this be a java Path or File?
		return connection.sendCmd(MaltCommandFactory.importSettings(filename));
	}
	
	/**
	 * Update test parameters as specified by TestSettings.
	 * @param ts A mapping of parameters and values to be updated for a given test.
	 * @return A response showing the codified command sent to the MALT and an acknowledgement.
	 */
	public Response settings(TestSettings ts) {
		return connection.sendCmd(MaltCommandFactory.settings(ts));
	}
	
	/**
	 * Update calibration fields as specified by CalibrationSettings.
	 * @param cs A mapping of fields and values to be updated for a given calibration channel.
	 * @return A response showing the codified command sent to the MALT and an acknowledgement.
	 */
	public Response settings(CalibrationSettings cs) {
		return connection.sendCmd(MaltCommandFactory.settings(cs));
	}

	/**
	 * Update option fields as specified by OptionSettings.
	 * @param os A mapping of fields and values to be updated.
	 * @return A response showing the codified command sent to the MALT and an acknowledgement.
	 */
	public Response settings(OptionSettings os) {
		return connection.sendCmd(MaltCommandFactory.settings(os));
	}

	/**
	 * Uses the data provided in JSON to update test, calibration, or option settings 
	 * on the connected Malt.
	 * 
	 * @param json A string in JSON format specifying the settings to update.
	 * The field names must match the names of the Calib, Option and Param enumeration, and map to values 
	 * with the expected type, {@link settings.Properties#getType() getType()}.
	 * Example: <pre>
	"""
	{
		"options":{"logLevel":2,"startenable":true},
		"calibration":[
			{"channel":"testPA","settings":{"offset":15.0}},
			{"channel":"diffPA","settings":{"gradient":1.5,"offset":-4.0}}
		],
		"testconfig":[
			{"index":2,"settings":{"idString":"Pump","filltime":5000,"measuretime":1000}},
			{"index":3,"settings":{"idString":"Cylinder head","filltime":12000,"measuretime":1500}},
			{"index":4,"settings":{"idString":"Valve","testpressure":2000}}
		]
	}
	"""
	 * </pre>
	 * "options" maps to an object of key-value settings.<br>
	 * "calibration" maps to an array containing settings for a given channel.<br>
	 * "testconfig" maps to an array containing settings for a given test index.<br>
	 * This method delegates to the settings methods: <code>OptionSettings</code>, 
	 * <code>CalibrationSettings</code> and <code>TestSettings</code>.
	 * @return An array of responses, one for each set of settings.
	 * @since 2022-12 
	 */
	public Response[] settings(String json) {
		ArrayList<Response> res = new ArrayList<>();
		//Remove multi-lines
		json = json.replaceAll("[\\r\\n\\t]", " ");
		
		//options
		//search for options key. If present it is a settings object in group(1) 
		Pattern p = Pattern.compile("\"options\".*?([{].*?[}])");
		Matcher m = p.matcher(json);
		if (m.find()) {
			OptionSettings os = new OptionSettings();
			os.set(m.group(1));
			res.add(this.settings(os));
		}

		//calibration
		//search for calibration key. If present it is an array of calibrations 
		p = Pattern.compile("\"calibration\".*?(\\[.*?\\])");
		m = p.matcher(json);
		if (m.find()) {
			//m.group(1)) is the array of calibrations
			//find settings for each channel 
			p = Pattern.compile("\"channel\"[^\"]+\"(\\w+)\".*?\"settings\".*?([{].*?[}])");
			m = p.matcher(m.group(1));
			while (m.find()) {	
				//channel=>group(1), settings=>group(2)
				CalibrationSettings cs = new CalibrationSettings(Channel.valueOf(m.group(1)));
				cs.set(m.group(2));
				res.add(this.settings(cs));
			}
		}
		
		//testconfig
		//search for testconfig key. If present it is an array of test configurations 
		p = Pattern.compile("\"testconfig\".*?(\\[.*?\\])");
		m = p.matcher(json);
		if (m.find()) {
			//m.group(1) is the array of test configurations
			//find each test configuration 
			p = Pattern.compile("\"index\"[^\\d]+(\\d+).*?\"settings\".*?([{].*?[}])");
			m = p.matcher(m.group(1));
			while (m.find()) {
				//index=>group(1), settings=>group(2)
				TestSettings ts = new TestSettings(Integer.parseInt(m.group(1)));
				ts.set(m.group(2));
				res.add(this.settings(ts));
			}
		}
		if (res.isEmpty()) res.add(this.sendUserCommand("")); //no match or invalid json
		Response[] arr = new Response[0];
		return res.toArray(arr);
	}
	
	/**
	 * A request for the current step in progress on the MALT. 
	 * @return A response with the MALT's current processing step. The step is number which can be interpreted using the MALT documentation.  
	 */
	public Response requestStep() {
		return connection.sendCmd(MaltCommandFactory.getStep());
	}

	/**
	 * Request the current state of the MALT status flags/lights. 
	 * @return A response with a 4-bit value. Each bit represents whether a flag is set(=1) or not (=0). 
	 * Bit3&rarr;Fault, Bit2&rarr;Ready, Bit1&rarr;Fail, Bit0&rarr;Pass. 
	 */
	public Response requestStatus() {
		return connection.sendCmd(MaltCommandFactory.getIndicators());
	}

	/**
	 * Request the index of current selected test.
	 * @return Response with the index of the currently selected test, 0..15.
	 */
	public Response requestTestIndex() {
		return connection.sendCmd(MaltCommandFactory.getTestIndex());
	}

	/**
	 * Request the code of the most recently completed test. The meaning of the code is available in the MALt documentation.
	 * @return the code of the most recently completed test.
	 */
	public Response requestResultCode() {
		return connection.sendCmd(MaltCommandFactory.getResult());
	}
	
	/**
	 * Request the test state of the most recently completed test. NEW ADDITION (02/07/2024)
	 * @return the test state of the test.
	 */
	public Response requestTestState() {
		return connection.sendCmd(MaltCommandFactory.getTestState());
	}
	
	/**
	 * Request the DiffPAZ of the most recently completed test. NEW ADDITION (02/07/2024)
	 * @return the DiffPAZ of the most recently completed test.
	 */
	public Response requestDiffPAZ() {
		return connection.sendCmd(MaltCommandFactory.getDiffPAZ());
	}
	
	/**
	 * Request the TestP data from the datalog buffer. NEW ADDITION (03/07/2024)
	 * @return the TestP of the datalog buffer.
	 */
	public Response requestDataTestP() {
		return connection.sendCmd(MaltCommandFactory.getDataTestP());
	}
	
	/**
	 * Request the DiffP data from the datalog buffer. NEW ADDITION (03/07/2024)
	 * @return the DiffP of the datalog buffer.
	 */
	public Response requestDataDiffP() {
		return connection.sendCmd(MaltCommandFactory.getDataDiffP());
	}
	

	/**
	 * Request the data readings stored on the MALT. The readings relate to the most recent or current test.
	 * @return The test data readings logged during the most recent test, or the current test of it is in process. The log is a sequence of triples in csv format. Each triple comprises (milliseconds, test pressure, differential pressure), where the milliseconds denotes the time since the start of the test.
	 */
	public Response requestLastData() {
		return connection.sendCmd(MaltCommandFactory.getTestData());
	}

	/**
	 * Returns a mapping of the parameters and their values for the given test. 
	 * @param index  The index of test for this request.
	 * @return Returns a response with a mapping of test parameters (k<sub>i</sub>) and their values (v<sub>i</sub>). The mapping is the form of key-value pairs T&lt;index&gt;{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}, where the keys are the ordinal codes for the test fields.
	 */
	public Response requestTestSettings(int index) {
		return connection.sendCmd(MaltCommandFactory.getParameters(index));
	}

	/**
	 * Returns a mapping of the calibration fields and their values. 
	 * @param channel  The calibration {@link settings.CalibrationSettings.Channel channel }.
	 * @return A response with a mapping of calibration parameters (k<sub>i</sub>) and their values (v<sub>i</sub>). The mapping is the form of key-value pairs T&lt;index&gt;{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}, where the keys are the ordinal codes for the calibration fields.
	 */
	public Response requestCalibrationSettings(Channel channel) {
		return connection.sendCmd(MaltCommandFactory.getCalibration(channel));
	}
	
	/**
	 * Returns a mapping of the option fields and their values. 
	 * @return A response with a mapping of option fields (k<sub>i</sub>) and their values (v<sub>i</sub>). The mapping is the form of key-value pairs T&lt;index&gt;{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}, where the keys are the ordinal codes for the option fields.
	 */
	public Response requestOptionSettings() {
		return connection.sendCmd(MaltCommandFactory.getOptions());
	}

	/**
	 * A general utility that sends a user command to the MALT. The command should be a string that conforms to the MALT serial communication specification. 
	 * @param command A string that conforms to the MALT serial communication specification.  
	 * @return A response to indicate the given command has been received and processed, or 'unknown command' if the command is not recognised by the server.
	 */
	public Response sendUserCommand(String command) {
		return connection.sendCmd(MaltCommandFactory.userCommand(command));
	}


	/**
	 * Returns the target URL (host:port) and the connection response for this Malt.
	 */
	@Override
	public String toString() {
		return "Malt ["+ this.host + ":" + this.port + " " 
				+ (this.isConnected()?"connected":"not connected") + "]" 
				+ "\r\n" + this.getConnectionResponse();
	}
	
}
