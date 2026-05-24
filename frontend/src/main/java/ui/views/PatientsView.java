package ui.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PatientsView {

    private static final String C_PAGE_BG    = "#F0F4F8";
    private static final String C_TEAL       = "#0F9D8A";
    private static final String C_TEAL_DARK  = "#0A7A6B";
    private static final String C_TEXT_DARK  = "#1A2332";
    private static final String C_TEXT_LIGHT = "#718096";

    public static StackPane getView() {

        VBox content = new VBox(14);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(460);

        StackPane logoMark = new StackPane();
        logoMark.setPrefSize(72, 72);
        logoMark.setMaxSize(72, 72);

        Rectangle bg = new Rectangle(72, 72);
        bg.setArcWidth(20); bg.setArcHeight(20);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1BC9B2")),
            new Stop(1, Color.web(C_TEAL_DARK))
        ));

        // Person silhouette using stacked rectangles: head circle + body rect
        StackPane personIcon = new StackPane();

        Rectangle head = new Rectangle(20, 20);
        head.setArcWidth(20); head.setArcHeight(20);
        head.setFill(Color.WHITE);
        head.setTranslateY(-14);

        Rectangle body = new Rectangle(28, 18);
        body.setArcWidth(14); body.setArcHeight(14);
        body.setFill(Color.WHITE);
        body.setTranslateY(10);

        personIcon.getChildren().addAll(head, body);
        logoMark.getChildren().addAll(bg, personIcon);

        Label title = new Label("Patients Management");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(C_TEXT_DARK));

        Region pill = new Region();
        pill.setPrefSize(48, 4);
        pill.setMaxWidth(48);
        pill.setStyle("-fx-background-color: " + C_TEAL + "; -fx-background-radius: 2;");

        Label sub = new Label("Patient management features will be available here.");
        sub.setFont(Font.font("Arial", 13));
        sub.setTextFill(Color.web(C_TEXT_LIGHT));
        sub.setWrapText(true);
        sub.setAlignment(Pos.CENTER);

        content.getChildren().addAll(logoMark, title, pill, sub);

        StackPane pane = new StackPane(content);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: " + C_PAGE_BG + ";");
        return pane;
    }
}