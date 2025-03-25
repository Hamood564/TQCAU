/**
 * Tools to create mappings of MALT fields to their values.
 * <p>The settings package enumerates MALT configuration fields: {@link settings.Param test parameters}, 
 * {@link settings.Option options} and {@link settings.Calib calibrations}. Each field has 
 * {@link settings.Properties Properties} comprising its type, default value, and a description, which
 * can be accessed using the {@link settings.FieldProperties FieldProperties} interface</p>
 * 
 * <p>'Builder' classes enable application programmers to map fields to values and then call the 
 * {@link settings.SettingsBuilder SettingsBuilder.build()} method to construct a string of key-value pairs
 * in the format required by a MALT TCP command. The abstract SettingsBuilder class provides common 
 * functionality that is elaborated by its subclasses: {@link settings.TestSettings}, 
 * {@link settings.OptionSettings}, and {@link settings.CalibrationSettings}.</p>
 * 
 * <p>SettingsBuilder objects are passed as parameters in <code>maltdriver.Malt.settings(XSettings)</code> 
 * methods to configure a MALT.</p> 
 */
package settings;
