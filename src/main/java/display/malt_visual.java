package display;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class malt_visual extends Application{
	
	 @Override
	    public void start(Stage primaryStage) throws Exception{
	        Parent root = FXMLLoader.load(getClass().getResource("hellofx2.fxml"));
	        primaryStage.setTitle("TQC MALT Utility");
	        primaryStage.setScene(new Scene(root, 400, 300));
	        primaryStage.show();
	    }
	
	 public static void main(String[] args) {
	        launch(args);
	    }

}
