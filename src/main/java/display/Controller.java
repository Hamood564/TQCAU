package display;



import java.net.URL;
import java.util.ResourceBundle;

import application.MALTCall;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable{

    @FXML
    private TextField hostTextField;

    @FXML
    private TextField portTextField;
    
    @FXML
    private Button saveSettingbtn;
    
    @FXML
    private TextField DiffPressureField;

    @FXML
    private TextField EvacTimeField;

    @FXML
    private TextField FillTimeField;

    @FXML
    private TextField IsolDelayField;

    @FXML
    private TextField LeakRateField;

    @FXML
    private TextField MeasureTimeField;

    @FXML
    private TextField StabTimeField;

    @FXML
    private TextField TestIdField;

    @FXML
    private TextField TestNumberField;

    @FXML
    private TextField TestPressureField;
    
    @FXML
    private TableView<tabledata> resulttable;
    
    @FXML
    private TableColumn<tabledata,String> configuration;
    
    @FXML
    private TableColumn<tabledata,String> result;
    
    @FXML
    private ChoiceBox<String> testIndexField;
    private String[] testIndex = {"0","1","2","3","4","5","6","7","8","9","10",
    		"11","12","13","14","15"};
    
    
    private Integer testIdx;
    
    @FXML
    void connectMALT(ActionEvent event) {
    	System.out.println("Host Text Field is:"+ hostTextField.getText());
    	System.out.println("Port Text Field is:"+ portTextField.getText());
    	MALTCall.connectMalt(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
    	System.out.println("Malt is connected..");
    }

    @FXML
    void maltReset(ActionEvent event) {
    	MALTCall.maltreset();

    }

    @FXML
    public void maltStart(ActionEvent event) {
    	System.out.println("Starting test with idx.."+testIdx.toString());
    	MALTCall.runtestMaltwithIdx(testIdx);
    	
    	if (!MALTCall.MaltNotBusy()) {
    		try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	configuration.setCellValueFactory(new PropertyValueFactory<tabledata,String>("config"));
		result.setCellValueFactory(new PropertyValueFactory<tabledata,String>("result"));
		resulttable.setItems(getcfgresult());
    	
    	
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		testIndexField.getItems().addAll(testIndex);
		testIndexField.setOnAction(this::setIndex);
		
			}
	
	private ObservableList<tabledata> getcfgresult() {
		
		ObservableList<tabledata> data = FXCollections.observableArrayList();
		data.add(new tabledata(MALTCall.getTestConfigSetting(testIdx), MALTCall.getLastResult()));
		data.add(new tabledata("testcfg2", "result2"));
		return data;
	}

	public void setIndex(ActionEvent event) {
		String testIdx = testIndexField.getValue();
		this.testIdx = Integer.parseInt(testIdx);
	}
	
	@FXML
    void saveSetting(ActionEvent event) {
		System.out.println("Saving setting to Test Number" + TestNumberField.getText());
		MALTCall.saveSetting(Integer.parseInt(TestNumberField.getText()), TestIdField.getText(), TestPressureField.getText(), FillTimeField.getText(),
				StabTimeField.getText(), IsolDelayField.getText(), MeasureTimeField.getText(), EvacTimeField.getText(), LeakRateField.getText(), 
				DiffPressureField.getText());

    }
	
    
   

}
