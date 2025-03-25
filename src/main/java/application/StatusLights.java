package application;

/**
 * Example application program to help interrogate the MALT status flags (device LEDs).
 * 
 * Enumeration of MALT status lights. The isOn(int) method can be used to 
 * interpret the value returned by the Malt API in response to requestStatus().    
 * @author leszek@tqc
 */
public enum StatusLights {
	PASS(1), FAIL(2), READY(4), ALARM(8);

	private int mask;
	
	private StatusLights(int mask) {
		this.mask = mask;
	}
	
	public boolean isOn(int val) {
		return (val & this.mask) != 0;
	}

	static public void printAll(int val) {
		for (StatusLights ind : StatusLights.values()) {
			System.out.print(" " + ind.name() + ":" + (ind.isOn(val)?"ON":"OFF"));
		}
		System.out.println();
	}

}
