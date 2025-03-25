package ftpapp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.SimpleFormatter;


import com.enterprisedt.net.ftp.EventListener;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import com.enterprisedt.util.debug.Appender;
import com.enterprisedt.util.debug.FileAppender;
import com.enterprisedt.util.debug.Level;
import com.enterprisedt.util.debug.Logger;
import com.enterprisedt.util.debug.StandardOutputAppender;

public class TransferMonitoring {
	
	public static void Logger(String fileName, String logLevel) {
		try {
            // set up logger so that we get some output
            Logger log = Logger.getLogger(TransferMonitoring.class);
                        
            if (logLevel.equalsIgnoreCase("INFO")) {
            	log.setLevel(Level.INFO);
             // this will appear with the level set to INFO
                log.info("This is an info message");
            } else if (logLevel.equalsIgnoreCase("DEBUG")) {
                log.setLevel(Level.DEBUG);
             // this will not appear unless the level is set to DEBUG
                log.debug("This is a debug message");
            } else if (logLevel.equalsIgnoreCase("ERROR")) {
            	log.setLevel(Level.ERROR);
                // this will appear with the level set to INFO
                log.error("This is an error message");
            } else {
                com.enterprisedt.util.debug.Logger.setLevel(Level.INFO); // Default to INFO level if no match
                log.info("This is an info message");
            }
            
            Monitor(logLevel);
            
            // log to a file and also to standard output
            Logger.addAppender(new FileAppender(fileName));
            Logger.addAppender(new StandardOutputAppender());
           

            
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	
	public static String LoggerOut(String logLevel) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Logger log = Logger.getLogger(TransferMonitoring.class);

        try {
            // Create and add custom StringWriter appender
            Appender stringWriterAppender = new Appender() {
                public void log(String msg) {
                    printWriter.println(msg);
                }

				@Override
				public void close() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void log(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
            };

            // Add custom appender and other appenders
            Logger.addAppender(stringWriterAppender);
            Logger.addAppender(new StandardOutputAppender());

            // Set log level
            if (logLevel.equalsIgnoreCase("INFO")) {
                log.setLevel(Level.INFO);
                log.info("This is an info message");
            } else if (logLevel.equalsIgnoreCase("DEBUG")) {
                log.setLevel(Level.DEBUG);
                log.debug("This is a debug message");
            } else if (logLevel.equalsIgnoreCase("ERROR")) {
                log.setLevel(Level.ERROR);
                log.error("This is an error message");
            } else {
                log.setLevel(Level.INFO); // Default to INFO level if no match
                log.info("This is an info message");
            }

            Monitor(logLevel);

        } catch (Exception e) {
            e.printStackTrace(printWriter);
        }

        return stringWriter.toString();
    }

	
	

	public static void Monitor(String logLevel) {
		
		// set up logger so that we get some output
        Logger log = Logger.getLogger(TransferMonitoring.class);
        
        if (logLevel.equalsIgnoreCase("INFO")) {
        	log.setLevel(Level.INFO);
        } else if (logLevel.equalsIgnoreCase("DEBUG")) {
        	log.setLevel(Level.DEBUG);
        } else if (logLevel.equalsIgnoreCase("ERROR")) {
        	log.setLevel(Level.ERROR);
        } else {
        	log.setLevel(Level.INFO); // Default to INFO level if no match
        }
        
        FileTransferClient ftpClient = FTPClientManager.getInstance();
		// set up listener
		ftpClient.setEventListener(new EventListenerImpl());
		 // the transfer notify interval must be greater than buffer size
		ftpClient.getAdvancedSettings().setTransferBufferSize(500);
		ftpClient.getAdvancedSettings().setTransferNotifyInterval(1000);
        
		
	}
	
	
	
	
	/**
	 * Listens to events
	 */
	static class EventListenerImpl implements EventListener {

	    private Logger log = Logger.getLogger(EventListenerImpl.class);

	    public void bytesTransferred(String connId, String remoteFilename, long bytes) {
	        log.info("Bytes transferred=" + bytes);
	    }
	    
	    /**
	     * Log an FTP command being sent to the server. Not used for SFTP.
	     * 
	     * @param cmd   command string
	     */
	    public void commandSent(String connId, String cmd) {
	        log.info("Command sent: " + cmd);
	    }
	    
	    /**
	     * Log an FTP reply being sent back to the client. Not used for
	     * SFTP.
	     * 
	     * @param reply   reply string
	     */
	    public void replyReceived(String connId, String reply) {
	        log.info("Reply received: " + reply);
	    }
	        
	    /**
	     * Notifies that a download has started
	     * 
	     * @param remoteFilename   remote file name
	     */
	    public void downloadStarted(String connId, String remoteFilename) {
	        log.info("Started download: " + remoteFilename);
	    }
	    
	    /**
	     * Notifies that a download has completed
	     * 
	     * @param remoteFilename   remote file name
	     */
	    public void downloadCompleted(String connId, String remoteFilename) {
	        log.info("Completed download: " + remoteFilename);
	    }
	    
	    /**
	     * Notifies that an upload has started
	     * 
	     * @param remoteFilename   remote file name
	     */
	    public void uploadStarted(String connId, String remoteFilename) {
	        log.info("Started upload: " + remoteFilename);
	    }
	    
	    /**
	     * Notifies that an upload has completed
	     * 
	     * @param remoteFilename   remote file name
	     */
	    public void uploadCompleted(String connId, String remoteFilename) {
	        log.info("Completed upload: " + remoteFilename);
	    }

}
	
	
	

	 
	
	
	

}
