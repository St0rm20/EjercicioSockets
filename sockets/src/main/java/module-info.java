module co.edu.uniquindio.sockets {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens co.edu.uniquindio.sockets to javafx.fxml;
    exports co.edu.uniquindio.sockets;
    exports co.edu.uniquindio.sockets.controladores;
    opens co.edu.uniquindio.sockets.controladores to javafx.fxml;
    exports co.edu.uniquindio.sockets.interfaces;
    opens co.edu.uniquindio.sockets.interfaces to javafx.fxml;
}