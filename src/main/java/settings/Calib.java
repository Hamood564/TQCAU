package settings;

/** Enumeration of fields that identify the calibration parameters of a MALT analogue to digital channel. 
 * Each field has a set of {@link settings.Properties Properties} comprising its <code>type</code>, <code>default value</code>, and brief <code>description</code>.
 * Note that the <code>default value</code> is given as a string that represents a value of its <code>type</code>.   
 * 
 * @author leszek@tqc
 * @version 2024-01
 *
 */
public enum Calib implements FieldProperties {
	/** Properties:  no type, "", No specific meaning */	
	NONE		(false,"no type", "", "No specific meaning"),	
	/** Properties:  int, "0", Max pressure value */	
	valueMax	(true, "int", "0", "Max pressure value"),
	/** Properties:  int, "0", Digital count corresponding to max pressure value */	
	countMax	(true, "int", "0", "Digital count corresponding to max pressure value"),
	/** Properties:  int, "0", Min pressure value */	
	valueMin	(true, "int", "0", "Min pressure value"),
	/** Properties:  int, "0", Digital count corresponding to min pressure value */	
	countMin	(true, "int", "0", "Digital count corresponding to min pressure value"),
	/** Properties:  float, "0.0", Gradient */	
	gradient	(true, "float", "0.0", "Gradient"),
	/** Properties:  float, "0.0", Offset */	
	offset		(true, "float", "0.0", "Offset");

	private final Properties properties;  
	
	private Calib(boolean numeric, String type, String defaultValue, String description) {
		this.properties = new ImmutableProperties(numeric, type, defaultValue, description);
	}
	
	/** Properties: 
	 * Accessor for the Properties of an object.
	 * @return Reference to an instance of Properties.
	 */
	public Properties getProperties() {
		return this.properties;
	}
	
	/**
	 * String representation of a Calib field.
	 * @return Returns a String comprising this field name and its properties.
	 */
	@Override
	public String toString() {
		return this.name() + ": " + this.getProperties();
	}
	
}