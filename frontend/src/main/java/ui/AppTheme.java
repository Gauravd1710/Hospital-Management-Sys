package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Shared palette constants and logo builder for NexaCare.
 * All members are public so ui.views.* can access them freely.
 */
public final class AppTheme {

    private AppTheme() {}

    // ── Palette ───────────────────────────────────────────────────────────
    public static final String C_PAGE_BG      = "#F0F4F8";
    public static final String C_WHITE        = "#FFFFFF";
    public static final String C_BLUE         = "#2B7FD4";
    public static final String C_BLUE_DARK    = "#1B5FA8";
    public static final String C_BLUE_XDARK   = "#134080";
    public static final String C_BLUE_LIGHT   = "#EBF4FF";
    public static final String C_BLUE_MID     = "#D0E8FF";
    public static final String C_TEAL         = "#0F9D8A";
    public static final String C_TEAL_LIGHT   = "#E1F5F2";
    public static final String C_GREEN        = "#1DB87A";
    public static final String C_GREEN_LIGHT  = "#E6F9F1";
    public static final String C_RED          = "#E24B4A";
    public static final String C_RED_LIGHT    = "#FCEBEB";
    public static final String C_TEXT_DARK    = "#1A2332";
    public static final String C_TEXT_MED     = "#4A5568";
    public static final String C_TEXT_LIGHT   = "#718096";
    public static final String C_BORDER       = "#E2E8F0";

    /**
     * Builds the NexaCare logo group: gradient mark with ECG pulse line
     * + two-tone "NexaCare" wordmark + tagline.
     *
     * @param large  true = 64 px mark (splash pages), false = 40 px (header)
     */
    public static HBox buildLogoGroup(boolean large) {

        int    markSize = large ? 64 : 40;
        int    nameSize = large ? 26 : 17;
        int    tagSize  = large ? 12 : 10;
        double strokeW  = large ? 2.8 : 1.8;

        // ── Mark: gradient rounded square ────────────────────────────────
        StackPane mark = new StackPane();
        mark.setPrefSize(markSize, markSize);
        mark.setMinSize(markSize, markSize);

        Rectangle bg = new Rectangle(markSize, markSize);
        bg.setArcWidth(markSize * 0.30);
        bg.setArcHeight(markSize * 0.30);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#3D9BE9")),
            new Stop(0.5, Color.web(C_BLUE)),
            new Stop(1.0, Color.web(C_BLUE_XDARK))
        ));

        // ECG heartbeat pulse line (9 white rounded rectangles)
        double cx = markSize / 2.0;
        double cy = markSize / 2.0;
        double w  = markSize * 0.72;
        double h  = markSize * 0.44;

        Rectangle seg1 = seg(cx - w/2,              cy,         w*0.22,          strokeW);
        Rectangle seg2 = seg(cx - w/2 + w*0.22 - strokeW/2, cy - h,   strokeW,  h);
        Rectangle seg3 = seg(cx - w/2 + w*0.22 - strokeW/2, cy - h,   w*0.18,   strokeW);
        Rectangle seg4 = seg(cx - w/2 + w*0.40,     cy - h,     strokeW,          h + h*0.6 + strokeW);
        Rectangle seg5 = seg(cx - w/2 + w*0.40,     cy + h*0.6, w*0.16,           strokeW);
        Rectangle seg6 = seg(cx - w/2 + w*0.56 - strokeW/2, cy - h*0.45, strokeW, h*0.45 + strokeW*0.5);
        Rectangle seg7 = seg(cx - w/2 + w*0.56,     cy - h*0.45, w*0.10,          strokeW);
        Rectangle seg8 = seg(cx - w/2 + w*0.66,     cy - h*0.45, strokeW,         h*0.45 + strokeW*0.5);
        Rectangle seg9 = seg(cx - w/2 + w*0.66,     cy,          w*0.34,           strokeW);

        // Teal pulse-dot accent (top-right)
        Circle pulseDot = new Circle(markSize * 0.14);
        pulseDot.setFill(Color.web(C_TEAL));
        pulseDot.setTranslateX(markSize * 0.28);
        pulseDot.setTranslateY(-markSize * 0.28);

        mark.getChildren().addAll(bg,
            seg1, seg2, seg3, seg4, seg5, seg6, seg7, seg8, seg9,
            pulseDot);

        // ── Word-mark: "Nexa" dark + "Care" teal ─────────────────────────
        HBox wordMark = new HBox(0);
        wordMark.setAlignment(Pos.BASELINE_LEFT);

        Label nexa = new Label("Nexa");
        nexa.setFont(Font.font("Georgia", FontWeight.BOLD, nameSize));
        nexa.setTextFill(Color.web(C_TEXT_DARK));

        Label care = new Label("Care");
        care.setFont(Font.font("Georgia", FontWeight.BOLD, nameSize));
        care.setTextFill(Color.web(C_TEAL));

        wordMark.getChildren().addAll(nexa, care);

        Label tagLine = new Label("Smart Hospital Management");
        tagLine.setFont(Font.font("Arial", tagSize));
        tagLine.setTextFill(Color.web(C_TEXT_LIGHT));

        VBox wordStack = new VBox(0, wordMark, tagLine);
        wordStack.setAlignment(Pos.BOTTOM_LEFT);

        HBox group = new HBox(10, mark, wordStack);
        group.setAlignment(Pos.CENTER_LEFT);
        return group;
    }

    /** Convenience: create a white rounded rectangle segment for the ECG line. */
    private static Rectangle seg(double x, double y, double w, double h) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setArcWidth(2); r.setArcHeight(2);
        r.setFill(Color.WHITE);
        return r;
    }
}