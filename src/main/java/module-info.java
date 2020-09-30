module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires poi.ooxml;
    requires poi;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}