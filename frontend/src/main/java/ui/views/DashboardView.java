package ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ui.AppTheme;

public class DashboardView {

    public static StackPane getView() {

        VBox content = new VBox(18);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(500);

        // Large NexaCare logo (reused from AppTheme)
        HBox logoGroup = AppTheme.buildLogoGroup(true);
        logoGroup.setAlignment(Pos.CENTER);

        // Gradient accent divider
        Region divider = new Region();
        divider.setPrefSize(60, 3);
        divider.setMaxWidth(60);
        divider.setStyle(
            "-fx-background-color: linear-gradient(to right, " +
            AppTheme.C_BLUE + ", " + AppTheme.C_TEAL + ");" +
            "-fx-background-radius: 2;"
        );

        Label title = new Label("Welcome to NexaCare");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(AppTheme.C_TEXT_DARK));

        Label sub = new Label("Your smart, next-generation hospital management platform.");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));
        sub.setWrapText(true);
        sub.setAlignment(Pos.CENTER);

        // Feature pills
        HBox pills = new HBox(12);
        pills.setAlignment(Pos.CENTER);
        pills.getChildren().addAll(
            featurePill("Patient Tracking", AppTheme.C_BLUE,  AppTheme.C_BLUE_LIGHT),
            featurePill("Bed Management",   AppTheme.C_TEAL,  AppTheme.C_TEAL_LIGHT),
            featurePill("Smart Scheduling", AppTheme.C_GREEN, AppTheme.C_GREEN_LIGHT)
        );

        content.getChildren().addAll(logoGroup, divider, title, sub, pills);

        StackPane pane = new StackPane(content);
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: " + AppTheme.C_PAGE_BG + ";");
        return pane;
    }

    private static Label featurePill(String text, String fg, String bg) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.web(fg));
        lbl.setPadding(new Insets(5, 14, 5, 14));
        lbl.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + fg + ";" +
            "-fx-border-radius: 14; -fx-border-width: 1;"
        );
        return lbl;
    }
}