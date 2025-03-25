package settings;

/**
 * A SettingsBuilder for MALT options. The fields of the mapping are specified by the {@link Option} enumeration.
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public class OptionSettings extends SettingsBuilder<Option> {
	
	/**
	 * Constructs an OptionSettings builder for MALT Option fields.   
	 */
	public OptionSettings() {
		super(Option.class);
	}

	/** Creates an OptionSettings builder based on an encoded list of key-value pairs. This is the inverse 
	 * of {@link #build()}, and may provide a starting point for further editing of an option 
	 * settings builder.     
	 * @param kv a string containing a list of comma separated key:value pairs in the form 
	 * <code>{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code> where the keys are the ordinal
	 * values of the option fields. 
	 * If the prefix is missing then test index 0 is assumed. 
	 */
	public OptionSettings(String kv) {
		//e.g. O{k1:v1,k2:v2, ...}
		super(Option.class, kv);
	}
	
	/**
	 * A string representation of these settings.
	 * @return A comma separated list of field:value pairs enclosed in braces:   
	 * <code>{f<sub>1</sub>:v<sub>1</sub>,f<sub>2</sub>:v<sub>2</sub>,...}</code>. 
	 * In contrast to the {@link #build()} method, the fields are names from the 
	 * enumeration {@link Option} rather than their ordinal values.
	 */
	@Override
	public String toString() {
		return "Options:" + super.toString();
	}

	/**
	 * A JSON representation of the key-value mappings in this OptionSettings. 
	 * Keys are given as the names in the {@link Option} enumeration.
	 * @return A JSON representation of the key-value mappings in this OptionSettings.
	 */
	public String asJSON() {
		return "{\"options\":" + super.asJSON() + "}";
	}
	
	/**
	 * Updates this OptionSettings builder with the key-value pairs in the <code>json</code> string. 
	 * @param json A string in json format with key-value data pairs. The keys are names from 
	 * the {@link Option} enum.
	 * @return This settings builder with updated key-value pairs.
	 */
	public OptionSettings set(String json) {
		//e.g. json = {"e1":v1,"e2:"v2","e1":v1,...}
		super.set(Option.class, json);
		return this;
	}
}
