package application;

import java.io.IOException;

import org.json.simple.JSONObject;


import legacy.setMALTresponse;
import maltdriver.Malt;
import maltdriver.Response;
import settings.Calib;
import settings.CalibrationSettings;
import settings.CalibrationSettings.Channel;
import settings.Option;
import settings.OptionSettings;
import settings.Param;
import settings.TestSettings;

public class MALTCall {
	
	public static String IP;
    public static Integer Port;
    public static String Volume;
    
    
    public static void connectMalt(String IP, Integer Port){
        //Connect to a MALT
        setIP(IP);
        setPort(Port);
        Malt malt = new Malt(IP, Port);
        Response connres = malt.getConnectionResponse();
        System.out.println(connres);
        System.out.println(malt.getHost() + ":" + malt.getPort() + ", " );

        //DefaultSettings.restoreDefaults(malt); //uncomment if required
    }
    
   
    
    public static void retrievesettings(){
        /*===== Retrieve all the settings =======*/
        //variable to capture response from server when a command is sent and processed. 
        Response res = null;
        //test configurations
        Malt malt = new Malt(IP, Port);
        for (int i=0; i<16; i++) {
                res = malt.requestTestSettings(i);
                System.out.println(res);
        }

        //calibrations
        for (Channel ch : Channel.values()) {
                res = malt.requestCalibrationSettings(ch);
                System.out.println(res);
        }

        //options
        res = malt.requestOptionSettings();
        System.out.println(res); //full response context
        System.out.println(res.getMessage()); //just the response message

        //--- use the last response message to give a nicer printout of options
        // Note this format conforms with the syntax expected.
        OptionSettings ops = new OptionSettings(res.getMessage());
        System.out.println("\nOptions\n=======");
        for (Option opt : ops) { //iterate over the option settings
                System.out.println(opt.name() + " : " + ops.get(opt));
        }
        System.out.println();

    }
    
    public static void setIP(String IP){
    	MALTCall.IP = IP;
       
    }
    
    public static void setPort(Integer Port){
    	MALTCall.Port = Port;
        
    }
    
    public static void setVolume(String Volume){
    	MALTCall.Volume = Volume;
    }
    
    
    public static void changecalibSettings(){
        Malt malt = new Malt(IP, Port);
        Response res = null;
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
    }
    
    public static void changeconfiguration(String Requirement, String TestIndex) throws IOException{
        String testIndex = TestIndex;
        Malt malt = new Malt(IP, Port);
        Response res = null;
        JSONObject configuration = setMALTresponse.Configuration(Requirement);
        String Pressure = (String)configuration.get("Pressure");
        String FillTime = (String)configuration.get("Fill");
        String StabilisationTime = (String)configuration.get("StabilisationTime") ;
        String IsolationDelay= (String)configuration.get("IsolationDelay") ;
        String MeasureTime= (String)configuration.get("MeasureTime");
        String VentingTime = (String)configuration.get("VentingTime");
        String AlarmLeakRate = (String)configuration.get("AlarmLeakRate");
        String AlarmDiffp =(String)configuration.get("AlarmDiffPressure");
                
        //====Configure test with index 1.====
        TestSettings ts = new TestSettings(1);
        ts	.set(Param.idString,testIndex)
                .set(Param.testpressure,Pressure)
                .set(Param.filltime,FillTime)
                .set(Param.stabilisationtime,StabilisationTime)
                .set(Param.isolationdelay,IsolationDelay)
                .set(Param.measuretime,MeasureTime)
                .set(Param.evacuationtime,VentingTime)
                .set(Param.alarmleakrate,AlarmLeakRate)
                .set(Param.alarmdiffp,AlarmDiffp);
        res = malt.settings(ts); //Send to MALT
        System.out.println(res);
    }
    
    
    
    public static void saveSetting(Integer TestNumber, String TestIndex, String Pressure, String Fill, String Stabilisation,
    		String Isolation, String Measure,String Vent, String LeakRate, String DiffP) {
    	Malt malt = new Malt(IP, Port);
        Response res = null;
    	TestSettings ts = new TestSettings(TestNumber);
        ts	.set(Param.idString,TestIndex)
                .set(Param.testpressure,Pressure)
                .set(Param.filltime,Fill)
                .set(Param.stabilisationtime,Stabilisation)
                .set(Param.isolationdelay,Isolation)
                .set(Param.measuretime,Measure)
                .set(Param.evacuationtime,Vent)
                .set(Param.alarmleakrate,LeakRate)
                .set(Param.alarmdiffp,DiffP);
        res = malt.settings(ts); //Send to MALT
        System.out.println(res);
    }
    
    
    public static void runtestMaltwithIdx(Integer Index) {
    	Response res = null;
    	Malt malt = new Malt(IP, Port);
    	malt.selectTest(Index);
    	res= malt.start();
    	System.out.println(res);
    	res = malt.requestResultCode();
		System.out.println(res);
		res = malt.requestLastData();
		System.out.println(res.getMessage().replaceAll(",", "\t"));
    	
    }
    
    public static void maltreset() {
    	Response res = null;
    	Malt malt = new Malt(IP, Port);
    	res = malt.reset();
		System.out.println(res);
    }
    
    public static void runtestMalt(){
        Malt malt = new Malt(IP, Port);
        Response res = null;
        /* ======== Run a test========= */
        res = malt.start();
        System.out.println(res);
        
        res = malt.requestResultCode();
        System.out.println(res);
        res = malt.requestLastData();
        //System.out.println(res.getResponse());
        System.out.println(res.getMessage().replaceAll(",", "\t"));

        /*===See code below. Run test(1) 5 times. Modify filltime on each occasion.*/ 
        //experiment(malt, 1, 5); //uncomment to run 

        res = malt.disconnect();
        System.out.println(res);
        System.out.println(malt.getConnectionResponse());
    }
    
    public static String getLastResult() {
    	 Malt malt = new Malt(IP, Port);
         Response res = null;
         res = malt.requestResultCode();
		return res.asJSON2();
         
    }
    
    public static String getTestConfigSetting(Integer Index) {
    	Malt malt = new Malt(IP, Port);
        Response res = null;
        res = malt.requestTestSettings(Index);
		return res.asJSON();
    	
    }
    
    public static Boolean MaltNotBusy() {
    	Malt malt = new Malt(IP, Port);
        
        if (!malt.testRunning()  &&  malt.isReady()) {
        	return true;
        }else {
			return false;
		}
		
    	
    }
    
    
    public static void experiment( int testIndex, int repetitions, Param ToChange, String Expression, String Key) throws IOException {
    Malt malt = new Malt(IP, Port);
    malt.selectTest(testIndex);
    TestSettings ts = new TestSettings(testIndex);
    JSONObject configuration = setMALTresponse.Configuration(Volume);
    ts.set(ToChange, (String)configuration.get(Key));

    for (int t=1; t<=repetitions; t++) {
            System.out.println("Start" + t + "--------------------------------------------");
            ts.set(ToChange, Expression); 
            Response res = malt.settings(ts);
            System.out.println(res);

            res = malt.start();
            System.out.println(res);

            res = malt.requestResultCode();
            System.out.println(res);
            res = malt.requestLastData();
            System.out.println(res.getMessage().replaceAll(",", "\t"));

            System.out.println("End " + t + " --------------------------------------------");
    }}
    
    public static String[] MALTDisplay () {
    	Malt malt = new Malt(IP, Port);
        String[] displayElementStore = new String[4];

        displayElementStore[0] = malt.requestTestState().simpleString();
        displayElementStore[1] = malt.requestDataTestP().simpleString();
        displayElementStore[2] = malt.requestDataDiffP().simpleString().trim();
        displayElementStore[3] = malt.requestDiffPAZ().simpleString();
        return displayElementStore;
    }

    public static Integer MALTStatus(){
    	Malt malt = new Malt(IP, Port);
        Integer status = Integer.parseInt(malt.requestStatus().getMessage());
        return status;
    }

    public static void MALTStatusLights(){
    	Malt malt = new Malt(IP, Port);
        do {
            int val = Integer.parseInt(malt.requestStatus().getMessage());
            StatusLights.printAll(val);
        } while (malt.testRunning());
    }

    public static String MALTGetData(){
    	Malt malt = new Malt(IP, Port);
        Response res = null;
        String ResultData;
        res = malt.requestResultCode();
        System.out.println(res);
        res = malt.requestLastData();
        //System.out.println(res.getResponse());
        System.out.println(res.getMessage().replaceAll(",", "\t"));
        return ResultData = res.getMessage().replaceAll(",", "\t");
    }
    
    
    
    






}
