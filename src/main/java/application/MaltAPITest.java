package application;

import maltdriver.Malt;
import maltdriver.Response;
import settings.Calib;
import settings.CalibrationSettings;
import settings.CalibrationSettings.Channel;
import settings.Option;
import settings.OptionSettings;
import settings.Param;
import settings.TestSettings;

public class MaltAPITest {
	
	public static void main(String[] args) {
		//Connect to a MALT
		//Malt malt = new Malt(); 
		Malt malt = new Malt("192.168.115.177", 5000); //("192.168.115.220", 5000);
		Response connres = malt.getConnectionResponse();
		System.out.println(connres);
		//System.out.println(malt.getHost() + ":" + malt.getPort() + ", " + (malt.isReady()?"ready":"not ready"));
		
		//variable to capture response from server when a command is sent and processed. 
		Response res = null;
		
		//DefaultSettings.restoreDefaults(malt); //uncomment if required

		/*===== Retrieve all the settings =======*/
		//test configurations
		for (int i=0; i<16; i++) {
			res = malt.requestTestSettings(i);
			System.out.println(res.getMessage());
			System.out.println(new TestSettings(res.getMessage()).asJSON()); //just the response message
		}

		//calibrations
		for (Channel ch : Channel.values()) {
			res = malt.requestCalibrationSettings(ch);
			System.out.println("The calibrationsetting is...");
			System.out.println(res.asJSON());
		}
		
//		res = malt.requestTestSettings(1);
//		System.out.println(res.asJSON());
//		System.out.println(res.asJSON());

		//options
		OptionSettings ops = new OptionSettings();
		ops.set(Option.startenable, false);
		res = malt.settings(ops);
		System.out.println(res.asJSON()); //full response context
		res = malt.requestOptionSettings();
		System.out.println("================");
		System.out.println(res.asJSON()); //full response context
		System.out.println("================");
		System.out.println(res.getMessage()); //just the response message
		System.out.println(new OptionSettings(res.getMessage()).asJSON()); //just the response message
		
		//--- use the last response message to give a nicer printout of options
		// Note this format conforms with the syntax expected.
		System.out.println("\nOptions\n=======");
		for (Option opt : ops) { //iterate over the option settings
			System.out.println(opt.name() + " : " + ops.get(opt));
		}
		System.out.println();
		System.out.println("This is options string:"+ops.toString());

		/*==== Change some settings========================*/
		//modify the max settings for calibration channel diffPB
		CalibrationSettings cs = new CalibrationSettings(Channel.diffPB);
		cs.set(Calib.countMax, 40000);
		cs.set(Calib.valueMax, 16000);
		res = malt.settings(cs); 		//send the calibration settings to MALT
		System.out.println(res);
		
		//check that the changes havebeen made
		res = malt.requestCalibrationSettings(Channel.diffPB);
		System.out.println(res);
		System.out.println(new CalibrationSettings(res.getMessage()).asJSON()); //just the response message
		
		TestSettings ts2 = new TestSettings(malt.requestTestSettings(0).getMessage());
		System.out.println("The ts2 pressure is:"+ ts2.get(Param.testpressure));
		
		

		//====Configure test with index 1.====
		TestSettings ts = new TestSettings(1);
		ts	.set(Param.idString,"1: MyTest")
			.set(Param.testpressure,3000)
			.set(Param.filltime,10000)
			.set(Param.stabilisationtime,2000)
			.set(Param.isolationdelay,400)
			.set(Param.measuretime,1000)
			.set(Param.evacuationtime,500)
			.set(Param.alarmleakrate,3000)
			.set(Param.alarmdiffp,2000);
		
		
		
		
		
		res = malt.settings(ts); //Send to MALT
		System.out.println(res);
		System.out.println(new TestSettings(res.getMessage()).asJSON()); //just the response message

		//reset
		res = malt.reset();
		System.out.println(res);
		
		//wait until MALT is ready (i.e not in a test sequence)
		int i=0;
		while(!malt.isReady()) { 
			try {Thread.sleep(200);} catch (InterruptedException e) {}
			i++;
		}
		System.out.println("i=" + i); //LZ i=0; so the wait loop seems unnecessary in this context 
		/* ======== Run a test========= */
		malt.selectTest(2);		
		res = malt.start();
		System.out.println(res);
		//wait until MALT test starts
		int j=0;
		while(!malt.testRunning()) { 
			try {Thread.sleep(200);} catch (InterruptedException e) {}
			i++;
		}
		System.out.println("i=" + j); //LZ i=0; so the wait loop seems unnecessary in this context 

		//System.out.println(malt.testRunning()?"test running":"test not running");
		do {
			int val = Integer.parseInt(malt.requestStatus().getMessage());
			StatusLights.printAll(val);			
		} while (malt.testRunning());
		res = malt.requestResultCode();
		System.out.println(res);
		res = malt.requestLastData();
		//System.out.println(res.getResponse());
		
		System.out.println(res.getMessage().replaceAll(",", "\t"));
		
		/*===See code below. Run test(1) 5 times. Modify filltime on each occasion.*/ 
		//experiment(malt, 1, 5); //uncomment to run 
		
		Response[] jres = malt.settings("garbage"); //degenerate case: an empty json string
		for (Response r : jres) {
			System.out.println(r.asJSON());
		}
		
		String json =
		"""
			{
				"options":{"logLevel":3,"startenable":true},
				"calibration":[
					{"channel":"testPA","settings":{"offset":15.0}},
					{"channel":"diffPA","settings":{"gradient":1.5,"offset":-4.0}}
				],
				"testconfig":[
					{"index":2,"settings":{"idString":"Pump","filltime":5000,"measuretime":1000}},
					{"index":3,"settings":{"idString":"Cylinder Head Cover","filltime":12000,"measuretime":1500}},
					{"index":4,"settings":{"idString":"Valve","testpressure":2000}}
				]
			}
		""";
		try {
			jres =  malt.settings(json);
			for (Response r : jres) {
				System.out.println(r.asJSON());
			}
		}
		catch (Exception e ) {
			System.out.println(e.getMessage());
		}
		
		res = malt.requestOptionSettings();
		System.out.println(res.asJSON());

		res = malt.requestCalibrationSettings(Channel.diffPA);
		System.out.println(res.asJSON());
		
		res = malt.requestTestSettings(3);
		System.out.println(res.asJSON());
		
		res = malt.requestResultCode();
		System.out.println(res.asJSON2());
		
		res = malt.requestTestState();
		System.out.println(res.asJSON());
		
		res = malt.requestDiffPAZ();
		System.out.println("The DiffPAZ response is...." + res.simpleString());
		
		res = malt.requestDataTestP();
		System.out.println(res.asJSON());
		
		res = malt.requestDataDiffP();
		System.out.println(res.asJSON());
		
		int val2 = Integer.parseInt(malt.requestStatus().getMessage());
		System.out.println(val2);
//		
//		MALTCall.connectMalt("192.168.115.176", 5000);
//		String s = MALTCall.MALTGetData();
//        System.out.println(s);
		
		res = malt.disconnect();
		System.out.println(res);
		System.out.println(malt.getConnectionResponse());
		
		
		
	}
//	
//	
//	/**
//	 * @param m Reference to a (connected) MALT object
//	 * @param testIndex - index of test to run
//	 * @param repetitions - number of times
//	 */
//	public static void experiment(Malt m, int testIndex, int repetitions) {
//
//		m.selectTest(testIndex);
//		TestSettings ts = new TestSettings(testIndex);
//		ts.set(Param.stabilisationtime, 1000);
//		
//		for (int t=1; t<=repetitions; t++) {
//			System.out.println("Start" + t + "--------------------------------------------");
//			ts.set(Param.filltime, t*2000); //vary filltime
//			Response res = m.settings(ts);
//			System.out.println(res);
//			
//			res = m.start();
//			System.out.println(res);
//			while (m.testRunning()) {
//				int val = Integer.parseInt(m.requestStatus().getMessage());
//				StatusLights.printAll(val);
//			}
//			res = m.requestResultCode();
//			System.out.println(res);
//			res = m.requestLastData();
//			System.out.println(res.getMessage().replaceAll(",", "\t"));
//
//			System.out.println("End " + t + " --------------------------------------------");
//		}
//	}

}
