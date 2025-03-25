package settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A SettingsBuilder for MALT test parameters. The fields of the mapping are specified by 
 * the {@link Param} enumeration. This builder has an additional attribute for the index of the MALT test
 * under consideration.
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public class TestSettings extends SettingsBuilder<Param> {
	
	private int testIndex;
	
	/**
	 * Constructs a TestSettings builder for MALT test parameters with test index zero.   
	 */
	public TestSettings() {
		this(0);
	}

	/**
	 * Constructs a TestSettings builder for MALT test parameters with the given test index.   
	 * @param index the index of the test associated with this SettingsBuilder. 
	 */
	public TestSettings(int index) {
		super(Param.class); 
		this.testIndex = index;
	}
	
	/** Creates a TestSettings builder based on an encoded list of key-value pairs. This is the inverse 
	 * of {@link #build()}, and may provide a starting point for further editing of a test 
	 * settings builder.     
	 * @param kv a string containing a list of comma separated key:value pairs in the form 
	 * <code>nn{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code> where the keys are the ordinal
	 * values of the test parameter fields and the prefix n is the channel number under consideration. 
	 * If the prefix is missing then test index 0 is assumed. 
	 */
	public TestSettings(String kv) {
		//e.g. T15{k1:v1,k2:v2, ...}
		//populate the settings map
		super(Param.class, kv);
		//extract and set the test index
		Pattern p = Pattern.compile("(\\d+)\\{");
		Matcher m = p.matcher(kv);
		this.testIndex = (m.find()) ? Integer.parseInt(m.group(1)) : 0;		
	}
	
	/**
	 * Associates these settings with the given test index. 
	 * @param index the index of the MALT test parameters for these settings. 
	 */
	public void setTestIndex(int index) {
		this.testIndex = index;
	}
	
	/**
	 * Returns the test index for these settings.
	 * @return the test index for these settings.
	 */
	public int getTestIndex() {return this.testIndex;}

	/**
	 * Returns a formatted string of the key-value pairs prefixed by the index of this TestSetting. 
	 * The keys are the ordinal values of the corresponding fields in the {@link Param} enumeration.
	 * @return a string of key-value pairs in the form 
	 * <code>nn{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code>, where <code>nn</code> is 
	 * the index of the MALT test. 
	 */
	@Override
	public String build() {
		return this.testIndex + super.build();
	}
		
	/**
	 * A string representation of these settings.
	 * @return A comma separated list of field:value pairs enclosed in braces, prefixed by the index of the test:  
	 * <code>nn{f<sub>1</sub>:v<sub>1</sub>,f<sub>2</sub>:v<sub>2</sub>,...}</code>. In contrast to the {@link #build()} method,
	 * the fields are names from the enumeration {@link Param} rather than their ordinal values.
	 */
	@Override
	public String toString() {
		return "Test:" + this.testIndex + super.toString();
	}
	
	/**
	 * A JSON representation of the key-value mappings in this TestSettings. 
	 * Keys are given as the names in the {@link Param} enumeration.
	 * @return A JSON representation of the key-value mappings in this TestSettings.
	 */
	public String asJSON() {
		return "{\"testconfig\":{" +
				"\"index\":" + this.testIndex +
				",\"settings\":" +super.asJSON() +
				"}}";
	}


	/**
	 * Updates this TestSettings builder with the key-value pairs in the <code>json</code> string. 
	 * @param json A string in json format with key-value data pairs. The keys are names from 
	 * the {@link Param} enum.
	 * @return This settings builder with updated key-value pairs.
	 */
	public TestSettings set(String json) {
		//e.g. json = {"e1":v1,"e2:"v2","e1":v1,...}
//		System.out.println("TS::"+json);
		super.set(Param.class, json);
		return this;
	}
}