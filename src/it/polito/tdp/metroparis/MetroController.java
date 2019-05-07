package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MetroController {
	
	private Model model = new Model();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Fermata> selectSource;

    @FXML
    private ChoiceBox<Fermata> selectTarget;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnTrova;

    @FXML
    void doTrovaPercorso(ActionEvent event) {
    	
    	txtResult.clear();
    	model.creaGrafo();
    	Fermata source = selectSource.getValue();
    	Fermata target = selectTarget.getValue();
    	if(source.equals(target)) {
    		txtResult.appendText(">>>>Sei già qui!<<<< ");
    	}else {
    		List<Fermata> raggiungibili = model.fermateRaggiungibili(source);
        	if(!raggiungibili.contains(target)) {
        		txtResult.appendText("Partendo dalla stazione '"+source+"' non è possibile raggiungere '"+target+"'\n");
        	}else {
        		List<Fermata> percorso = model.percorsoFinoA(target);
        		int cont = 1;
        		txtResult.appendText("Devi seguire il seguente percorso:\n");
        		for(Fermata f : percorso) {
        			
        			txtResult.appendText(cont+". "+f+"\n");
        			cont++;
        		}
    	}
    	
    	}

    }

    @FXML
    void initialize() {
        assert selectSource != null : "fx:id=\"selectSource\" was not injected: check your FXML file 'Metro.fxml'.";
        assert selectTarget != null : "fx:id=\"selectTarget\" was not injected: check your FXML file 'Metro.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Metro.fxml'.";
        assert btnTrova != null : "fx:id=\"btnTrova\" was not injected: check your FXML file 'Metro.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(Fermata f : model.getAllFermate()) {
    		selectSource.getItems().addAll(f);
    		selectTarget.getItems().addAll(f);
    	}
    }
}
