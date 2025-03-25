package ftpapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


class FTPClient {
	
	private String server;
	private int port;
	private String user;
	private String password;
	private org.apache.commons.net.ftp.FTPClient ftp;
	
	FTPClient(String server, int port, String user, String password){
		this.server = server;
		this.port = port;
		this.user = user;
		this.password = password; 
	}
	
	//constructor
	void open() throws IOException{
		ftp = new org.apache.commons.net.ftp.FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
				
		
		ftp.connect(server, port);
		// Enable passive mode for data transfer
        ftp.enterLocalPassiveMode();
		
		int reply = ftp.getReplyCode();
		if(!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			throw new IOException("Exception in connecting to FTP Server");
		}
		ftp.login(user, password);
		//String workingDir = ftp.printWorkingDirectory();
		//System.out.println("Current directory: " + workingDir);
		//ftp.sendCommand("LIST -a");
	}
	
	void close() throws IOException{
		ftp.logout();
		ftp.disconnect();
	}
	
	//getting list of files on the server
	Collection<String> listFiles(String path) throws IOException {
		FTPFile[] files = ftp.listFiles(path);
		System.out.println("Number of files found: " + files.length);
		for (FTPFile file : files) {
		    System.out.println("File: " + file.getName());
		}
		return Arrays.stream(files)
				.map(FTPFile::getName)
				.collect(Collectors.toList());
	}
	
	//upload file to path
	void putFileToPath(File file, String path) throws IOException {
		ftp.storeFile(path, new FileInputStream(file));
	}
	
	//download file from server
	void downloadFile(String source, String destination) throws IOException {
		FileOutputStream out = new FileOutputStream(destination);
		ftp.retrieveFile(source, out);
		out.close();
	}
	
	
	//download whole directory
	void downloadDirectory(String remoteDirPath, String localParentDir) throws IOException {
        FTPFile[] subFiles = ftp.listFiles(remoteDirPath);
        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentRemoteFilePath = remoteDirPath + "/" + aFile.getName();
                File localFile = new File(localParentDir + "/" + aFile.getName());

                if (aFile.isDirectory()) {
                    // Create the local directory if it does not exist
                    if (!localFile.exists()) {
                        localFile.mkdirs();
                    }
                    // Recursive call
                    downloadDirectory(currentRemoteFilePath, localFile.getAbsolutePath());
                } else {
                    // Download the file
                    downloadFile(currentRemoteFilePath, localFile.getAbsolutePath());
                }
            }
        }
	}
	
	
	public static void main(String[] args) {
		
        String server = "192.168.115.176";
        
        int port = 21; // Default FTP port
        String user = "anonymous";
        String password = "";
        
        FTPClient ftpClient = new FTPClient(server, port, user, password);
        try {
			ftpClient.open();
			
			// List files on the server
			String path = "";
			Collection<String> fileList = ftpClient.listFiles(path);
			
			
			if (fileList != null) {
				System.out.println("fileList != null");	
			}else {
				System.out.println("fileList = null");
			}
			
			if (!fileList.isEmpty()) {
				System.out.println("!fileList.isEmpty()");
			}else {
				System.out.println("fileList.isEmpty()");
			}
			
			if(fileList != null && !fileList.isEmpty()) {
				System.out.println("Files in" + path + ":" );
				for (String fileName : fileList) {
					System.out.println(fileName);
				}
			}else {
				System.out.println("No files found in " + path );
			}
			
			
            ftpClient.close();
			
			
			
			
			
		} catch (IOException e) {
			System.out.println("Cannot open connection...");
			e.printStackTrace();
		}
        
        
		
	}
		
	

}
