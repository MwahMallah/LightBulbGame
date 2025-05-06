module org.vut_ija_project.ija {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires commons.csv;

    exports org.vut_ija_project.ija.Applicaiton;
    opens org.vut_ija_project.ija.Applicaiton to javafx.fxml;
    exports org.vut_ija_project.ija.Controller;
    opens org.vut_ija_project.ija.Controller to javafx.fxml;
    exports org.vut_ija_project.ija.ViewModel;
    opens org.vut_ija_project.ija.ViewModel to javafx.fxml;
}