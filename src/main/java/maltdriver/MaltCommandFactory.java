package maltdriver;

import settings.Calib;
import settings.CalibrationSettings;
import settings.Option;
import settings.OptionSettings;
import settings.Param;
import settings.TestSettings;

/*=====================================================================*/
class MaltCommandFactory {
	
	static public Command start() {
		return MaltCommand.Start; 
	}

	static public Command reset() {
		return MaltCommand.Reset;
	}

	static public Command saveSettings() {
		return MaltCommand.SaveToMemory;
	}

	static public Command loadSettings() {
		return MaltCommand.LoadFromMemory;
	}

	static public Command selectTest(int testIndex) {
		return new IndexedCommand(MaltCommand.SelectTest, testIndex);
	}
	
	static public Command exportSettings(String filename) { //java.nio.Path?
		return new FileCommand(MaltCommand.FileExport, filename);
	}
	
	static public Command importSettings(String filename) { //java.nio.Path?
		return new FileCommand(MaltCommand.FileImport, filename);
	}

	static public Command settings(TestSettings settings) {
		return new SettingsCommand<Param>(MaltCommand.Parameters, settings);
	}
	
	static public Command settings(OptionSettings settings) {
		return new SettingsCommand<Option>(MaltCommand.Options, settings);
	}
	
	static public Command settings(CalibrationSettings settings) {
		return new SettingsCommand<Calib>(MaltCommand.Calibration, settings);
	}

	static public Command getStep() {
		return MaltCommand.QStep;
	}
	
	static public Command getIndicators() {
		return MaltCommand.QIndicators;
	}

	static public Command getTestIndex() {
		return MaltCommand.QTestIndex;
	}

	static public Command getResult() {
		return MaltCommand.QResultCode;
	}
	
	static public Command getTestState() {
		return MaltCommand.QTeststate;
	}
	
	static public Command getDiffPAZ() {
		return MaltCommand.QDiffPAZ;
	}
	
	static public Command getDataTestP() {
		return MaltCommand.QDataTestP;
	}
	
	static public Command getDataDiffP() {
		return MaltCommand.QDataDiffP;
	}
	
	
	static public Command getTestData() {
		return MaltCommand.QLastData;
	}

	static public Command getOptions() {
		return MaltCommand.QOptions;
	}

	static public Command getParameters(int testIndex) {
		return new IndexedCommand(MaltCommand.QParameters, testIndex);
	}
	
	static public Command getCalibration(CalibrationSettings.Channel channel) {
		return new IndexedCommand(MaltCommand.QCalibration, channel.ordinal());
	}
	
	static public Command userCommand(String command) {
		return new UserCommand(command);
	}
	
}


