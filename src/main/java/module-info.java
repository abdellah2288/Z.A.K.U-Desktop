module com.zaku.desktop.zaku_desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires org.eclipse.paho.client.mqttv3;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fazecast.jSerialComm;

    opens com.zaku_desktop to javafx.fxml;
    exports com.zaku_desktop;
    exports com.zaku_desktop.utilities;
    opens com.zaku_desktop.utilities to javafx.fxml;
}