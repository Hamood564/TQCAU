package settings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A SettingsBuilder for MALT calibration parameters. The fields of the mapping are specified by 
 * the {@link Calib} enumeration. This builder has an additional attribute for the the analogue 
 * to digital {@link Channel channel} under consideration.
 * 
 * @author leszek@tqc
 * @version 2024-01
 */
public class CalibrationSettings extends SettingsBuilder<Calib> {

	/**
	 * Enumeration of analogue/digital channels in the MALT device. 
	 */
	public enum Channel{
						/**Differential Pressure channel A */
						diffPA, 
						/**Differential Pressure channel B */
						diffPB, 
						/**Test Pressure channel A */
						testPA, 
						/**Test Pressure channel B */
						testPB
			};

	private Channel channel;
	
	/**
	 * Constructs a CalibrationSettings builder for the default channel <code>Channel.diffPA</code>.   
	 */
	public CalibrationSettings() {
		this(Channel.diffPA);
	}
	
	/**
	 * Constructs a CalibrationSettings builder for the given channel.   
	 * @param channel the channel for these calibration settings
	 */
	public CalibrationSettings(Channel channel) {
		super(Calib.class); 
		this.channel = channel;
	}

	/** Creates a calibrationSettings builder based on an encoded list of key-value pairs. This is the inverse 
	 * of {@link #build()}, and may provide a starting point for further editing of a calibration settings builder.     
	 * @param kv a string containing a list of comma separated key:value pairs in the form 
	 * <code>nn{k<sub>1</sub>:v<sub>1</sub>,k<sub>2</sub>:v<sub>2</sub>,...}</code>, where the keys are the ordinal
	 * values of the calibration fields and the prefix n is the channel number under consideration. 
	 * If the prefix is missing then channel 0 (<code>Channel.diffPA</code>) is assumed. 
	 */
	public CalibrationSettings(String kv) {
		//e.g. C2{k1:v1,k2:v2, ...}
		//populate the settings map
		super(Calib.class, kv);
		//extract and set the test index
		Pattern p = Pattern.compile("(\\d+)\\{");
		Matcher m = p.matcher(kv);
		this.channel = (m.find()) ? Channel.values()[Integer.parseInt(m.group(1))] : Channel.diffPA;		
	}
	
	/**
	 * Associates these settings with the given calibration channel. 
	 * @param channel the channel for these calibration settings
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	/**
	 * Returns the calibration channel for these settings.
	 * @return the channel to which these settings apply.
	 */
	public Channel getChannel() {return this.channel;}

	/**
	 * Returns a formatted string of the key-value pairs in this settings builder.
	 * @return a string of the form <code><i>C</i>{k1:v1,k2:v2,...}</code> of 
	 * key-value pairs prefixed by the digital/analogue channel number. 
	 */
	@Override
	public String build() {
		return this.channel.ordinal() + super.build();
	}

	/**
	 * A string representation of these settings.
	 * @return A comma separated list of field:value pairs enclosed in braces, prefixed by the calibration channel:  
	 * <code>channel{f<sub>1</sub>:v<sub>1</sub>,f<sub>2</sub>:v<sub>2</sub>,...}</code>. In contrast to the {@link #build()} method,
	 * the fields are names from the enumeration {@link Calib} rather than their ordinal values.
	 */
	@Override
	public String toString() {
		return "Calibration:" + this.channel.name() + super.toString();
	}

	/**
	 * A JSON representation of the key-value mappings in this CalibrationSettings. 
	 * Keys are given as the names in the {@link Calib} enumeration.
	 * @return A JSON representation of the key-value mappings in this CalibrationSettings.
	 */
	public String asJSON() {
		return "{\"calibration\":{" +
				"\"channel\":\"" + this.channel.name() + "\"" +
				",\"settings\":" +super.asJSON() +
				"}}";
	}


	/**
	 * Updates this CalibrationSettings builder with the key-value pairs in the <code>json</code> string. 
	 * @param json A string in json format with key-value data pairs. The keys are names from 
	 * the {@link Calib} enum.
	 * @return This settings builder with updated key-value pairs.
	 */
	public CalibrationSettings set(String json) {
		super.set(Calib.class, json);
		return this;
	}
}