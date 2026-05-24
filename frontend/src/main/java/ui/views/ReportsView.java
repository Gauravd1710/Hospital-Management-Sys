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

public class ReportsView {

    private static final String C_PAGE_BG    = "#F0F4F8";
    private static final String C_GREEN      = "#1DB87A";
    private static final String C_GREEN_DARK = "#158A5B";
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
            new Stop(0, Color.web("#2ED48A")),
            new Stop(1, Color.web(C_GREEN_DARK))
        ));

        // Bar chart icon: three rectangles of different heights
        Rectangle bar1 = new Rectangle(8, 14);
        bar1.setArcWidth(3); bar1.setArcHeight(3);
        bar1.setFill(Color.WHITE);
        bar1.setTranslateX(-12); bar1.setTranslateY(6);

        Rectangle bar2 = new Rectangle(8, 24);
        bar2.setArcWidth(3); bar2.setArcHeight(3);
        bar2.setFill(Color.WHITE);
        bar2.setTranslateY(1);

        Rectangle bar3 = new Rectangle(8, 18);
        bar3.setArcWidth(3); bar3.setArcHeight(3);
        bar3.setFill(Color.WHITE);
        bar3.setTranslateX(12); bar3.setTranslateY(4);

        logoMark.getChildren().addAll(bg, bar1, bar2, bar3);

        Label title = new Label("Reports & Analytics");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(C_TEXT_DARK));

        Region pill = new Region();
        pill.setPrefSize(48, 4);
        pill.setMaxWidth(48);
        pill.setStyle("-fx-background-color: " + C_GREEN + "; -fx-background-radius: 2;");

        Label sub = new Label("Reporting and analytics features will be available here.");
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