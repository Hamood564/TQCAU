package settings;

/** Enumeration of fields used to set the parameters of a leak test. 
 * Each field has a set of {@link settings.Properties Properties} comprising its <code>type</code>, <code>default value</code>, and brief <code>description</code>.
 * Note that the <code>default value</code> is given as a string that represents a value of its <code>type</code>.   
 * Note, boolean is also considered a numeric type in this context.
 *  
 * @author leszek@tqc
 * @version 2024-01
 */
public enum Param implements FieldProperties {

	/** Properties: no type, "", No specific meaning */
	NOPARAM					(false, "no type", "", "No specific meaning"),

	/** Properties: string[40], "-", "Identifier for test */
	idString				(false, "string[40]", "-", "Identifier for test"),

	/** Properties: mbar int, "0", Test Pressure, negative for vacuum */
	testpressure			(true, "mbar int", "0", "Test Pressure, negative for vacuum"),

	/** Properties: millisecond int, "0", Delay time after closing vent valve */
	initdelay				(true, "millisecond int", "0", "Delay time after closing vent valve"),

	/** Properties: millisecond int, "0", Time allowed for venting */
	ventoffdelay			(true, "millisecond int", "0", "Time allowed for venting"),

	/** Properties: millisecond int, "0", Fill valve open time */
	filltime				(true, "millisecond int", "0", "Fill valve open time"),

	/** Properties: millisecond int, "0", Stabilisation time */
	stabilisationtime		(true, "millisecond int", "0", "Stabilisation time"),

	/** Properties: millisecond int, "0", Dwell after closing isolation valve before zero of diff pressure */
	isolationdelay			(true, "millisecond int", "0", "Dwell after closing isolation valve before zero of diff pressure"),

	/** Properties: millisecond int, "0", Leak rate measure time */
	measuretime				(true, "millisecond int", "0", "Leak rate measure time"),

	/** Properties: millisecond int, "0", Evacuation or vent time at end of test */
	evacuationtime			(true, "millisecond int", "0", "Evacuation or vent time at end of test"),

	/** Properties: Pa/s or mbar/s int, "0", Offset compensation applied during measure */
	offsetcomp				(true, "Pa/s or mbar/s int", "0", "Offset compensation applied during measure"),

	/** Properties: mm3/s or mbar/s int, "0", Leak rate at the differential pressure threshold */
	alarmleakrate			(true, "mm3/s or mbar/s int", "0", "Leak rate at the differential pressure threshold"),

	/** Properties: Pa or mbar int, "0", Differential pressure fail threshold */
	alarmdiffp				(true, "Pa or mbar int", "0", "Differential pressure fail threshold"),

	/** Properties: mbar int, "0", Dosing Gross Leak Pressure Limit (mbar) */
	spare1					(true, "mbar int", "0", "Dosing Gross Leak Pressure Limit (mbar)"),

	/** Properties: bit pattern 0..FF int, "0", Fixture outputs at start */
	fixtureOPstart			(true, "bit pattern 0..FF int", "0", "Fixture outputs at start"),

	/** Properties: millisecond int, "0", Delay time after switching fixture outputs (and before input check) */
	fixturedelaytime		(true, "millisecond int", "0", "Delay time after switching fixture outputs (and before input check)"),

	/** Properties: bit pattern 0..FF int, "0", Check inputs */
	checkinputs				(true, "bit pattern 0..FF int", "0", "Check inputs"),

	/** Properties: boolean,"false", Enable inputs check" */
	enablecheckinputs 		(true, "boolean", "false", "Enable inputs check"),

	/** Properties: bit pattern 0..FF int", "0", Fixture outputs at end */
	fixtureOPend			(true, "bit pattern 0..FF int", "0", "Fixture outputs at end"),

	/** Properties: 0..15 (0=end) int, "0", Chain test - next test on pass" */
	nexttestonpass			(true, "0..15 (0=end) int", "0", "Chain test - next test on pass"),

	/** Properties: 0..15 (0=end) int, "0", Chain test next test on fail */
	nexttestonfailoralarm	(true, "0..15 (0=end) int", "0", "Chain test next test on fail"),

	/** Properties: boolean, false, inhibit results display at end of chained test */
	inhibitresults			(true, "boolean", "false", "inhibit results display at end of chained test"),

	/** Properties: millisecond int, "0", Delay at end of test before flashing the Done indicator */
	spare2					(true, "millisecond int", "0", "Delay at end of test before flashing the Done indicator"),

	/** Properties: millisecond (0=disable) int, "0", pressure equalisation time when test pressure changes */
	spare3					(true, "millisecond (0=disable) int", "0", "pressure equalisation time when test pressure changes"),

	/** Properties: boolean, false, Use test pressure to measure leakage instead of differential pressure */
	spareflag1				(true, "boolean", "false", "Use test pressure to measure leakage instead of differential pressure"),

	/** Properties: int, "0", Spare for future use */
	spareint10				(true, "int", "0", "Spare for future use"),

	/** Properties: int, "0", Spare for future use */
	spareint11				(true, "int", "0", "Spare for future use"),

	/** Properties: int, "0", Spare for future use */
	spareint12				(true, "int", "0", "Spare for future use"),

	/** Properties:  int, "0", Spare for future use */
	spareint13				(true, "int", "0", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
	spareflag10				(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
	spareflag11				(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
	spareflag12				(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
	spareflag13				(true, "boolean", "false", "Spare for future use"),
	//FixtureControl

	/** Properties: boolean, false, Fixture 1 op enabled */
	enabled_1				(true, "boolean", "false", "Fixture 1 op enabled"),

	/** Properties: millisecond int, "0", Time to complete fixture 1 op */
	fixtime_1				(true, "millisecond int", "0", "Time to complete fixture 1 op"),

	/** Properties: boolean, false, Enable feedback for fixture 1 */
	feedback_1				(true, "boolean", "false", "Enable feedback for fixture 1"),

	/** Properties: boolean, false,	 Fixture 2 op enabled */
	enabled_2				(true, "boolean", "false", "Fixture 2 op enabled"),

	/** Properties: millisecond int, "0", Time to complete fixture 2 op */
	fixtime_2				(true, "millisecond int", "0", "Time to complete fixture 2 op"),

	/** Properties: boolean, false, Enable feedback for fixture 2 */
	feedback_2				(true, "boolean", "false", "Enable feedback for fixture 2"),

	/** Properties: boolean, false, Fixture 3 op enabled */
	enabled_3				(true, "boolean", "false", "Fixture 3 op enabled"),

	/** Properties: millisecond int, "0", Time to complete fixture 3 op */
	fixtime_3				(true, "millisecond int", "0", "Time to complete fixture 3 op"),

	/** Properties: boolean, false, "Enable feedback for fixture 1"*/
	feedback_3				(true, "boolean", "false", "Enable feedback for fixture 3"),

	/** Properties: boolean, false, "Enable pass mark operation" */
	passmark				(true, "boolean", "false", "Enable pass mark operation"),

	/** Properties: int, "0", Time to complete pass mark operation */
	passmarktime			(true, "int", "0", "Time to complete pass mark operation"),

	/** Properties: millisecond int, "0", Time to complete fixture 3 op */
	failack				(true, "boolean", "false", "Enable feedback on fixture failure"),

	/** Properties: millisecond int, "0", Time to complete fixture 3 op */
	fillToPres				(true, "boolean", "false", "Enable fill to pressure operation"),

	/** Properties:  millisecond int, "0", Time to complate fill to pressure operation */
	fillToPresTimer		(true, "int", "0", "Time to complate fill to pressure operation"),

	/** Properties: int, "0", Spare for future use */
    spareint20				(true, "int", "0", "Spare for future use"),

	/** Properties: int, "0", Spare for future use */
    spareint21				(true, "int", "0", "Spare for future use"),

	/** Properties: int, "0", Spare for future use */
    spareint22				(true, "int", "0", "Spare for future use"),

	/** Properties: int, "0", Spare for future use */
    spareint23				(true, "int", "0", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
    spareflag20			(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
    spareflag21			(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
    spareflag22			(true, "boolean", "false", "Spare for future use"),

	/** Properties: boolean, false, Spare for future use */
    spareflag23			(true, "boolean", "false", "Spare for future use"),

	/** Properties: string[40], "-", Identifier for test */
	sparestring20		(false, "string[40]", "-", "Identifier for test");

	private final Properties properties;
	
	private Param(boolean numeric, String type, String defaultValue, String description) {
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
	 * String representation of a Param field.
	 * @return Returns a String comprising this field name and its properties.
	 */
	@Override
	public String toString() {
		return this.name() + ": " + this.getProperties();
	}
}