package settings;

/**
 * Interface to retrieve the properties defined in {@link settings.Properties}. 
 * Each MALT field can access its immutable properties via this interface.
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public interface FieldProperties {
	
	/**
	 * Accessor for the Properties of an object.
	 * @return Reference to an instance of Properties.
	 */
	public Properties getProperties();
}

