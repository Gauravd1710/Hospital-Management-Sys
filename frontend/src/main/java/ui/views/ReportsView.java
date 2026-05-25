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
import ui.AppTheme;

public class ReportsView {

    public static StackPane getView() {

        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(500);

        // Icon mark: green gradient + bar chart bars
        StackPane mark = new StackPane();
        mark.setPrefSize(72, 72); mark.setMaxSize(72, 72);

        Rectangle bg = new Rectangle(72, 72);
        bg.setArcWidth(22); bg.setArcHeight(22);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#2ED48A")),
            new Stop(1, Color.web("#158A5B"))
        ));

        Rectangle b1 = new Rectangle(10, 16);
        b1.setArcWidth(4); b1.setArcHeight(4);
        b1.setFill(Color.WHITE); b1.setTranslateX(-16); b1.setTranslateY(6);

        Rectangle b2 = new Rectangle(10, 28);
        b2.setArcWidth(4); b2.setArcHeight(4);
        b2.setFill(Color.WHITE); b2.setTranslateY(0);

        Rectangle b3 = new Rectangle(10, 20);
        b3.setArcWidth(4); b3.setArcHeight(4);
        b3.setFill(Color.WHITE); b3.setTranslateX(16); b3.setTranslateY(4);

        mark.getChildren().addAll(bg, b1, b2, b3);

        Region divider = new Region();
        divider.setPrefSize(60, 3); divider.setMaxWidth(60);
        divider.setStyle("-fx-background-color: " + AppTheme.C_GREEN + "; -fx-background-radius: 2;");

        Label title = new Label("Reports & Analytics");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(AppTheme.C_TEXT_DARK));

        Label sub = new Label("Reporting and analytics features coming soon.");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));

        content.getChildren().addAll(mark, title, divider, sub);

        StackPane pane = new StackPane(content);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: " + AppTheme.C_PAGE_BG + ";");
        return pane;
    }
}