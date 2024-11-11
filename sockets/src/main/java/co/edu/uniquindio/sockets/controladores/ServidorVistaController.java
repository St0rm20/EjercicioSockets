// In ServidorVistaController.java
package co.edu.uniquindio.sockets.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import co.edu.uniquindio.sockets.AhorcadoServidor;
import co.edu.uniquindio.sockets.interfaces.ObserverContador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServidorVistaController implements ObserverContador {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label TituloAhorcado;

    @FXML
    private Button buttonEnviar;

    @FXML
    private TextField textFieldPalabra;

    @FXML
    private Label textIntentosRestantes;

    @FXML
    void onEnviar() throws IOException {
        String palabra = textFieldPalabra.getText();
        textFieldPalabra.setText("");
        AhorcadoServidor.seleccionarPalabra(palabra);
    }

    @FXML
    void initialize() {
        AhorcadoServidor.addObserver(this);
    }

    @Override
    public void update(int contador) {
        Platform.runLater(() -> textIntentosRestantes.setText("Intentos restantes: " + contador));
    }
}