package ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import ui.views.PatientsView;
import ui.views.ReportsView;

// ===================== BACKEND =====================

abstract class Patient {
    String id, name, diagnosis;

    public Patient(String id, String name, String diagnosis) {
        this.id = id; this.name = name; this.diagnosis = diagnosis;
    }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public String getDiagnosis() { return diagnosis; }

    public abstract String getDetails();
    public abstract String getType();
}

class Beds extends Patient {
    String roomNumber;
    public Beds(String id, String name, String diagnosis, String roomNumber) {
        super(id, name, diagnosis);
        this.roomNumber = roomNumber;
    }
    @Override public String getDetails() { return "Room " + roomNumber; }
    @Override public String getType()    { return "BED"; }
}

class Appointment extends Patient {
    String appointmentDate;
    public Appointment(String id, String name, String diagnosis, String appointmentDate) {
        super(id, name, diagnosis);
        this.appointmentDate = appointmentDate;
    }
    @Override public String getDetails() { return appointmentDate; }
    @Override public String getType()    { return "APPT"; }
}

class Hospital {
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    public void addPatient(Patient p)            { patientList.add(p); }
    public ObservableList<Patient> getPatients() { return patientList; }
}

// ===================== FRONTEND =====================

public class Main extends Application {

    Hospital hospital     = new Hospital();
    int totalBeds         = 0;
    int totalAppointments = 0;

    Label bedCountLabel;
    Label apptCountLabel;
    Label totalCountLabel;

    private StackPane contentArea;

    // ── Palette (unchanged from original) ─────────────────────────────────
    private static final String C_PAGE_BG     = "#F0F4F8";
    private static final String C_WHITE       = "#FFFFFF";
    private static final String C_BLUE        = "#2B7FD4";
    private static final String C_BLUE_DARK   = "#1B5FA8";
    private static final String C_BLUE_LIGHT  = "#EBF4FF";
    private static final String C_TEAL        = "#0F9D8A";
    private static final String C_TEAL_LIGHT  = "#E1F5F2";
    private static final String C_GREEN       = "#1DB87A";
    private static final String C_GREEN_LIGHT = "#E6F9F1";
    private static final String C_RED         = "#E24B4A";
    private static final String C_RED_LIGHT   = "#FCEBEB";
    private static final String C_TEXT_DARK   = "#1A2332";
    private static final String C_TEXT_MED    = "#4A5568";
    private static final String C_TEXT_LIGHT  = "#718096";
    private static final String C_BORDER      = "#E2E8F0";

    @Override
    public void start(Stage stage) {

        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + C_PAGE_BG + ";");

        contentArea = new StackPane();
        contentArea.getChildren().add(buildBody());
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        root.getChildren().addAll(buildHeader(), contentArea);

        Scene scene = new Scene(root, 1200, 760);
        stage.setTitle("MediCare — Hospital Management System");
        stage.setScene(scene);
        stage.setMinWidth(1050);
        stage.setMinHeight(680);
        stage.show();
    }

    // ===================== HEADER =====================

    private HBox buildHeader() {

        HBox header = new HBox();
        header.setPadding(new Insets(0, 28, 0, 24));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(64);
        header.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2);"
        );

        // ── Logo: cross inside rounded square, drawn with JavaFX shapes ──
        StackPane logoMark = buildLogoMark();

        // Brand text stacked: "MediCare" bold + "Hospital Management" small
        VBox brandStack = new VBox(0);
        brandStack.setAlignment(Pos.CENTER_LEFT);
        brandStack.setPadding(new Insets(0, 0, 0, 10));

        Label brandName = new Label("MediCare");
        brandName.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
        brandName.setTextFill(Color.web(C_TEXT_DARK));

        Label brandSub = new Label("Hospital Management");
        brandSub.setFont(Font.font("Arial", 10));
        brandSub.setTextFill(Color.web(C_TEXT_LIGHT));

        brandStack.getChildren().addAll(brandName, brandSub);

        // ── Vertical divider ────────────────────────────────────────────
        Region vDiv = new Region();
        vDiv.setPrefSize(1, 28);
        vDiv.setMinSize(1, 28);
        vDiv.setMaxSize(1, 28);
        vDiv.setStyle("-fx-background-color: " + C_BORDER + ";");
        HBox divWrap = new HBox(vDiv);
        divWrap.setAlignment(Pos.CENTER);
        divWrap.setPadding(new Insets(0, 20, 0, 18));

        // ── Nav pills ───────────────────────────────────────────────────
        Label navDashboard = navPill("Dashboard", true);
        Label navPatients  = navPill("Patients",  false);
        Label navReports   = navPill("Reports",   false);

        navDashboard.setOnMouseClicked(e -> {
            setActiveNav(navDashboard, navPatients, navReports);
            contentArea.getChildren().setAll(buildBody());
        });
        navPatients.setOnMouseClicked(e -> {
            setActiveNav(navPatients, navDashboard, navReports);
            contentArea.getChildren().setAll(PatientsView.getView());
        });
        navReports.setOnMouseClicked(e -> {
            setActiveNav(navReports, navDashboard, navPatients);
            contentArea.getChildren().setAll(ReportsView.getView());
        });

        HBox navRow = new HBox(4, navDashboard, navPatients, navReports);
        navRow.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // ── User area ───────────────────────────────────────────────────
        // Status dot
        StackPane statusDot = new StackPane();
        statusDot.setPrefSize(8, 8);
        statusDot.setMinSize(8, 8);
        statusDot.setStyle("-fx-background-color: " + C_GREEN + "; -fx-background-radius: 4;");

        Label statusLbl = new Label("Online");
        statusLbl.setFont(Font.font("Arial", 11));
        statusLbl.setTextFill(Color.web(C_GREEN));

        HBox statusBadge = new HBox(5, statusDot, statusLbl);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(4, 10, 4, 10));
        statusBadge.setStyle(
            "-fx-background-color: " + C_GREEN_LIGHT + ";" +
            "-fx-background-radius: 12;"
        );

        // Avatar
        StackPane avatar = new StackPane();
        avatar.setPrefSize(36, 36);
        avatar.setMinSize(36, 36);
        avatar.setStyle(
            "-fx-background-color: " + C_TEAL_LIGHT + ";" +
            "-fx-background-radius: 18;" +
            "-fx-border-color: " + C_TEAL + ";" +
            "-fx-border-radius: 18;" +
            "-fx-border-width: 1.5;"
        );
        Label avLbl = new Label("DA");
        avLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        avLbl.setTextFill(Color.web(C_TEAL));
        avatar.getChildren().add(avLbl);

        VBox userInfo = new VBox(1);
        Label userName = new Label("Dr. Admin");
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        userName.setTextFill(Color.web(C_TEXT_DARK));
        Label userRole = new Label("Administrator");
        userRole.setFont(Font.font("Arial", 10));
        userRole.setTextFill(Color.web(C_TEXT_LIGHT));
        userInfo.getChildren().addAll(userName, userRole);

        HBox userBox = new HBox(10, avatar, userInfo);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 16));

        header.getChildren().addAll(
            logoMark, brandStack, divWrap,
            navRow, spacer,
            statusBadge, userBox
        );

        return header;
    }

    /** Builds a clean logo mark: gradient rounded square with a "+" cross made of two Rectangles. */
    private StackPane buildLogoMark() {

        StackPane mark = new StackPane();
        mark.setPrefSize(40, 40);
        mark.setMinSize(40, 40);

        // Gradient background square
        Rectangle bg = new Rectangle(40, 40);
        bg.setArcWidth(12); bg.setArcHeight(12);
        bg.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#4A9FE8")),
            new Stop(1, Color.web(C_BLUE_DARK))
        ));

        // Horizontal bar of the cross
        Rectangle hBar = new Rectangle(18, 4);
        hBar.setArcWidth(3); hBar.setArcHeight(3);
        hBar.setFill(Color.WHITE);

        // Vertical bar of the cross
        Rectangle vBar = new Rectangle(4, 18);
        vBar.setArcWidth(3); vBar.setArcHeight(3);
        vBar.setFill(Color.WHITE);

        mark.getChildren().addAll(bg, hBar, vBar);
        return mark;
    }

    private void setActiveNav(Label active, Label... inactives) {
        active.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        active.setTextFill(Color.web(C_BLUE));
        active.setStyle(
            "-fx-background-color: " + C_BLUE_LIGHT + ";" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        for (Label l : inactives) {
            l.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            l.setTextFill(Color.web(C_TEXT_MED));
            l.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 20;" +
                "-fx-cursor: hand;"
            );
        }
    }

    private Label navPill(String text, boolean active) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", active ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        lbl.setPadding(new Insets(7, 16, 7, 16));
        lbl.setTextFill(Color.web(active ? C_BLUE : C_TEXT_MED));
        lbl.setStyle(
            "-fx-background-color: " + (active ? C_BLUE_LIGHT : "transparent") + ";" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        lbl.setOnMouseEntered(e -> {
            if (!lbl.getTextFill().equals(Color.web(C_BLUE)))
                lbl.setStyle("-fx-background-color: " + C_BLUE_LIGHT + "; -fx-background-radius: 20; -fx-cursor: hand;");
        });
        lbl.setOnMouseExited(e -> {
            if (!lbl.getTextFill().equals(Color.web(C_BLUE)))
                lbl.setStyle("-fx-background-color: transparent; -fx-background-radius: 20; -fx-cursor: hand;");
        });
        return lbl;
    }

    // ===================== BODY =====================

    private HBox buildBody() {
        HBox body = new HBox(24);
        body.setPadding(new Insets(26, 28, 26, 28));

        VBox leftCol  = buildLeftColumn();
        VBox rightCol = buildRightColumn();

        leftCol.setMinWidth(320);
        leftCol.setMaxWidth(340);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        body.getChildren().addAll(leftCol, rightCol);
        return body;
    }

    // ===================== LEFT COLUMN =====================

    private VBox buildLeftColumn() {

        VBox col = new VBox(0);

        VBox formCard = card();

        // ── Card header ──────────────────────────────────────────────────
        HBox cardHeader = new HBox(10);
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        cardHeader.setPadding(new Insets(0, 0, 16, 0));

        // Small blue dot accent
        StackPane dot = new StackPane();
        dot.setPrefSize(10, 10);
        dot.setMinSize(10, 10);
        dot.setStyle("-fx-background-color: " + C_BLUE + "; -fx-background-radius: 5;");

        VBox headerText = new VBox(1);
        Label cardTitle = new Label("Add New Patient");
        cardTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        cardTitle.setTextFill(Color.web(C_TEXT_DARK));
        Label cardSub = new Label("Fill in the details below to register");
        cardSub.setFont(Font.font("Arial", 11));
        cardSub.setTextFill(Color.web(C_TEXT_LIGHT));
        headerText.getChildren().addAll(cardTitle, cardSub);

        cardHeader.getChildren().addAll(dot, headerText);

        // ── Type selector ────────────────────────────────────────────────
        ToggleGroup typeGroup = new ToggleGroup();
        ToggleButton bedBtn  = typeToggle("Bed Patient", typeGroup, true);
        ToggleButton apptBtn = typeToggle("Appointment", typeGroup, false);

        HBox typeRow = new HBox(8, bedBtn, apptBtn);

        // ── Fields ───────────────────────────────────────────────────────
        TextField idField        = styledField("Patient ID  e.g. P-001");
        TextField nameField      = styledField("Full Name  e.g. Rahul Sharma");
        TextField diagnosisField = styledField("Diagnosis  e.g. Hypertension");
        TextField extraField     = styledField("Room No.  e.g. 204");

        typeGroup.selectedToggleProperty().addListener((obs, old, nw) -> {
            if (nw == bedBtn) extraField.setPromptText("Room No.  e.g. 204");
            else              extraField.setPromptText("Date  e.g. 2026-06-01");
        });

        // ── Buttons ───────────────────────────────────────────────────────
        Button addBtn   = primaryButton("Add Patient");
        Button clearBtn = dangerButton("Clear All Records");

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setWrapText(true);

        addBtn.setOnAction(e -> {
            String id   = idField.getText().trim();
            String name = nameField.getText().trim();
            String diag = diagnosisField.getText().trim();
            String xtra = extraField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || diag.isEmpty() || xtra.isEmpty()) {
                showInlineStatus(statusLabel, "Please fill in all fields.", false);
                shakeButton(addBtn);
                return;
            }

            Patient patient;
            if (typeGroup.getSelectedToggle() == bedBtn) {
                patient = new Beds(id, name, diag, xtra);
                totalBeds++;
                bedCountLabel.setText(String.valueOf(totalBeds));
            } else {
                patient = new Appointment(id, name, diag, xtra);
                totalAppointments++;
                apptCountLabel.setText(String.valueOf(totalAppointments));
            }

            hospital.addPatient(patient);
            totalCountLabel.setText(String.valueOf(hospital.getPatients().size()));
            idField.clear(); nameField.clear();
            diagnosisField.clear(); extraField.clear();
            showInlineStatus(statusLabel, "Patient added successfully.", true);
            pulseButton(addBtn);
        });

        clearBtn.setOnAction(e -> {
            hospital.getPatients().clear();
            totalBeds = 0; totalAppointments = 0;
            bedCountLabel.setText("0");
            apptCountLabel.setText("0");
            totalCountLabel.setText("0");
            showInlineStatus(statusLabel, "All records cleared.", true);
        });

        formCard.getChildren().addAll(
            cardHeader,
            sectionLabel("PATIENT TYPE"),   typeRow,
            thinDivider(),
            sectionLabel("IDENTIFICATION"), idField, nameField,
            thinDivider(),
            sectionLabel("MEDICAL INFO"),   diagnosisField,
            thinDivider(),
            sectionLabel("ROOM / APPOINTMENT"), extraField,
            thinDivider(),
            addBtn, clearBtn,
            statusLabel
        );

        col.getChildren().add(formCard);
        return col;
    }

    // ===================== RIGHT COLUMN =====================

    private VBox buildRightColumn() {

        VBox col = new VBox(20);

        totalCountLabel = new Label("0");
        bedCountLabel   = new Label("0");
        apptCountLabel  = new Label("0");

        HBox statsRow = new HBox(16,
            statCard("Total Patients",  totalCountLabel, C_BLUE,  C_BLUE_LIGHT,  "P"),
            statCard("Admitted (Beds)", bedCountLabel,   C_TEAL,  C_TEAL_LIGHT,  "B"),
            statCard("Appointments",    apptCountLabel,  C_GREEN, C_GREEN_LIGHT, "A")
        );

        // ── Records card ─────────────────────────────────────────────────
        VBox recordsCard = card();

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setPadding(new Insets(0, 0, 14, 0));

        // Accent bar
        Region accentBar = new Region();
        accentBar.setPrefSize(4, 22);
        accentBar.setMinSize(4, 22);
        accentBar.setStyle("-fx-background-color: " + C_BLUE + "; -fx-background-radius: 2;");

        Label title = new Label("Patient Records");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
        title.setTextFill(Color.web(C_TEXT_DARK));

        titleRow.getChildren().addAll(accentBar, title);

        ListView<Patient> listView = new ListView<>(hospital.getPatients());
        listView.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        listView.setFixedCellSize(72);
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setCellFactory(lv -> new PatientCell());

        recordsCard.getChildren().addAll(titleRow, listView);
        VBox.setVgrow(recordsCard, Priority.ALWAYS);

        col.getChildren().addAll(statsRow, recordsCard);
        VBox.setVgrow(col, Priority.ALWAYS);

        return col;
    }

    // ===================== LIST CELL =====================

    class PatientCell extends ListCell<Patient> {

        @Override
        protected void updateItem(Patient p, boolean empty) {
            super.updateItem(p, empty);
            setStyle("-fx-background-color: transparent; -fx-padding: 0;");

            if (empty || p == null) {
                setText(null); setGraphic(null);
            } else {
                boolean isBed = p instanceof Beds;

                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(10, 14, 10, 14));
                row.setStyle(
                    "-fx-background-color: " + C_WHITE + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: " + C_BORDER + ";" +
                    "-fx-border-radius: 12;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 6, 0, 0, 2);"
                );

                // Left coloured stripe
                Region stripe = new Region();
                stripe.setPrefSize(4, 40);
                stripe.setMinSize(4, 40);
                stripe.setStyle(
                    "-fx-background-color: " + (isBed ? C_TEAL : C_BLUE) + ";" +
                    "-fx-background-radius: 2;"
                );

                // Avatar circle with first-letter initial
                StackPane avatarCircle = new StackPane();
                avatarCircle.setPrefSize(42, 42);
                avatarCircle.setMinSize(42, 42);
                avatarCircle.setStyle(
                    "-fx-background-color: " + (isBed ? C_TEAL_LIGHT : C_BLUE_LIGHT) + ";" +
                    "-fx-background-radius: 21;" +
                    "-fx-border-color: " + (isBed ? C_TEAL : C_BLUE) + ";" +
                    "-fx-border-radius: 21;" +
                    "-fx-border-width: 1.5;"
                );
                Label initLbl = new Label(String.valueOf(p.getName().charAt(0)).toUpperCase());
                initLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
                initLbl.setTextFill(Color.web(isBed ? C_TEAL : C_BLUE));
                avatarCircle.getChildren().add(initLbl);

                // Info block
                VBox info = new VBox(3);

                HBox nameRow = new HBox(8);
                nameRow.setAlignment(Pos.CENTER_LEFT);

                Label nameLbl = new Label(p.getName());
                nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                nameLbl.setTextFill(Color.web(C_TEXT_DARK));

                // Badge
                Label badge = new Label(p.getType());
                badge.setFont(Font.font("Arial", FontWeight.BOLD, 9));
                badge.setPadding(new Insets(2, 7, 2, 7));
                badge.setTextFill(Color.web(isBed ? C_TEAL : C_BLUE));
                badge.setStyle(
                    "-fx-background-color: " + (isBed ? C_TEAL_LIGHT : C_BLUE_LIGHT) + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-color: " + (isBed ? C_TEAL : C_BLUE) + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-border-width: 1;"
                );
                nameRow.getChildren().addAll(nameLbl, badge);

                Label detailLbl = new Label(p.getDiagnosis() + "   |   " + p.getDetails());
                detailLbl.setFont(Font.font("Arial", 12));
                detailLbl.setTextFill(Color.web(C_TEXT_MED));

                Label idLbl = new Label("ID: " + p.getId());
                idLbl.setFont(Font.font("Arial", 11));
                idLbl.setTextFill(Color.web(C_TEXT_LIGHT));

                info.getChildren().addAll(nameRow, detailLbl, idLbl);
                HBox.setHgrow(info, Priority.ALWAYS);

                row.getChildren().addAll(stripe, avatarCircle, info);

                setGraphic(row);
                setPadding(new Insets(3, 2, 3, 2));
            }
        }
    }

    // ===================== HELPERS =====================

    private VBox card() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(22));
        box.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );
        return box;
    }

    /**
     * Stat card: left coloured accent bar, then icon circle + number + label.
     * Uses a plain letter (P/B/A) instead of emoji for cross-platform safety.
     */
    private HBox statCard(String title, Label value, String accent, String bg, String iconLetter) {

        HBox card = new HBox(0);
        card.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 14;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );
        HBox.setHgrow(card, Priority.ALWAYS);

        // Accent bar (left side)
        Region accentBar = new Region();
        accentBar.setPrefWidth(5);
        accentBar.setMinWidth(5);
        accentBar.setStyle(
            "-fx-background-color: " + accent + ";" +
            "-fx-background-radius: 14 0 0 14;"
        );

        // Icon circle
        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(44, 44);
        iconCircle.setMinSize(44, 44);
        iconCircle.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: " + accent + ";" +
            "-fx-border-radius: 22;" +
            "-fx-border-width: 1.5;"
        );
        Label iconLbl = new Label(iconLetter);
        iconLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        iconLbl.setTextFill(Color.web(accent));
        iconCircle.getChildren().add(iconLbl);

        // Text
        VBox content = new VBox(4);
        content.setPadding(new Insets(14, 16, 14, 14));
        content.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(content, Priority.ALWAYS);

        value.setFont(Font.font("Georgia", FontWeight.BOLD, 34));
        value.setTextFill(Color.web(accent));

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", 12));
        titleLbl.setTextFill(Color.web(C_TEXT_LIGHT));

        content.getChildren().addAll(iconCircle, value, titleLbl);
        card.getChildren().addAll(accentBar, content);

        return card;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setStyle(
            "-fx-background-color: " + C_PAGE_BG + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + C_TEXT_DARK + ";"
        );
        tf.focusedProperty().addListener((obs, old, focused) ->
            tf.setStyle(
                "-fx-background-color: " + C_WHITE + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + (focused ? C_BLUE : C_BORDER) + ";" +
                "-fx-border-radius: 8;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + C_TEXT_DARK + ";"
            )
        );
        return tf;
    }

    private ToggleButton typeToggle(String text, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setPrefHeight(36);

        String activeStyle =
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;";

        String inactiveStyle =
            "-fx-background-color: " + C_PAGE_BG + ";" +
            "-fx-text-fill: " + C_TEXT_MED + ";" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-radius: 8;" +
            "-fx-font-size: 12px;";

        btn.setStyle(selected ? activeStyle : inactiveStyle);
        btn.selectedProperty().addListener((obs, old, nw) -> btn.setStyle(nw ? activeStyle : inactiveStyle));
        return btn;
    }

    private Button primaryButton(String text) {
        Button btn = new Button("+ " + text);
        btn.setPrefHeight(44);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        String base =
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(43,127,212,0.30), 8, 0, 0, 3);";
        String hover =
            "-fx-background-color: " + C_BLUE_DARK + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(43,127,212,0.45), 10, 0, 0, 4);";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button("x  " + text);
        btn.setPrefHeight(40);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Arial", 12));
        String base =
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-text-fill: " + C_RED + ";" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;";
        String hover =
            "-fx-background-color: " + C_RED_LIGHT + ";" +
            "-fx-text-fill: " + C_RED + ";" +
            "-fx-border-color: " + C_RED + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setTextFill(Color.web(C_TEXT_LIGHT));
        lbl.setPadding(new Insets(6, 0, 0, 0));
        return lbl;
    }

    private Region thinDivider() {
        Region r = new Region();
        r.setPrefHeight(1);
        r.setMaxHeight(1);
        r.setStyle("-fx-background-color: " + C_BORDER + ";");
        VBox.setMargin(r, new Insets(6, 0, 2, 0));
        return r;
    }

    private void showInlineStatus(Label lbl, String msg, boolean success) {
        lbl.setText((success ? "✓  " : "!  ") + msg);
        lbl.setTextFill(Color.web(success ? C_GREEN : C_RED));
        FadeTransition ft = new FadeTransition(Duration.millis(250), lbl);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private void pulseButton(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(130), btn);
        st.setFromX(1); st.setToX(1.04);
        st.setFromY(1); st.setToY(1.04);
        st.setAutoReverse(true); st.setCycleCount(2); st.play();
    }

    private void shakeButton(Button btn) {
        javafx.animation.TranslateTransition tt =
            new javafx.animation.TranslateTransition(Duration.millis(55), btn);
        tt.setByX(7); tt.setAutoReverse(true); tt.setCycleCount(4); tt.play();
    }

    public static void main(String[] args) { launch(); }
}