package co.edu.uniquindio.sockets;

import co.edu.uniquindio.sockets.interfaces.ObserverContador;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AhorcadoServidor extends Application {

    private static final int PUERTO = 12345; // Puerto del servidor
    public static ArrayList<Socket> usuarios = new ArrayList<>(); // Lista de usuarios conectados
    public static ArrayList<DataOutputStream> clientes = new ArrayList<>(); // Lista de streams de salida
    private static String palabraActual;
    private static StringBuilder palabraOculta;
    private static int intentos;
    private static boolean juegoTerminado = false;
    private static ArrayList<ObserverContador> observers = new ArrayList<>();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AhorcadoServidor.class.getResource("servidor-vista.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Ahorcado");
        stage.setScene(scene);
        stage.show();
    }



    //------------------------------- Sockets --------------------------------

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PUERTO);
                System.out.println("Servidor iniciado en el puerto " + PUERTO);

                while (true) {


                    Socket cliente = serverSocket.accept();
                    usuarios.add(cliente);
                    DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                    clientes.add(out);
                    new Thread(() -> {
                        try (DataInputStream in = new DataInputStream(cliente.getInputStream())) {
                            while (true) {
                                String letra = in.readUTF().toLowerCase();
                                actualizarPalabra(letra);
                                enviarActualizacion();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        launch();
    }

    public static void seleccionarPalabra(String palabra) throws IOException {
        if(palabra.equals("intentos")){
            System.out.println("Palabra no válida");
            return;
        }
        intentos = 6;
        notifyObservers(intentos);
        juegoTerminado = false;
        palabraActual = palabra.toLowerCase();
        palabraOculta = new StringBuilder("*".repeat(palabra.length()));
        System.out.println("Palabra seleccionada: " + palabra);
        enviarActualizacion();
    }

    private static void enviarActualizacion() throws IOException {
        for (DataOutputStream cliente : clientes) {
            cliente.writeUTF(palabraOculta.toString());
            cliente.writeUTF("intentos"+ intentos);
        }
    }


    private static void actualizarPalabra(String letra) throws IOException {
        if (juegoTerminado) {
            return;
        }
        letra = letra.toLowerCase();
        boolean acierto = false;
        for (int i = 0; i < palabraActual.length(); i++) {
            if (palabraActual.charAt(i) == letra.charAt(0)) {
                if (palabraOculta.charAt(i) != letra.charAt(0)) {
                    palabraOculta.setCharAt(i, letra.charAt(0));
                    acierto = true;
                }
            }
        }
        if (palabraOculta.toString().equals(palabraActual)) {
            palabraOculta = new StringBuilder("Ganaste");
            juegoTerminado = true;
        }
        if (!acierto) {
            intentos--;
            notifyObservers(intentos);
            if (intentos == 0) {
                palabraOculta = new StringBuilder("Perdiste");
                juegoTerminado = true;
            }
        }
        enviarActualizacion();
    }

    public static void addObserver(ObserverContador observer) {
        observers.add(observer);
    }

    public static void notifyObservers(int intentos) {
        Platform.runLater(() -> {
            for (ObserverContador observer : observers) {
                observer.update(intentos);
            }
        });
    }

}