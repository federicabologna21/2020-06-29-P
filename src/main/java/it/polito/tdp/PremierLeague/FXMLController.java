/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.CoppieMigliori;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<String> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	txtResult.clear();
    	this.doCreaGrafo(event);
    	
    	
    	txtResult.appendText("Le coppie con connessioni migliori sono:\n");
    	for (CoppieMigliori c: this.model.getCoppieMigliori()) {
    		txtResult.appendText(c+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	String mese = this.cmbMese.getValue();
    	int meseScelta = this.mese(mese);
    	
    	String minutiS = this.txtMinuti.getText();
    	 int min;
    	 
    	 if(mese == null) {
    		 txtResult.appendText("selezionare un valore");
    		 return;
    	 }
    	 
    	
    	try {
    		min = Integer.parseInt(minutiS);
    	}catch (NumberFormatException ne) {
    		txtResult.appendText("Errore inserire un numero!");
    		return;
    	}
    	
    	this.model.creaGrafo(min, meseScelta);
    	txtResult.appendText("GRAFO CREATO!\n");
    	txtResult.appendText("# VERTICI: "+this.model.getNumeroVertici()+"\n");
    	txtResult.appendText("# ARCHI: "+this.model.getNumeroArchi()+"\n");
    	
    	
    	this.cmbM1.getItems().addAll(this.model.getVerticiTendina());
    	this.cmbM2.getItems().addAll(this.model.getVerticiTendina());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	
    	Match m1 = this.cmbM1.getValue();
    	Match m2 = this.cmbM2.getValue();
    	
    	// this.model.trovaPercorso(m1, m2);
    	List<Match> risultato = this.model.trovaPercorso(m1, m2);
    	txtResult.appendText("IL PERCORSO CON PESO MASSIMO ( "+this.model.calcolaPeso(risultato)+")  E':\n");
    	
    	for (Match m: risultato) {
    		txtResult.appendText(m+"\n ");
    	}
    	
    
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbMese.getItems().add("Gennaio");
    	this.cmbMese.getItems().add("Febbraio");
    	this.cmbMese.getItems().add("Marzo");
    	this.cmbMese.getItems().add("Aprile");
    	this.cmbMese.getItems().add("Maggio");
    	this.cmbMese.getItems().add("Giugno");
    	this.cmbMese.getItems().add("Luglio");
    	this.cmbMese.getItems().add("Agosto");
    	this.cmbMese.getItems().add("Settembre");
    	this.cmbMese.getItems().add("Ottobre");
    	this.cmbMese.getItems().add("Novembre");
    	this.cmbMese.getItems().add("Dicembre");
    	
    }
    public int mese(String mese) {
    	int mm=0;
    	if(mese.equals("Gennaio"))
    		mm=1;
    	if(mese.equals("Febbraio"))
    		mm=2;
    	if(mese.equals("Marzo"))
    		mm=3;
    	if(mese.equals("Aprile"))
    		mm=4;
    	if(mese.equals("Maggio"))
    		mm=5;
    	if(mese.equals("Giugno"))
    		mm=6;
    	if(mese.equals("Luglio"))
    		mm=7;
    	if(mese.equals("Agosto"))
    		mm=8;
    	if(mese.equals("Settembre"))
    		mm=9;
    	if(mese.equals("Ottobre"))
    		mm=10;
    	if(mese.equals("Novembre"))
    		mm=11;
    	if(mese.equals("Dicembre"))
    		mm=12;
    	return mm;
    }
    
    
}
