package settings;

/**
 * Accessors for the properties of an entity. For example each MALT field has these immutable properties: type and units, 
 * a default value and a description of its purpose.
 * Note, boolean is also considered a numeric type in this context.
 *   
 * @author leszek@tqc
 * @version 2024-01
 */
public interface Properties {

	/**
	 * Returns true if this property is a number.
	 * @return true if the type of this property is a number, otherwise returns false.
	 * @since 2022-10
	 */
	public boolean isNumber();
	
	/**
	 * Gives textual information about the type and units of this entity. 
	 * @return A description of this entity's type and units.
	 */
	public String getType();

	/**
	 * Gives the default value of this entity in text form. 
	 * @return A description of this entity's default value.
	 */
	public String getDefaultValue();
	
	/**
	 * Property giving descriptive information about this entity. 
	 * @return Descriptive information about this entity.
	 */
	public String getDescription();
}

/**
 * Helper class to associate immutable Properties with each constant of the MALT enumerations.
 * @author leszek@tqc
 * @version 2022-10 
 *
 */
class ImmutableProperties implements Properties {
	private final boolean numeric;
	private final String type;
	private final String defaultValue;
	private final String description;
	
	protected ImmutableProperties(boolean numeric, String type, String defaultValue, String description) {
		this.numeric = numeric;
		this.type = type;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	
	@Override
	public boolean isNumber() {
		// TODO Auto-generated method stub
		return this.numeric;
	}
	
	@Override
	public String getType() {
		return this.type;
	}
	
	@Override
	public String getDefaultValue() {
		return this.defaultValue;
	}
	
	@Override
	public String getDescription() {
		return this.description;
	}
	
	@Override
	public String toString() {
		return 	(this.isNumber()?"number":"string") + ", " + this.type + ", " + this.defaultValue + ", " + this.description;
	}


}