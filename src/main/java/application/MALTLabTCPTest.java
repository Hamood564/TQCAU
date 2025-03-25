package application;

import java.util.Iterator;

import maltdriver.Malt;
import maltdriver.Response;
import settings.CalibrationSettings.Channel;

public class MALTLabTCPTest {
	
	public static void main(String[] args) {
		
		//Connect to a MALT
		//Malt malt = new Malt(); 
	    Malt malt = new Malt("192.168.115.205", 5000); //("192.168.115.220", 5000);
		Response connres = malt.getConnectionResponse();
		System.out.println(connres);
		//System.out.println(malt.getHost() + ":" + malt.getPort() + ", " + (malt.isReady()?"ready":"not ready"));
				
		
		Response res = null;
		
		while(true){
			for (int i = 0; i < 16; i++) {
				
				malt.selectTest(i);		
				res = malt.start();
				System.out.println(res);
				
				//test settings
				
				res = malt.requestTestSettings(i);
				System.out.println(res);
				
				for (Channel ch : Channel.values()) {
					res = malt.requestCalibrationSettings(ch);
					System.out.println(res.asJSON());
				}
				
				//options
		        res = malt.requestOptionSettings();
		        System.out.println(res); //full response context
		        System.out.println(res.getMessage()); //just the response message
				
				
				//System.out.println(malt.testRunning()?"test running":"test not running");
				do {
					int val = Integer.parseInt(malt.requestStatus().getMessage());
					StatusLights.printAll(val);			
				} while (malt.testRunning());
				res = malt.requestResultCode();
				System.out.println(res);
				malt.reset();
				
			}
		}
		
		

	}
}
