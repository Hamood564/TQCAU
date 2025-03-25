package application;

import maltdriver.Malt;
import settings.Calib;
import settings.CalibrationSettings;
import settings.CalibrationSettings.Channel;
import settings.Option;
import settings.OptionSettings;
import settings.Param;
import settings.TestSettings;

/**
 * Application program to configure MALT with the 'defaults' defined in 
 * defaultXettings() methods.
 * @author leszek.zarzycki
 * @version 2021-03
 */
public class DefaultSettings {

	public static void restoreDefaults(Malt malt) {
		
		//options
		malt.settings(defaultOptionSettings());
		
		//tests
		TestSettings ts = defaultTestSettings();
		for (int i=0; i<16; i++) {
			//for each test index:
			ts.setTestIndex(i);
			ts.set(Param.idString, i + ": config" + i); //e,g, "12: config12"
			malt.settings(ts);
		}
		
		//calibration channels
		CalibrationSettings cs = defaultCalibrationSettings();
		for (Channel ch : Channel.values()) {
			//for each A/D channel
			cs.setChannel(ch);
			malt.settings(cs);
		}
	
		/*==== uncomment to save... ========= */
		//malt.saveSettings(); //save to MALT user parameter memory.
		//malt.exportSettings("defaults.txt"); //save to MALT sd card 
	}
	
	
	/**
	 * Return a OptionSettings object with all fields set to 'default'values
	 */
	public static OptionSettings defaultOptionSettings() {
		OptionSettings ops = new OptionSettings(); 
		ops	.set(Option.logPort,9284)
			.set(Option.logIP,"192.168.115.200")
			.set(Option.logLevel,1)
			.set(Option.testpTol,20)
			.set(Option.diffpAbort,7500)
			.set(Option.diffpNegAlarm,-200)
			.set(Option.LT_ID,'A')
			.set(Option.startenable,false)
			.set(Option.option1,0)
			.set(Option.option2,0.0)
			.set(Option.option3,0)
			.set(Option.option4,0.0)
			.set(Option.option5,0)
			.set(Option.option6,0.0)
			.set(Option.password1,"highpass")
			.set(Option.password2,"lowpass");	
		return ops;
	}
	
	public static CalibrationSettings defaultCalibrationSettings() {
		CalibrationSettings cs = new CalibrationSettings(); 
		cs	.set(Calib.valueMax,30000)
			.set(Calib.countMax,14000)
			.set(Calib.valueMin,0)
			.set(Calib.countMin,6500)
			.set(Calib.gradient,1.0)
			.set(Calib.offset,0.0);	
		return cs;
	}
	
	public static TestSettings defaultTestSettings() {
		TestSettings ts = new TestSettings(); 
		ts	.set(Param.idString,"-")
			.set(Param.testpressure,5000)
			.set(Param.initdelay,100)
			.set(Param.ventoffdelay,200)
			.set(Param.filltime,2000)
			.set(Param.stabilisationtime,5000)
			.set(Param.isolationdelay,400)
			.set(Param.measuretime,2000)
			.set(Param.evacuationtime,1000)
			.set(Param.offsetcomp,0)
			.set(Param.alarmleakrate,5000)
			.set(Param.alarmdiffp,1000)
			.set(Param.spare1,0)
			.set(Param.fixtureOPstart,0)
			.set(Param.fixturedelaytime,0)
			.set(Param.checkinputs,0)
			.set(Param.enablecheckinputs,false)
			.set(Param.fixtureOPend,0)
			.set(Param.nexttestonpass,0)
			.set(Param.nexttestonfailoralarm,0)
			.set(Param.inhibitresults,false)
			.set(Param.spare2,0)
			.set(Param.spare3,0)
			.set(Param.spareflag1,false)
			.set(Param.spareint10,0)
			.set(Param.spareint11,0)
			.set(Param.spareint12,0)
			.set(Param.spareint13,0)
			.set(Param.spareflag10,false)
			.set(Param.spareflag11,false)
			.set(Param.spareflag12,false)
			.set(Param.spareflag13,false)
			.set(Param.enabled_1,false)
			.set(Param.fixtime_1,0)
			.set(Param.feedback_1,false)
			.set(Param.enabled_2,false)
			.set(Param.fixtime_2,0)
			.set(Param.feedback_2,false)
			.set(Param.enabled_3,false)
			.set(Param.fixtime_3,0)
			.set(Param.feedback_3,false)
			.set(Param.passmark,false)
			.set(Param.passmarktime,0)
			.set(Param.failack,false)
			.set(Param.fillToPres,false)
			.set(Param.fillToPresTimer,0)
			.set(Param.spareint20,0)
			.set(Param.spareint21,0)
			.set(Param.spareint22,0)
			.set(Param.spareint23,0)
			.set(Param.spareflag20,false)
			.set(Param.spareflag21,false)
			.set(Param.spareflag22,false)
			.set(Param.spareflag23,false)
			.set(Param.sparestring20,"-");	
		return ts;
	}
	
}
