package co.edu.uniquindio.sockets.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.sockets.AhorcadoCliente;
import co.edu.uniquindio.sockets.AhorcadoServidor;
import co.edu.uniquindio.sockets.interfaces.Observer;
import co.edu.uniquindio.sockets.interfaces.ObserverContador;
import co.edu.uniquindio.sockets.interfaces.ObserverContadorCliente;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClienteVistaController implements Observer, ObserverContadorCliente {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label TituloAhorcado;

    @FXML
    private Button buttonEnviar;

    @FXML
    private Label palabraAhorcado;

    @FXML
    private TextField textFieldLetra;

    @FXML
    private Label textIntentosRestantes;

    @FXML
    void onEnviar(ActionEvent event) {
        String letra = textFieldLetra.getText();
        if (!letra.isEmpty()) {
            if(letra.length() > 1){
                letra = letra.substring(0, 1);
            }
            AhorcadoCliente.enviarMensaje(letra);
            textFieldLetra.clear();
        }
    }

    @FXML
    void initialize() {
        AhorcadoCliente.addObserver(this);
        AhorcadoCliente.addObserverContador(this);
    }

    @Override
    public void update(String palabraOculta) {
        Platform.runLater(()-> palabraAhorcado.setText(palabraOculta));
    }

    @Override
    public void update(int contador) {
        Platform.runLater(() -> textIntentosRestantes.setText("Intentos restantes: " + contador));
    }
}