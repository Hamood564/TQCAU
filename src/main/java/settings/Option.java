package settings;

/** Enumeration of fields used to set the options of a MALT. 
 * Each field has a set of {@link settings.Properties Properties} comprising its <code>type</code>, <code>default value</code>, and brief <code>description</code>.
 * Note that the <code>default value</code> is given as a string that represents a value of its <code>type</code>.   
 * 
 * @author leszek@tqc
 * @version 2024-01
 *
 */
public enum Option implements FieldProperties { 
	/** Properties:  no type, "", No specific meaning */	
	NOOPTION		(false, "no type", "", "No specific meaning"),

	/** Properties:  int, "", Remote Port for TCP and UDP comms - Local port for UDP listener */	
	logPort			(true, "int", "0", "Remote Port for TCP and UDP comms - Local port for UDP listener"),
	
	/** Properties:  IPADDR, 0.0.0.0, Default IP Address to log to (overridden by UDP Comms */	
	logIP			(false, "IPADDR", "0.0.0.0", "Default IP Address to log to (overridden by UDP Comms"),
	
	/** Properties:  int 0=Always, 1=High, 2=Medium, 3=Low, "0", Logging message level to send */	
	logLevel		(true, "int 0=Always, 1=High, 2=Medium, 3=Low", "0", "Logging message level to send"),
	
	/** Properties:  %+/- in, "0", Tolerance for Test Pressure */
	testpTol		(true, "%+/- int", "0", "Tolerance for Test Pressure"),
	
	/** Properties:  Pa int, "0", Abort test at this diff pressure */
	diffpAbort		(true, "Pa int", "0", "Abort test at this diff pressure"),
	
	/** Properties:  Pa or mbar int, "-20", Reference negative alarm differential pressure */	
	diffpNegAlarm	(true, "Pa or mbar int", "-20", "Reference negative alarm differential pressure"),
	
	/** Properties:  char, A, Identifier for Leak Test A */
	LT_ID			(false, "char", "A", "Identifier for Leak Test A"),
	
	/** Properties:  boolean, false, Check fixture is ready at start of test sequence */	
	startenable		(true, "boolean", "false", "Check fixture is ready at start of test sequence"),
	
	/** Properties:  int, "0", Option 1 */
	option1			(true, "int", "0", "Option 1"),
	
	/** Properties:  float, "0.0", Option 2 */	
	option2			(true, "float", "0.0", "Option 2"),
	
	/** Properties:  int, "0", Option 3 */
	option3			(true, "int", "0", "Option 3"),
	
	/** Properties:  float, "0.0", Option 4 */
	option4			(true, "float", "0.0", "Option 4"),
	
	/** Properties:  int, "0", Part ID barcode */
	option5			(true, "int", "0", "Part ID barcode"),
	
	/** Properties:  float, "0.0", Option 6 */
	option6			(true, "float", "0.0", "Option 6"),
	
	/** Properties:  char[25] highpass, Authentication to access configuration web pages */
	password1		(false, "char[25]", "highpass", "Authentication to access configuration web pages"),
	
	/** Properties:  char[25], lowpass, Authentication to access configuration web pages */
	password2		(false, "char[25]", "lowpass", "Authentication to access configuration web pages");

	private final Properties properties;  
	
	private Option(boolean numeric, String type, String defaultValue, String description) {
		this.properties = new ImmutableProperties(numeric, type, defaultValue, description);
	}
	
	/**
	 * Accessor for the Properties of an object.
	 * @return Reference to an instance of Properties.
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * String representation of an Option field.
	 * @return Returns a String comprising this field name and its properties.
	 */
	@Override
	public String toString() {
		return this.name() + ": " + this.getProperties();
	}

}