/**
 * MALT connection and command interface. This package provides two top level classes:
 * <ul>
 * <li><code>Malt</code>: contains methods to connect to a MALT device and send commands to it;</li>
 * <li><code>Response</code>: captures data about each interaction.</li>
 * </ul>
 * The overloaded <code>Malt.settings(XSettings)</code> methods accept mappings of MALT fields to values
 * and send them to the MALT. The tools to create <code>XSettings</code> objects are in package {@link settings}
 * together with enumerations of MALT configuration fields.
 */
package maltdriver;
