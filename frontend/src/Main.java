// import javafx.application.Application;
// import javafx.collections.FXCollections;
// import javafx.collections.ObservableList;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.*;
// import javafx.scene.effect.DropShadow;
// import javafx.scene.layout.*;
// import javafx.scene.paint.Color;
// import javafx.scene.text.Font;
// import javafx.stage.Stage;

// abstract class Patient {
//     String id;
//     String name;
//     String diagnosis;

//     public Patient(String id, String name, String diagnosis) {
//         this.id = id;
//         this.name = name;
//         this.diagnosis = diagnosis;
//     }

//     public String getId() {
//         return id;
//     }

//     public String getName() {
//         return name;
//     }

//     public String getDiagnosis() {
//         return diagnosis;
//     }

//     public abstract String getDetails();
// }

// class Beds extends Patient {

//     String roomNumber;

//     public Beds(String id, String name, String diagnosis, String roomNumber) {
//         super(id, name, diagnosis);
//         this.roomNumber = roomNumber;
//     }

//     @Override
//     public String getDetails() {
//         return "Room No: " + roomNumber;
//     }
// }

// class Appointment extends Patient {

//     String appointmentDate;

//     public Appointment(String id, String name, String diagnosis, String appointmentDate) {
//         super(id, name, diagnosis);
//         this.appointmentDate = appointmentDate;
//     }

//     @Override
//     public String getDetails() {
//         return "Appointment: " + appointmentDate;
//     }
// }

// class Hospital {

//     ObservableList<String> patientList = FXCollections.observableArrayList();

//     public void addPatient(Patient patient) {

//         String data =
//                 "🆔 " + patient.getId() +
//                 "   |   👤 " + patient.getName() +
//                 "   |   🩺 " + patient.getDiagnosis() +
//                 "   |   " + patient.getDetails();

//         patientList.add(data);
//     }

//     public ObservableList<String> getPatients() {
//         return patientList;
//     }
// }

// public class Main extends Application {

//     Hospital hospital = new Hospital();

//     @Override
//     public void start(Stage stage) {

//         // ================= TITLE =================

//         Label title = new Label("🏥 Smart Hospital Management System");
//         title.setFont(Font.font("Arial", 28));
//         title.setTextFill(Color.WHITE);

//         // ================= INPUT FIELDS =================

//         TextField idField = new TextField();
//         idField.setPromptText("Enter Patient ID");

//         TextField nameField = new TextField();
//         nameField.setPromptText("Enter Patient Name");

//         TextField diagnosisField = new TextField();
//         diagnosisField.setPromptText("Enter Diagnosis");

//         TextField extraField = new TextField();
//         extraField.setPromptText("Room No / Appointment Date");

//         ComboBox<String> typeBox = new ComboBox<>();
//         typeBox.getItems().addAll("Bed Patient", "Appointment Patient");
//         typeBox.setValue("Bed Patient");

//         // ================= BUTTONS =================

//         Button addButton = new Button("➕ Add Patient");
//         addButton.setStyle("""
//                 -fx-background-color: #00c853;
//                 -fx-text-fill: white;
//                 -fx-font-size: 15px;
//                 -fx-background-radius: 12;
//                 -fx-padding: 10 20 10 20;
//                 """);

//         Button clearButton = new Button("🗑 Clear");
//         clearButton.setStyle("""
//                 -fx-background-color: #d50000;
//                 -fx-text-fill: white;
//                 -fx-font-size: 15px;
//                 -fx-background-radius: 12;
//                 -fx-padding: 10 20 10 20;
//                 """);

//         // ================= LIST VIEW =================

//         ListView<String> patientView = new ListView<>();
//         patientView.setItems(hospital.getPatients());

//         patientView.setStyle("""
//                 -fx-control-inner-background: #1e1e1e;
//                 -fx-font-size: 14px;
//                 -fx-text-fill: white;
//                 """);

//         // ================= BUTTON ACTION =================

//         addButton.setOnAction(e -> {

//             String id = idField.getText();
//             String name = nameField.getText();
//             String diagnosis = diagnosisField.getText();
//             String extra = extraField.getText();

//             if (id.isEmpty() || name.isEmpty() || diagnosis.isEmpty() || extra.isEmpty()) {

//                 Alert alert = new Alert(Alert.AlertType.WARNING);
//                 alert.setTitle("Input Error");
//                 alert.setHeaderText(null);
//                 alert.setContentText("Please fill all fields!");
//                 alert.showAndWait();

//                 return;
//             }

//             if (typeBox.getValue().equals("Bed Patient")) {

//                 Beds patient = new Beds(id, name, diagnosis, extra);
//                 hospital.addPatient(patient);

//             } else {

//                 Appointment patient = new Appointment(id, name, diagnosis, extra);
//                 hospital.addPatient(patient);
//             }

//             idField.clear();
//             nameField.clear();
//             diagnosisField.clear();
//             extraField.clear();
//         });

//         // ================= CLEAR BUTTON =================

//         clearButton.setOnAction(e -> {
//             hospital.getPatients().clear();
//         });

//         // ================= FORM LAYOUT =================

//         VBox formBox = new VBox(15,
//                 typeBox,
//                 idField,
//                 nameField,
//                 diagnosisField,
//                 extraField,
//                 addButton,
//                 clearButton
//         );

//         formBox.setPadding(new Insets(25));
//         formBox.setAlignment(Pos.CENTER);
//         formBox.setMaxWidth(350);

//         formBox.setStyle("""
//                 -fx-background-color: rgba(255,255,255,0.08);
//                 -fx-background-radius: 20;
//                 """);

//         formBox.setEffect(new DropShadow(20, Color.BLACK));

//         // ================= MAIN LAYOUT =================

//         VBox leftSection = new VBox(25, title, formBox);
//         leftSection.setAlignment(Pos.TOP_CENTER);

//         VBox rightSection = new VBox(15);

//         Label patientLabel = new Label("📋 Patient Records");
//         patientLabel.setFont(Font.font("Arial", 22));
//         patientLabel.setTextFill(Color.WHITE);

//         rightSection.getChildren().addAll(patientLabel, patientView);
//         rightSection.setPrefWidth(600);

//         HBox root = new HBox(40, leftSection, rightSection);

//         root.setPadding(new Insets(30));
//         root.setStyle("""
//                 -fx-background-color: linear-gradient(to bottom right, #0f2027, #203a43, #2c5364);
//                 """);

//         // ================= SCENE =================

//         Scene scene = new Scene(root, 1100, 650);

//         stage.setTitle("Hospital Management System");
//         stage.setScene(scene);
//         stage.show();
//     }

//     public static void main(String[] args) {
//         launch();
//     }
// }


import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

// ===================== BACKEND (UNCHANGED) =====================

abstract class Patient {
    String id, name, diagnosis;
    public Patient(String id, String name, String diagnosis) {
        this.id = id; this.name = name; this.diagnosis = diagnosis;
    }
    public String getId()        { return id; }
    public String getName()      { return name; }
    public String getDiagnosis() { return diagnosis; }
    public abstract String getDetails();
}

class Beds extends Patient {
    String roomNumber;
    public Beds(String id, String name, String diagnosis, String roomNumber) {
        super(id, name, diagnosis);
        this.roomNumber = roomNumber;
    }
    @Override public String getDetails() { return "Room No: " + roomNumber; }
}

class Appointment extends Patient {
    String appointmentDate;
    public Appointment(String id, String name, String diagnosis, String appointmentDate) {
        super(id, name, diagnosis);
        this.appointmentDate = appointmentDate;
    }
    @Override public String getDetails() { return "Appointment: " + appointmentDate; }
}

class Hospital {
    ObservableList<Patient> patientList = FXCollections.observableArrayList();
    public void addPatient(Patient patient) { patientList.add(patient); }
    public ObservableList<Patient> getPatients() { return patientList; }
}

// ===================== FRONTEND (REDESIGNED) =====================

public class Main extends Application {

    Hospital hospital = new Hospital();
    int totalBeds = 0, totalAppointments = 0;
    Label bedCountLabel, apptCountLabel, totalCountLabel;

    // ── Color palette ──────────────────────────────────────────
    private static final String C_PAGE_BG     = "#F0F4F8";
    private static final String C_WHITE       = "#FFFFFF";
    private static final String C_BLUE        = "#2B7FD4";
    private static final String C_BLUE_DARK   = "#1B5FA8";
    private static final String C_TEAL        = "#0F9D8A";
    private static final String C_TEAL_LIGHT  = "#E1F5F2";
    private static final String C_BLUE_LIGHT  = "#EBF4FF";
    private static final String C_GREEN       = "#1DB87A";
    private static final String C_GREEN_LIGHT = "#E6F9F1";
    private static final String C_RED         = "#E24B4A";
    private static final String C_RED_LIGHT   = "#FCEBEB";
    private static final String C_AMBER       = "#D97706";
    private static final String C_AMBER_LIGHT = "#FFFBEB";
    private static final String C_TEXT_DARK   = "#1A2332";
    private static final String C_TEXT_MED    = "#4A5568";
    private static final String C_TEXT_LIGHT  = "#718096";
    private static final String C_BORDER      = "#E2E8F0";

    @Override
    public void start(Stage stage) {

        // ── Root layout ────────────────────────────────────────
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + C_PAGE_BG + ";");

        root.getChildren().addAll(
            buildHeader(),
            buildBody()
        );

        Scene scene = new Scene(root, 1200, 720);
        stage.setTitle("MediCare — Hospital Management System");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    // ── HEADER ────────────────────────────────────────────────
    private HBox buildHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 32, 0, 32));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(64);
        header.setSpacing(0);
        header.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );

        // Logo mark
        StackPane logoMark = new StackPane();
        logoMark.setPrefSize(36, 36);
        logoMark.setStyle(
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-background-radius: 10;"
        );
        Label cross = new Label("+");
        cross.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        cross.setTextFill(Color.WHITE);
        logoMark.getChildren().add(cross);

        Label logoText = new Label("MediCare");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logoText.setTextFill(Color.web(C_TEXT_DARK));
        logoText.setPadding(new Insets(0, 0, 0, 10));

        Label logoSub = new Label("Hospital Management");
        logoSub.setFont(Font.font("Arial", 13));
        logoSub.setTextFill(Color.web(C_TEXT_LIGHT));
        logoSub.setPadding(new Insets(0, 0, 0, 8));

        Separator sep = new Separator(javafx.geometry.Orientation.VERTICAL);
        sep.setPadding(new Insets(12, 16, 12, 16));
        sep.setStyle("-fx-background-color: " + C_BORDER + ";");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Nav pills
        Label navDashboard = navPill("Dashboard", true);
        Label navPatients  = navPill("Patients", false);
        Label navReports   = navPill("Reports", false);

        // User avatar
        StackPane avatar = new StackPane();
        avatar.setPrefSize(36, 36);
        avatar.setStyle(
            "-fx-background-color: " + C_TEAL_LIGHT + ";" +
            "-fx-background-radius: 18;"
        );
        Label avatarLbl = new Label("Dr");
        avatarLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        avatarLbl.setTextFill(Color.web(C_TEAL));
        avatar.getChildren().add(avatarLbl);

        Label drName = new Label("Dr. Admin");
        drName.setFont(Font.font("Arial", 13));
        drName.setTextFill(Color.web(C_TEXT_MED));
        drName.setPadding(new Insets(0, 0, 0, 8));

        header.getChildren().addAll(
            logoMark, logoText, logoSub, sep,
            navDashboard, navPatients, navReports,
            spacer, avatar, drName
        );
        return header;
    }

    private Label navPill(String text, boolean active) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", active ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        lbl.setPadding(new Insets(6, 14, 6, 14));
        lbl.setTextFill(Color.web(active ? C_BLUE : C_TEXT_MED));
        lbl.setStyle(
            "-fx-background-color: " + (active ? C_BLUE_LIGHT : "transparent") + ";" +
            "-fx-background-radius: 20;"
        );
        return lbl;
    }

    // ── BODY ─────────────────────────────────────────────────
    private HBox buildBody() {
        HBox body = new HBox(24);
        body.setPadding(new Insets(28, 32, 28, 32));
        VBox.setVgrow(body, Priority.ALWAYS);

        VBox leftCol = buildLeftColumn();
        VBox rightCol = buildRightColumn();

        HBox.setHgrow(leftCol, Priority.NEVER);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        leftCol.setMinWidth(340);
        leftCol.setMaxWidth(360);

        body.getChildren().addAll(leftCol, rightCol);
        return body;
    }

    // ── LEFT COLUMN (form) ────────────────────────────────────
    private VBox buildLeftColumn() {
        VBox col = new VBox(20);

        // Section title
        Label sectionTitle = new Label("Add New Patient");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        sectionTitle.setTextFill(Color.web(C_TEXT_DARK));

        Label sectionSub = new Label("Fill in the details below");
        sectionSub.setFont(Font.font("Arial", 13));
        sectionSub.setTextFill(Color.web(C_TEXT_LIGHT));

        // Patient type toggle
        ToggleGroup typeGroup = new ToggleGroup();
        ToggleButton bedBtn  = typeToggle("Bed Patient",         typeGroup, true);
        ToggleButton apptBtn = typeToggle("Appointment Patient", typeGroup, false);

        HBox typeRow = new HBox(8, bedBtn, apptBtn);
        typeRow.setAlignment(Pos.CENTER_LEFT);

        // Input fields
        TextField idField        = styledField("Patient ID",              "e.g. P-001");
        TextField nameField      = styledField("Full Name",               "e.g. Rahul Sharma");
        TextField diagnosisField = styledField("Diagnosis",               "e.g. Hypertension");
        TextField extraField     = styledField("Room No / Appt. Date",    "e.g. 204 or 2026-06-01");

        // Update extraField prompt on toggle
        typeGroup.selectedToggleProperty().addListener((obs, old, nw) -> {
            if (nw == bedBtn)
                extraField.setPromptText("Room number e.g. 204");
            else
                extraField.setPromptText("Date e.g. 2026-06-01");
        });

        // Buttons
        Button addBtn   = primaryButton("  +  Add Patient");
        Button clearBtn = dangerButton("  ×  Clear All Records");

        addBtn.setMaxWidth(Double.MAX_VALUE);
        clearBtn.setMaxWidth(Double.MAX_VALUE);

        // Status label
        Label statusLabel = new Label("");
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setTextFill(Color.web(C_GREEN));

        // Add action
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

            idField.clear(); nameField.clear(); diagnosisField.clear(); extraField.clear();
            showInlineStatus(statusLabel, "Patient added successfully.", true);
            pulseButton(addBtn);
        });

        // Clear action
        clearBtn.setOnAction(e -> {
            hospital.getPatients().clear();
            totalBeds = 0; totalAppointments = 0;
            bedCountLabel.setText("0");
            apptCountLabel.setText("0");
            totalCountLabel.setText("0");
            showInlineStatus(statusLabel, "All records cleared.", true);
        });

        // Form card
        VBox formCard = card();
        formCard.getChildren().addAll(
            labeledSection("Patient Type"), typeRow,
            labeledSection("Identification"), idField, nameField,
            labeledSection("Medical Info"), diagnosisField,
            labeledSection("Room / Appointment"), extraField,
            new Region(), addBtn, clearBtn, statusLabel
        );

        col.getChildren().addAll(sectionTitle, sectionSub, formCard);
        return col;
    }

    // ── RIGHT COLUMN (stats + list) ───────────────────────────
    private VBox buildRightColumn() {
        VBox col = new VBox(20);
        VBox.setVgrow(col, Priority.ALWAYS);

        // Stat cards row
        totalCountLabel = new Label("0");
        bedCountLabel   = new Label("0");
        apptCountLabel  = new Label("0");

        HBox statsRow = new HBox(16,
            statCard("Total Patients",    totalCountLabel, C_BLUE,  C_BLUE_LIGHT,  "ALL"),
            statCard("Admitted (Beds)",   bedCountLabel,   C_TEAL,  C_TEAL_LIGHT,  "BED"),
            statCard("Appointments",      apptCountLabel,  C_GREEN, C_GREEN_LIGHT, "APT")
        );
        for (javafx.scene.Node n : statsRow.getChildren())
            HBox.setHgrow(n, Priority.ALWAYS);

        // Patient records
        Label recTitle = new Label("Patient Records");
        recTitle.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        recTitle.setTextFill(Color.web(C_TEXT_DARK));

        Label recSub = new Label("All registered patients");
        recSub.setFont(Font.font("Arial", 13));
        recSub.setTextFill(Color.web(C_TEXT_LIGHT));

        HBox recHeader = new HBox(8, recTitle, new Region(), recSub);
        HBox.setHgrow(recHeader.getChildren().get(1), Priority.ALWAYS);
        recHeader.setAlignment(Pos.CENTER_LEFT);

        // Table header
        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(10, 16, 10, 16));
        tableHeader.setStyle(
            "-fx-background-color: " + C_PAGE_BG + ";" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-width: 0 0 1 0;"
        );
        tableHeader.getChildren().addAll(
            colHeader("ID",          80),
            colHeader("Name",       160),
            colHeader("Diagnosis",  160),
            colHeader("Type",        90),
            colHeader("Details",    200)
        );

        // ListView
        ListView<Patient> listView = new ListView<>(hospital.getPatients());
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        listView.setCellFactory(lv -> new PatientCell());

        // Empty state
        Label emptyLabel = new Label("No patients yet. Add one using the form.");
        emptyLabel.setFont(Font.font("Arial", 14));
        emptyLabel.setTextFill(Color.web(C_TEXT_LIGHT));
        listView.setPlaceholder(emptyLabel);

        // Records card
        VBox recordsCard = card();
        recordsCard.setPadding(new Insets(0));
        VBox.setVgrow(recordsCard, Priority.ALWAYS);
        recordsCard.getChildren().addAll(
            padded(recHeader), tableHeader, listView
        );

        col.getChildren().addAll(statsRow, recordsCard);
        return col;
    }

    // ── Custom ListCell ───────────────────────────────────────
    class PatientCell extends ListCell<Patient> {
        private final HBox row = new HBox();
        private final Label idLbl   = cellLabel(80);
        private final Label nameLbl = cellLabel(160);
        private final Label diagLbl = cellLabel(160);
        private final Label typeLbl = new Label();
        private final Label detLbl  = cellLabel(200);

        PatientCell() {
            super();
            typeLbl.setMinWidth(90);
            typeLbl.setMaxWidth(90);
            typeLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            typeLbl.setPadding(new Insets(3, 8, 3, 8));
            typeLbl.setStyle("-fx-background-radius: 12;");

            row.setPadding(new Insets(12, 16, 12, 16));
            row.setSpacing(0);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().addAll(idLbl, nameLbl, diagLbl, typeLbl, detLbl);
            setGraphic(row);
            setText(null);
            setStyle("-fx-background-color: transparent; -fx-padding: 0;");

            // Hover highlight
            row.setOnMouseEntered(e -> row.setStyle(
                "-fx-background-color: " + C_BLUE_LIGHT + ";" +
                "-fx-background-radius: 8;"
            ));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: transparent;"));
        }

        @Override
        protected void updateItem(Patient p, boolean empty) {
            super.updateItem(p, empty);
            if (empty || p == null) {
                setGraphic(null);
            } else {
                idLbl.setText(p.getId());
                nameLbl.setText(p.getName());
                diagLbl.setText(p.getDiagnosis());
                detLbl.setText(p.getDetails());

                boolean isBed = p instanceof Beds;
                typeLbl.setText(isBed ? "BED" : "APPT");
                typeLbl.setTextFill(Color.web(isBed ? C_TEAL : C_AMBER));
                typeLbl.setStyle(
                    "-fx-background-color: " + (isBed ? C_TEAL_LIGHT : C_AMBER_LIGHT) + ";" +
                    "-fx-background-radius: 12;"
                );

                // Alternating rows
                row.setStyle(
                    "-fx-background-color: " + (getIndex() % 2 == 0 ? C_WHITE : C_PAGE_BG) + ";" +
                    "-fx-background-radius: 0;"
                );
                row.setOnMouseEntered(e -> row.setStyle(
                    "-fx-background-color: " + C_BLUE_LIGHT + ";" +
                    "-fx-background-radius: 0;"
                ));
                row.setOnMouseExited(e -> row.setStyle(
                    "-fx-background-color: " + (getIndex() % 2 == 0 ? C_WHITE : C_PAGE_BG) + ";" +
                    "-fx-background-radius: 0;"
                ));

                FadeTransition ft = new FadeTransition(Duration.millis(300), row);
                ft.setFromValue(0.4);
                ft.setToValue(1.0);
                ft.play();

                setGraphic(row);
            }
        }
    }

    // ── HELPERS ───────────────────────────────────────────────

    private VBox card() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;"
        );
        return card;
    }

    private HBox statCard(String title, Label valueLabel, String accent, String bg, String abbr) {
        HBox card = new HBox(16);
        card.setPadding(new Insets(18, 20, 18, 20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 14;" +
            "-fx-border-width: 1;"
        );

        // Icon circle
        StackPane icon = new StackPane();
        icon.setPrefSize(44, 44);
        icon.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-background-radius: 22;"
        );
        Label iconLbl = new Label(abbr);
        iconLbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        iconLbl.setTextFill(Color.web(accent));
        icon.getChildren().add(iconLbl);

        VBox info = new VBox(4);
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", 12));
        titleLbl.setTextFill(Color.web(C_TEXT_LIGHT));

        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setTextFill(Color.web(accent));

        info.getChildren().addAll(titleLbl, valueLabel);
        card.getChildren().addAll(icon, info);
        return card;
    }

    private TextField styledField(String label, String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setFont(Font.font("Arial", 13));
        tf.setPrefHeight(40);
        tf.setMaxWidth(Double.MAX_VALUE);
        tf.setStyle(
            "-fx-background-color: " + C_PAGE_BG + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-border-width: 1;" +
            "-fx-padding: 8 12 8 12;" +
            "-fx-text-fill: " + C_TEXT_DARK + ";" +
            "-fx-prompt-text-fill: " + C_TEXT_LIGHT + ";"
        );
        tf.focusedProperty().addListener((obs, old, nw) -> {
            if (nw) tf.setStyle(
                "-fx-background-color: " + C_WHITE + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + C_BLUE + ";" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1.5;" +
                "-fx-padding: 8 12 8 12;" +
                "-fx-text-fill: " + C_TEXT_DARK + ";" +
                "-fx-prompt-text-fill: " + C_TEXT_LIGHT + ";"
            );
            else tf.setStyle(
                "-fx-background-color: " + C_PAGE_BG + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + C_BORDER + ";" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1;" +
                "-fx-padding: 8 12 8 12;" +
                "-fx-text-fill: " + C_TEXT_DARK + ";" +
                "-fx-prompt-text-fill: " + C_TEXT_LIGHT + ";"
            );
        });
        return tf;
    }

    private ToggleButton typeToggle(String text, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setFont(Font.font("Arial", 13));
        btn.setPadding(new Insets(7, 16, 7, 16));
        String activeStyle =
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: " + C_BLUE + ";" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;";
        String inactiveStyle =
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-text-fill: " + C_TEXT_MED + ";" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: " + C_BORDER + ";" +
            "-fx-border-radius: 20;" +
            "-fx-border-width: 1;";
        btn.setStyle(selected ? activeStyle : inactiveStyle);
        btn.selectedProperty().addListener((obs, old, nw) ->
            btn.setStyle(nw ? activeStyle : inactiveStyle)
        );
        return btn;
    }

    private Button primaryButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setPrefHeight(44);
        btn.setStyle(
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: " + C_BLUE_DARK + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + C_BLUE + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;"
        ));
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", 13));
        btn.setPrefHeight(40);
        btn.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-text-fill: " + C_RED + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + C_RED + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: " + C_RED_LIGHT + ";" +
            "-fx-text-fill: " + C_RED + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + C_RED + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: " + C_WHITE + ";" +
            "-fx-text-fill: " + C_RED + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + C_RED + ";" +
            "-fx-border-radius: 10;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;"
        ));
        return btn;
    }

    private Label labeledSection(String text) {
        Label lbl = new Label(text.toUpperCase());
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setTextFill(Color.web(C_TEXT_LIGHT));
        lbl.setPadding(new Insets(4, 0, 0, 0));
        return lbl;
    }

    private Label colHeader(String text, double width) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        lbl.setTextFill(Color.web(C_TEXT_LIGHT));
        lbl.setMinWidth(width);
        lbl.setMaxWidth(width);
        return lbl;
    }

    private Label cellLabel(double width) {
        Label lbl = new Label();
        lbl.setFont(Font.font("Arial", 13));
        lbl.setTextFill(Color.web(C_TEXT_DARK));
        lbl.setMinWidth(width);
        lbl.setMaxWidth(width);
        return lbl;
    }

    private HBox padded(HBox node) {
        node.setPadding(new Insets(16, 16, 12, 16));
        return node;
    }

    private void showInlineStatus(Label lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.setTextFill(Color.web(success ? C_GREEN : C_RED));
        FadeTransition ft = new FadeTransition(Duration.millis(200), lbl);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }

    private void pulseButton(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), btn);
        st.setFromX(1); st.setToX(1.04);
        st.setFromY(1); st.setToY(1.04);
        st.setAutoReverse(true); st.setCycleCount(2); st.play();
    }

    private void shakeButton(Button btn) {
        javafx.animation.TranslateTransition tt =
            new javafx.animation.TranslateTransition(Duration.millis(60), btn);
        tt.setByX(6); tt.setAutoReverse(true); tt.setCycleCount(4); tt.play();
    }

    public static void main(String[] args) { launch(); }
}