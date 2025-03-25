package settings;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * <p>SettingsBuilder is an generic class with the core methods required to build a MALT TCP command comprising 
 * key-value pairs in the form: <code>{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code>.
 * The keys are ordinal values of the enumeration <code>&lt;T&gt;</code> representing the MALT fields.</p>
 * 
 * <p>Subclasses this abstract class specify the enumeration that defines the domain of the mapping, and 
 * elaborate the {@link #build()} method.</p>
 * 
 * <p>The <code>set()</code> function is overloaded so that fields can accept values of a primitive 
 * type (int, double, boolean) or a string.</p>
 * 
 * <p>The interface is written using the <i>fluent</i> style enabling method chaining to build a 
 * MALT TCP command comprising more than one field.</p>
 * 
 * @author leszek@tqc
 * @version 2024-01
 * 
 * @param <T> the enumeration that constrains the keys of key:value pairs. 
 */
public abstract class SettingsBuilder<T extends Enum<T>> implements Iterable<T>{

	/* Enum maps are represented internally as arrays. 
	 * This representation is compact and efficient. */
	private EnumMap<T,Object> settings;
	
	/** Constructs a settings builder with no key-value pairs.
	 * @param prototype The class of the enum that defines the keys used in this SettingsBuilder
	 */
	protected SettingsBuilder(Class<T> prototype) {
		this.settings = new EnumMap<T,Object>(prototype);
	}

	/** Creates a SettingsBuilder based on an encoded list of key-value pairs. This is the inverse 
	 * of {@link build()}, and may provide a starting point for further editing of the 
	 * settings builder.     
	 * @param prototype The underlying enumeration for which the key-values pairs are defined.  
	 * @param kv a string containing a list of comma separated key:value pairs in the form 
	 * <code>{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code>
	 */
	protected SettingsBuilder(Class<T> prototype, String kv) {
		this(prototype);
		//e.g. T15{k1:v1,k2:v2, ...} 
		Pattern p = Pattern.compile("(\\d+):([^,}]+)[,|}]");
		Matcher m = p.matcher(kv); 
		T[] constants = prototype.getEnumConstants(); //enum fields
		while (m.find()) {
			//System.out.println(m.group());
			int num = Integer.parseInt(m.group(1));
			settings.put(constants[num], m.group(2).strip());
		}
	}

	/**
	 * Returns an iterator over the key fields in this settings builder. The keys are returned 
	 * in enumeration order.  
	 * @return an iterator over the key fields in this settings builder.
	 */
	public Iterator<T> iterator() {
		return settings.keySet().iterator();
	}
	
	/**
	 * Returns a formatted string of the key-value pairs in this settings builder. The keys are the ordinal values of 
	 * the corresponding fields in the enumeration <code>T</code>.
	 * @return a string of key-value pairs in the form 
	 * <code>{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code>
	 */
	public String build() {
	    List<String> list = 
	    		settings.entrySet()
	    		.stream()
	    		.map(e -> e.getKey().ordinal() + ":" + e.getValue())
	    		.collect(Collectors.toList());
		return "{" + String.join(",",list) + "}";
	}

	/**
	 * Removes all key-value pairs from the command.
	 * @return this SettingsBuilder with all content removed.
	 */
	public SettingsBuilder<T> clear() {
		settings.clear();
		return this;
	}
	
	/**
	 * Maps a field to an integer value in this settings builder.
	 * @param field The Malt parameter to configure.
	 * @param val The integer value to assign to the given field.
	 * @return A reference to this updated <code>SettingsBuilder</code>.
	 */
	public SettingsBuilder<T> set(T field, int val) {
		settings.put(field,  val);
		return this;
	}
	
	/**
	 * Maps a Malt field to a double or float value in this Settings object.
	 * @param field The Malt parameter to configure.
	 * @param val The double or float value to assign to the given field.
	 * @return A reference to this updated <code>SettingsBuilder</code>.
	 */
	public SettingsBuilder<T> set(T field, double val) {
		settings.put(field,  val);
		return this;
	}

	/**
	 * Maps a Malt field to a boolean value in this Settings object.
	 * @param field The Malt parameter to configure.
	 * @param val The boolean value to assign to the given field.
	 * @return A reference to this updated <code>SettingsBuilder</code>.
	 */
	public SettingsBuilder<T> set(T field, boolean val) {
		settings.put(field,  val?1:0);
		return this;
	}

	
	/**
	 * Maps a Malt field to a string value in this Settings object.
	 * @param field The Malt parameter to configure.
	 * @param val The string value to assign to the given field.
	 * @return A reference to this updated <code>SettingsBuilder</code>.
	 */
	public SettingsBuilder<T> set(T field, String val) {
		settings.put(field,  val);
		return this;
	}
	
	/**
	 * Removes the mapping for this field from this map if present.
	 * @param field the field whose mapping is to be removed from the map
	 * @return A reference to this updated <code>SettingsBuilder</code>.
	 */
	public SettingsBuilder<T> remove(T field) {
		this.settings.remove(field);
		return this;
	}
	
	/** Returns the string representation of value to which the specified field is mapped, or null 
	 * if this map contains no mapping for the field. 
	 * Returns a string representation of the value mapped to this field in this SettingsBuilder. 
	 * @param field The field whose associated value is to be returned. 
	 * @return  the string representation of value to which the specified field is mapped, or null 
	 * if this map contains no mapping for the field.
	 */
	public String get(T field) {
		return settings.get(field).toString(); 
	}
	
	/**
	 * A string representation of these settings. 
	 * @return A comma separated list of field:value pairs enclosed in braces, 
	 * <code>{f<sub>1</sub>:v<sub>1</sub>,f<sub>2</sub>:v<sub>2</sub>,...}</code>. In contrast to the {@link #build()} method,
	 * the fields are names from the enumeration <code>T</code> rather than their ordinal values.
	 */
	@Override
	public String toString() {
	    List<String> list = 
	    		settings.entrySet()
	    		.stream()
	    		.map(e -> e.getKey().name() + ":" + e.getValue())
	    		.collect(Collectors.toList());
		return "{" + String.join(",",list) + "}";
	}

	private String entryAsJSON(Enum<T> fp, String value) {
		Properties prop = ((FieldProperties) fp).getProperties(); //Is there a way to capture FieldProperties in the generic declaration?
		if(prop.getType().equals("boolean")) value = value.equals("0")?"false":"true";
		return "\"" + fp.name() + "\":" + (prop.isNumber()? value : "\""+value+"\"");
	}
	
	/**
	 * A JSON representation of the key-value mappings in this SettingsBuilder. 
	 * Keys are given as the names from the domain of the underlying enumeration. 
	 * @return A JSON representation of the key value mappings in this SettingsBuilder.
	 */
	public String asJSON() {
	    List<String> list =  
	    		settings.entrySet() 
	    		.stream()
	    		.map(e -> this.entryAsJSON(e.getKey(), e.getValue().toString()))
	    		.collect(Collectors.toList());
		return "{" + String.join(",",list) + "}";
	}

	
	/**
	 * Updates this settings builder with the key-value pairs in the <code>json</code> string.
	 * @param prototype The underlying enum for the keys used for these settings.  
	 * @param json A string in json format with key-value data pairs. The keys are names from 
	 * the underlying enumeration, <code>T</code>.
	 * @return This settings builder with updated key-value pairs.
	 */
	protected SettingsBuilder<T> set(Class<T> prototype, String json) {
		//e.g. json = {"e1":v1,"e2:"v2","e1":v1,...}
//		System.out.println("JSON::"+json);
		//find all key:value pairs. Rem key=>group(1), value=>group(2) in regex pattern
		Pattern p = Pattern.compile("\"([^\"]+)?\".*?([\\+\\-]?\\b[\\p{Graph}&&[^\"},\\s]]+(\\s+[\\p{Graph}&&[^\"},]]+)*)\"*");
		Matcher m = p.matcher(json);
		while (m.find()) {
//			System.out.println(m.group() + "::(" + m.group(1) + "::" + m.group(2) + ")");
			String val = m.group(2);
			//replace false/true strings with 0/1.
			if (val.equals("true")) val = "1"; else if (val.equals("false")) val = "0";
				settings.put(Enum.valueOf(prototype,m.group(1)), val);
		}
		return this;
	}
	
	/**
	 * Updates SettingsBuilder<code>&lt;T&gt;</code> with the key-value pairs in the <code>json</code> string. 
	 * @param json A string in json format with key-value data pairs. The keys are names from 
	 * the underlying enumeration, <code>&lt;T&gt;</code>.
	 * @return This settings builder with updated key-value pairs.
	 */
	abstract protected SettingsBuilder<T> set(String json);
}
