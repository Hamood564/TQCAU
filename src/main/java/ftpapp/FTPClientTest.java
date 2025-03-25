package ftpapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FileTransferClient;

public class FTPClientTest {
	
	public static void main(String[] args) throws ParseException, FTPException, IOException {
		
		
		
		//Connect to FTPServer and get Directory Listing
		FTPConnect ftpconnect = new FTPConnect();
		
		//ftpconnect.ServerConnect("speedtest.tele2.net", "Anonymous", "");
		FTPClientManager.reconnect("192.168.115.176", "Anonymous", "");
	
		String s = ftpconnect.getDirectoryListing();
		System.out.println(s);
		
		// Retrieve and print the current directory path
        String currentDirectory = ftpconnect.getCurrentDirectory();
        System.out.println("Current Directory: " + currentDirectory);		
		
		System.out.println("Moving to the next test!!!");
		
		//set the mode to Active
		ftpconnect.setActive();
		//set the mode to Passive
		ftpconnect.setPassive();
		//get Directory Listing to Confirm
		FileManager ftpmanager = new FileManager();
		//ftpmanager.DeleteDirectory("TESTDELE");
		String s1 = ftpconnect.getDirectoryListing();
		System.out.println(s1);
		
		System.out.println("Moving to the next test Download All!!!");		
		
		//ftpmanager.DownloadAllFiles("Y211201");
		System.out.println("Moving to the rest of the tests!!!");	
		
		ftpconnect.close();
		
		
		 // Attempt to reconnect
	    boolean isConnected = false;
	    int retryCount = 0;
	    while (!isConnected && retryCount < 3) {
	        try {
	        	FTPClientManager.reconnect("192.168.115.176", "Anonymous", "");
	            isConnected = true;
	            
	        } catch (Exception e) {
	            System.out.println("Connection attempt failed, retrying...");
	            retryCount++;
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // wait before retrying
	        }
	    }
	    
	    if(FTPClientManager.isConnected()) {
	    	System.out.println("The client is connected!!!!!>>>>>>");
	    }else {
	    	System.out.println("The client is NOT connected!!!!!>>>>>>");
	    }

	    if (!isConnected) {
	        System.out.println("Failed to reconnect after multiple attempts.");
	        return; // Exit or handle the failure accordingly
	    }else {
	    	System.out.println("Connected!!!!!!!!!!!!");
	    }
	    
		
		String s21 = ftpconnect.getDirectoryListing();
		System.out.println(s21);
		
		//Change Directory from root to a folder
		ChangeDirectory ftpcd = new ChangeDirectory();
		ftpcd.CD("Y211201");
		String s2 = ftpconnect.getDirectoryListing();
		System.out.println(s2);
		
		// Retrieve and print the current directory path
        String currentDirectory2 = ftpconnect.getCurrentDirectory();
        System.out.println("Current Directory!!>>: " + currentDirectory2);	
		
		
		
		
		System.out.println("Moving to the next test!!!");
		
		//Change Directory from folder to parent.. can be iterated to root
		ftpcd.CDParent();
		String s3 = ftpconnect.getDirectoryListing();
		System.out.println(s3);
		
		System.out.println("Moving to the next test!!!");
		
		//Now Downloading a file
		ftpcd.CD("Y211201");
		String s4 = ftpconnect.getDirectoryListing();
		System.out.println(s4);
		//FileManager ftpmanager = new FileManager();
		//ftpmanager.Download("21120102.CSV");
		
		
		System.out.println("Moving to the next test!!!");
		
		//Now Uploading file
		ftpconnect.setPassive();  // Ensure passive mode is set before upload
		
		String s5 = ftpconnect.getDirectoryListing();
		System.out.println(s5);
		
		//Now Deleting File from remote server
		
		//ftpmanager.Delete("Test42.CSV");
		String s6 = ftpconnect.getDirectoryListing();
		System.out.println(s6);
		
		System.out.println("Moving to the next test!!!");
		
		ftpmanager.setTransferASCII();
		ftpmanager.setTransferBinary();
		
		String s7 = ftpconnect.getDirectoryListing();
		System.out.println(s7);
		
		
		//upload string to file in the server
		StreamData stream = new StreamData();
		//stream.outputStream("hi this is a test3!!", "Test42.CSV");
		ftpmanager.uploadTextAsFile("hi this is a test3!!", "Test42.CSV");
		//ftpmanager.Download("Test42.CSV");
		
		
		//set up logger and monitor for ftp transfers
		TransferMonitoring ftptransfer = new TransferMonitoring();
		ftptransfer.Logger("TEST45.CSV", "DEBUG");
				
		
		//download string from a file in server
		
		String s8 = stream.inputStream("21120104.CSV");
		System.out.println(s8);
		
		
	
		
		System.out.println("From here on \n\n");
		
		
		String fileName = "21120104.CSV"; // Replace with the name of the file you want to download

        try {
            InputStream inputStream = FileManager.Download(fileName);
            
            
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                inputStream.close();
            } else {
                System.out.println("Failed to download the file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        
        ftpconnect.close();
    }
	
		
		
		
	

}
