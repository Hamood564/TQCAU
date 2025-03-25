package maltdriver;

import settings.SettingsBuilder;

interface Command {

	public String getCommand();
	
}

enum MaltCommand implements Command {
	Start("MS"), 
	Reset("MR"),
	Abort("MA"),
	SelectTest("MT"),
	Parameters("P"),
	Calibration("C"),
	Options("O"),
	FileExport("FE"),
	FileImport("FI"),
	SaveToMemory("FS"),
	LoadFromMemory("FL"),
	//Query
	QStep("?S"),
	QIndicators("?I"),
	QTeststate("?Q"),
	QDiffPAZ("?Z"),	
	QDataTestP("?L"),
	QDataDiffP("?M"),
	QTestIndex("?T"),
	QResultCode("?R"),
	QLastData("?D"),
	QParameters("?P"),
	QCalibration("?C"),
	QOptions("?O");
	

	private String cmd;
	
	private MaltCommand(String cmd) {
		this.cmd = cmd;
	}
	
	public String getCommand() {
		return cmd;
	}

}

/*=====================================================================*/
class IndexedCommand implements Command {
	
	protected Command cmd;
	protected int index;
	
	public IndexedCommand() {
		this(MaltCommand.SelectTest, 0);
	}

	public IndexedCommand(Command cmd) {
		this(cmd, 0);
	}
	
	public IndexedCommand(Command cmd, int index) {
		this.index = index;
		this.cmd = cmd;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex(int index) {
		return this.index;
	}
	
	@Override
	public String getCommand() {
		return cmd.getCommand() + this.index;
	}

}

/*=====================================================================*/
class FileCommand implements Command {
	
	private Command cmd;
	private String filename;
	
	public FileCommand() {
		this(MaltCommand.FileExport,"params.txt");
	}

	public FileCommand(Command cmd, String filename) {
		this.cmd = cmd;
		this.filename = filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getCommand() {
		return this.cmd.getCommand() + this.getFilename();
	}

}

/*=====================================================================*/
class SettingsCommand<T extends Enum<T>> implements Command {
	
	protected Command cmd;
	protected SettingsBuilder<T> sb;
	
	public SettingsCommand(Command cmd, SettingsBuilder<T> sb) {
		this.cmd = cmd;
		this.sb = sb;
	}

	public void setSettings(SettingsBuilder<T> sb) {
		this.sb = sb;
	}

	public SettingsBuilder<T> getSettings() {
		return this.sb;
	}
	
	@Override
	public String getCommand() { 
		return cmd.getCommand() + sb.build();
	}
}

/*=====================================================================*/
/*=====================================================================*/
class UserCommand implements Command {
	
//	private Command cmd;
	private String command;
	
	public UserCommand() {
		this("");
	}

	public UserCommand(String command) {
		this.command = command;
	}

	@Override
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String toString() {
		return "UserCommand [command=" + command + "]";
	}

}
