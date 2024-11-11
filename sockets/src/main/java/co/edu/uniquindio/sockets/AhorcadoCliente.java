package co.edu.uniquindio.sockets;

import co.edu.uniquindio.sockets.interfaces.Observer;
import co.edu.uniquindio.sockets.interfaces.ObserverContadorCliente;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class AhorcadoCliente extends Application {

    private static final String HOST = "localhost";
    private static final int PUERTO = 12345;

    private static Socket socket;
    private static DataOutputStream out;
    static DataInputStream in;
    private static ArrayList<Observer> observers = new ArrayList<>();
    private static ArrayList<ObserverContadorCliente> observersContador = new ArrayList<>();
    private static String palabraActual;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cliente-vista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Ahorcado Cliente");
        stage.setScene(scene);
        stage.show();


    }

    public static void enviarMensaje(String mensaje) {
        try {
            out.writeUTF(mensaje);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Thread(() -> {
            try {
                socket = new Socket(HOST, PUERTO);
                System.out.println("Conexión establecida con el servidor.");

                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());

                while (true) {
                    String mensaje = in.readUTF();
                    if (mensaje.startsWith("intentos")) {
                        int contador = Integer.parseInt(mensaje.substring(8));
                        notifyObserversContador(contador);
                    } else {
                        setPalabraActual(mensaje);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error en la conexión con el servidor:");
                e.printStackTrace();
            }
        }).start();

        launch(args);
    }


    public static void addObserver(Observer observer) {
        observers.add(observer);
    }

    public static void notifyObservers(String palabraOculta) {
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.update(palabraOculta);
            }
        });
    }

    public static void addObserverContador(ObserverContadorCliente observer) {
        observersContador.add(observer);
    }

    public static void notifyObserversContador(int contador) {
        Platform.runLater(() -> {
            for (ObserverContadorCliente observer : observersContador) {
                observer.update(contador);
            }
        });
    }


    public static void setPalabraActual(String palabraActual) {
        AhorcadoCliente.palabraActual = palabraActual;
        notifyObservers(palabraActual);
    }
}