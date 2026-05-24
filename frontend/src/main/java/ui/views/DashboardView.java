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

public class DashboardView {

    private static final String C_PAGE_BG    = "#F0F4F8";
    private static final String C_WHITE      = "#FFFFFF";
    private static final String C_BLUE       = "#2B7FD4";
    private static final String C_BLUE_DARK  = "#1B5FA8";
    private static final String C_BLUE_LIGHT = "#EBF4FF";
    private static final String C_TEXT_DARK  = "#1A2332";
    private static final String C_TEXT_LIGHT = "#718096";
    private static final String C_BORDER     = "#E2E8F0";

    public static StackPane getView() {

        VBox content = new VBox(14);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(460);

        // ── Logo mark (same as header, for consistency) ──────────────────
        StackPane logoMark = new StackPane();
        logoMark.setPrefSize(72, 72);
        logoMark.setMaxSize(72, 72);

        Rectangle bg = new Rectangle(72, 72);
        bg.setArcWidth(20); bg.setArcHeight(20);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4A9FE8")),
            new Stop(1, Color.web(C_BLUE_DARK))
        ));

        Rectangle hBar = new Rectangle(32, 7);
        hBar.setArcWidth(4); hBar.setArcHeight(4);
        hBar.setFill(Color.WHITE);

        Rectangle vBar = new Rectangle(7, 32);
        vBar.setArcWidth(4); vBar.setArcHeight(4);
        vBar.setFill(Color.WHITE);

        logoMark.getChildren().addAll(bg, hBar, vBar);

        // ── Text ──────────────────────────────────────────────────────────
        Label title = new Label("Dashboard Overview");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        title.setTextFill(Color.web(C_TEXT_DARK));

        // Accent underline pill
        Region pill = new Region();
        pill.setPrefSize(48, 4);
        pill.setMaxWidth(48);
        pill.setStyle("-fx-background-color: " + C_BLUE + "; -fx-background-radius: 2;");

        Label sub = new Label("Select a section from the top navigation to get started.");
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