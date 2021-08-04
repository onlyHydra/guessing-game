package gui;

import domain.Jucator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ExamenException;
import service.Observer;
import service.Service;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MainController extends UnicastRemoteObject implements Observer, Serializable {
    private Service service;
    private Jucator crtJucator;
    //ObservableList<Carte> model = FXCollections.observableArrayList();
    //private List<Carte> carti = new ArrayList<>();
    //private Carte lastChoice;


    @FXML
    TextArea textAreaEndGame;
    @FXML
    TextArea textAreaRez;
    @FXML
    Button button;
    @FXML
    Button btnSend;
    @FXML
    Button btnStart;
    @FXML
    ListView<String> listViewOpponents;
    @FXML
    TextField textFieldCuvant;
    @FXML
    TextField textFieldLitera;
//    @FXML
//    TableView<Carte> tableViewCards;
//    @FXML
//    TableColumn<Carte, String> tableColumnDenumire;

    public MainController() throws RemoteException {
    }


    @FXML
    public void initialize() {
        //tableColumnDenumire.setCellValueFactory(new PropertyValueFactory<Carte, String>("denumire"));
        //tableViewCards.setItems(model);
    }


    @FXML
    public void trimiteLitera(){
        try {
            String c1=textFieldLitera.getText();
            if(c1.length()==1) {
                service.joacaRunda(crtJucator, listViewOpponents.getSelectionModel().getSelectedItem(), c1);
                btnSend.setDisable(true);
            } else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Error!", "Plase send a letter!");

        } catch (Exception e) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Error!", e.getMessage());
        }

    }

    @FXML
    public void startJoc() {
        String cuvant=textFieldCuvant.getText();
        if(cuvant.length()>=6) {
            try {
                service.start(this.crtJucator,cuvant);
                btnStart.setDisable(true);
            } catch (ExamenException e) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Start failed!", e.getMessage());
            }
        }
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Start failed!", "Please insert a word of minimum 6 letters!");

    }


    @FXML
    public void initModel() {

    }



    @FXML
    public void logOut(){
        try {

            service.logout(crtJucator, this);
            Stage stage = (Stage)button.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/logInView.fxml"));
            Parent root = loader.load();

            LogInController logInController = loader.getController();
            logInController.setService(service);

            FXMLLoader cloader = new FXMLLoader();
            cloader.setLocation(getClass().getResource("/views/mainView.fxml"));
            Parent croot=cloader.load();


            MainController chatCtrl =
                    cloader.<MainController>getController();
            chatCtrl.setService(service);

            logInController.setMainController(chatCtrl);
            logInController.setParent(croot);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Log in:");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            dialogStage.show();

            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ExamenException e){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Iesire esuata!", e.getMessage());

        }
    }

    public void setService(Service service) {
        this.service=service;
        listViewOpponents.setDisable(true);
        //butonAlege.setDisable(true);


    }
    public void setUser(Jucator user) {
        this.crtJucator = user;
    }

    @Override
    public void notifyEndGame(String a) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textAreaEndGame.clear();
                textAreaEndGame.setText(a);
                btnSend.setDisable(true);
                btnStart.setDisable(true);
            }
        });
    }


    @Override
    public void notifyNewGame(List<Jucator> readyToPlay, String cuv) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (listViewOpponents.getItems().size() > 0)
                    listViewOpponents.getItems().clear();
                for(Jucator j:readyToPlay)
                    listViewOpponents.getItems().add(j.getId());
                listViewOpponents.getItems().remove(crtJucator.getId());
                listViewOpponents.setDisable(false);
                textAreaRez.clear();
                textAreaRez.setText(cuv);
                textFieldCuvant.clear();
                btnSend.setDisable(false);
            }
        });
    }

    @Override
    public void notifyWinner(String a) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                textAreaRez.clear();
                textAreaRez.setText(a);
                btnSend.setDisable(false);
            }
        });

    }


}