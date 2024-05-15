module com.example.network {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;


    //opens com.example.network to javafx.fxml;
   // exports com.example.network;
    exports com.example.network.client;
    opens com.example.network.client to javafx.fxml;
    //exports com.example.network.server;
   // opens com.example.network.server to javafx.fxml;
}