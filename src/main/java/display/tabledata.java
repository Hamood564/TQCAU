package display;

import javafx.beans.property.SimpleStringProperty;

public class tabledata {
	
	private SimpleStringProperty config, result;

	public tabledata(String config, String result) {
		super();
		this.config = new SimpleStringProperty(config);
		this.result = new SimpleStringProperty(result);
	}

	public String getConfig() {
		return config.get();
	}

	public void setConfig(SimpleStringProperty config) {
		this.config = config;
	}

	public String getResult() {
		return result.get();
	}

	public void setResult(SimpleStringProperty result) {
		this.result = result;
	}
}
