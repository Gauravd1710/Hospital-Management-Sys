
package ui;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.PatientDTO;
import service.ApiService;
import ui.Main.PatientCell;
import ui.views.PatientsView;
import ui.views.ReportsView;

// ===================== BACKEND =====================


// ===================== FRONTEND =====================

public class Main extends Application {
    private ObservableList<PatientDTO> patientList = FXCollections.observableArrayList();
    private FilteredList<PatientDTO> filteredPatients = new FilteredList<>(patientList, p -> true);
    private boolean editingMode = false;
    private long editingPatientId = -1;

    Label bedCountLabel;
    Label apptCountLabel;
    Label totalCountLabel;

    TextField idField;
    TextField nameField;
    TextField diagnosisField;
    TextField extraField;
    private TextField searchField;

    ToggleButton bedBtn;
    ToggleButton apptBtn;

    Button addBtn;

    private StackPane contentArea;
    private PatientsView patientsView;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + AppTheme.C_PAGE_BG + ";");

        contentArea = new StackPane();
        contentArea.getChildren().add(buildBody());
        patientsView = createPatientsView();
        loadPatientsFromDatabase();
        VBox.setVgrow(contentArea, Priority.ALWAYS);

        root.getChildren().addAll(buildHeader(), contentArea);

        Scene scene = new Scene(root, 1240, 780);
        stage.setTitle("NexaCare — Smart Hospital Management");
        stage.setScene(scene);
        stage.setMinWidth(1060);
        stage.setMinHeight(680);
        stage.show();
    }

    // ===================== HEADER =====================

    private HBox buildHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(0, 28, 0, 22));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(66);
        header.setStyle(
            "-fx-background-color: " + AppTheme.C_WHITE + ";" +
            "-fx-border-color: " + AppTheme.C_BORDER + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 2);"
        );

        HBox logoGroup = AppTheme.buildLogoGroup(false);

        // Thin vertical divider
        Region vDiv = new Region();
        vDiv.setPrefSize(1, 30); vDiv.setMinSize(1, 30); vDiv.setMaxSize(1, 30);
        vDiv.setStyle("-fx-background-color: " + AppTheme.C_BORDER + ";");
        HBox divWrap = new HBox(vDiv);
        divWrap.setAlignment(Pos.CENTER);
        divWrap.setPadding(new Insets(0, 22, 0, 20));

        // Nav pills
        Label navDashboard = navPill("Dashboard", true);
        Label navPatients  = navPill("Patients",  false);
        Label navReports   = navPill("Reports",   false);

        navDashboard.setOnMouseClicked(e -> {
            setActiveNav(navDashboard, navPatients, navReports);
            contentArea.getChildren().setAll(buildBody());
            loadPatientsFromDatabase();
        });
        navPatients.setOnMouseClicked(e -> {
            setActiveNav(navPatients, navDashboard, navReports);
            contentArea.getChildren().setAll(patientsView);
        });
        navReports.setOnMouseClicked(e -> {
            setActiveNav(navReports, navDashboard, navPatients);
            contentArea.getChildren().setAll(ReportsView.getView());
        });

        HBox navRow = new HBox(6, navDashboard, navPatients, navReports);
        navRow.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Status badge
        StackPane dot = new StackPane();
        dot.setPrefSize(7, 7); dot.setMinSize(7, 7);
        dot.setStyle("-fx-background-color: " + AppTheme.C_GREEN + "; -fx-background-radius: 4;");
        Label statusLbl = new Label("System Online");
        statusLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        statusLbl.setTextFill(Color.web(AppTheme.C_GREEN));
        HBox statusBadge = new HBox(6, dot, statusLbl);
        statusBadge.setAlignment(Pos.CENTER);
        statusBadge.setPadding(new Insets(5, 12, 5, 12));
        statusBadge.setStyle(
            "-fx-background-color: " + AppTheme.C_GREEN_LIGHT + ";" +
            "-fx-background-radius: 14;"
        );

        // User pill
        StackPane avatar = new StackPane();
        avatar.setPrefSize(34, 34); avatar.setMinSize(34, 34);
        avatar.setStyle(
            "-fx-background-color: " + AppTheme.C_BLUE_LIGHT + ";" +
            "-fx-background-radius: 17;" +
            "-fx-border-color: " + AppTheme.C_BLUE + ";" +
            "-fx-border-radius: 17; -fx-border-width: 1.5;"
        );
        Label avLbl = new Label("DA");
        avLbl.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        avLbl.setTextFill(Color.web(AppTheme.C_BLUE));
        avatar.getChildren().add(avLbl);

        VBox userInfo = new VBox(1);
        Label userName = new Label("Dr. Admin");
        userName.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        userName.setTextFill(Color.web(AppTheme.C_TEXT_DARK));
        Label userRole = new Label("Administrator");
        userRole.setFont(Font.font("Arial", 10));
        userRole.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));
        userInfo.getChildren().addAll(userName, userRole);

        HBox userBox = new HBox(9, avatar, userInfo);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(0, 0, 0, 18));

        header.getChildren().addAll(logoGroup, divWrap, navRow, spacer, statusBadge, userBox);
        return header;
    }

    private void setActiveNav(Label active, Label... inactives) {
        active.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        active.setTextFill(Color.web(AppTheme.C_BLUE));
        active.setStyle(
            "-fx-background-color: " + AppTheme.C_BLUE_LIGHT + ";" +
            "-fx-background-radius: 20; -fx-cursor: hand;"
        );
        for (Label l : inactives) {
            l.setFont(Font.font("Arial", FontWeight.NORMAL, 13));
            l.setTextFill(Color.web(AppTheme.C_TEXT_MED));
            l.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-background-radius: 20; -fx-cursor: hand;"
            );
        }
    }

    private Label navPill(String text, boolean active) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", active ? FontWeight.BOLD : FontWeight.NORMAL, 13));
        lbl.setPadding(new Insets(7, 18, 7, 18));
        lbl.setTextFill(Color.web(active ? AppTheme.C_BLUE : AppTheme.C_TEXT_MED));
        lbl.setStyle(
            "-fx-background-color: " + (active ? AppTheme.C_BLUE_LIGHT : "transparent") + ";" +
            "-fx-background-radius: 20; -fx-cursor: hand;"
        );
        lbl.setOnMouseEntered(e -> {
            if (!lbl.getTextFill().equals(Color.web(AppTheme.C_BLUE)))
                lbl.setStyle("-fx-background-color: " + AppTheme.C_BLUE_LIGHT +
                             "; -fx-background-radius: 20; -fx-cursor: hand;");
        });
        lbl.setOnMouseExited(e -> {
            if (!lbl.getTextFill().equals(Color.web(AppTheme.C_BLUE)))
                lbl.setStyle("-fx-background-color: transparent;" +
                             "-fx-background-radius: 20; -fx-cursor: hand;");
        });
        return lbl;
    }

    private PatientsView createPatientsView() {
        return new PatientsView(
                patientList,
                filteredPatients,
                this::loadPatientsFromDatabase,
                new PatientsView.PatientActionHandler() {
                    @Override
                    public void onEdit(PatientDTO patient) {
                        contentArea.getChildren().setAll(buildBody());
                        loadPatientsFromDatabase();

                        idField.setText(patient.getPatientId());
                        nameField.setText(patient.getName());
                        diagnosisField.setText(patient.getDiagnosis());
                        extraField.setText(patient.getDetails());

                        if (patient.getType().equals("BED")) {
                            bedBtn.setSelected(true);
                        } else {
                            apptBtn.setSelected(true);
                        }

                        editingMode = true;
                        editingPatientId = patient.getId();
                        addBtn.setText("Update Patient");
                    }

                    @Override
                    public void onDelete(PatientDTO patient) {
                        Alert confirm =
                                new Alert(Alert.AlertType.CONFIRMATION);

                        confirm.setTitle("Delete Patient");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Delete this patient?");

                        confirm.showAndWait()
                                .ifPresent(response -> {
                                    if (response == ButtonType.OK) {
                                        boolean success =
                                                ApiService.deletePatient(
                                                        patient.getId()
                                                );

                                        if (success) {
                                            loadPatientsFromDatabase();
                                        } else {
                                            Alert error =
                                                    new Alert(
                                                            Alert.AlertType.ERROR
                                                    );

                                            error.setContentText(
                                                    "Delete failed!"
                                            );
                                            error.showAndWait();
                                        }
                                    }
                                });
                    }
                }
        );
    }

    // ===================== BODY =====================

    private void loadPatientsFromDatabase() {

        patientList.clear();

        try {

            patientList.addAll(
                    ApiService.getAllPatients()
            );

        } catch (Exception e) {

            e.printStackTrace();

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Failed to load patients from database!"
            );

            alert.showAndWait();
        }

        long bedCount =
                patientList.stream()
                        .filter(patient ->
                                "BED".equals(patient.getType()))
                        .count();

        long appointmentCount =
                patientList.stream()
                        .filter(patient ->
                                "APPOINTMENT".equals(patient.getType()))
                        .count();

        totalCountLabel.setText(
                String.valueOf(patientList.size())
        );

        bedCountLabel.setText(
                String.valueOf(bedCount)
        );

        apptCountLabel.setText(
                String.valueOf(appointmentCount)
        );
    }

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

        HBox cardHeader = new HBox(10);
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        cardHeader.setPadding(new Insets(0, 0, 14, 0));

        Circle dot = new Circle(5, Color.web(AppTheme.C_TEAL));

        VBox headerText = new VBox(2);
        Label cardTitle = new Label("Add New Patient");
        cardTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        cardTitle.setTextFill(Color.web(AppTheme.C_TEXT_DARK));
        Label cardSub = new Label("Fill in the details below to register");
        cardSub.setFont(Font.font("Arial", 11));
        cardSub.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));
        headerText.getChildren().addAll(cardTitle, cardSub);
        cardHeader.getChildren().addAll(dot, headerText);

        ToggleGroup typeGroup = new ToggleGroup();
        bedBtn  = typeToggle("Bed Patient", typeGroup, true);
        apptBtn = typeToggle("Appointment", typeGroup, false);
        HBox typeRow = new HBox(8, bedBtn, apptBtn);

        idField        = styledField("Patient ID  e.g. P-001");
        nameField      = styledField("Full Name  e.g. Rahul Sharma");
        diagnosisField = styledField("Diagnosis  e.g. Hypertension");
        extraField     = styledField("Room No.  e.g. 204");

        typeGroup.selectedToggleProperty().addListener((obs, old, nw) -> {
            if (nw == bedBtn) extraField.setPromptText("Room No.  e.g. 204");
            else              extraField.setPromptText("Date  e.g. 2026-06-01");
        });

        addBtn   = primaryButton("Add Patient");
        Button clearBtn = dangerButton("Clear All Records");

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statusLabel.setWrapText(true);

       addBtn.setOnAction(e -> {

            String patientId = idField.getText().trim();
            String name = nameField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            String details = extraField.getText().trim();

            if (patientId.isEmpty() ||
                name.isEmpty() ||
                diagnosis.isEmpty() ||
                details.isEmpty()) {

                Alert alert =
                        new Alert(Alert.AlertType.WARNING);

                alert.setTitle("Validation Error");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Please fill all fields.");

                alert.showAndWait();

                return;
            }

            String type;

            if (bedBtn.isSelected()) {
                type = "BED";
            } else {
                type = "APPOINTMENT";
            }

            PatientDTO patient =
                    new PatientDTO(
                            patientId,
                            name,
                            diagnosis,
                            type,
                            details
                    );

            

            boolean success;

            if (editingMode) {

                success =
                        ApiService.updatePatient(
                                editingPatientId,
                                patient
                        );

            } else {

                success =
                        ApiService.savePatient(patient);
            }

            if (success) {

                loadPatientsFromDatabase();

                Alert alert =
                        new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(
                        editingMode
                                ? "Patient updated successfully!"
                                : "Patient added successfully!");

                alert.showAndWait();

                idField.clear();
                nameField.clear();
                diagnosisField.clear();
                extraField.clear();

                editingMode = false;
                editingPatientId = -1;
                addBtn.setText("+ Add Patient");

            } else {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Failed to save patient!");

                alert.showAndWait();
            }
        });

        clearBtn.setOnAction(e -> {
            patientList.clear();
            bedCountLabel.setText("0");
            apptCountLabel.setText("0");
            totalCountLabel.setText("0");
            editingMode = false;
            editingPatientId = -1;
            addBtn.setText("+ Add Patient");
            showInlineStatus(statusLabel, "All records cleared.", true);
        });

        formCard.getChildren().addAll(
            cardHeader,
            sectionLabel("PATIENT TYPE"),       typeRow,
            thinDivider(),
            sectionLabel("IDENTIFICATION"),     idField, nameField,
            thinDivider(),
            sectionLabel("MEDICAL INFO"),       diagnosisField,
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
            statCard("Total Patients",  totalCountLabel, AppTheme.C_BLUE,  AppTheme.C_BLUE_LIGHT,  "P"),
            statCard("Admitted (Beds)", bedCountLabel,   AppTheme.C_TEAL,  AppTheme.C_TEAL_LIGHT,  "B"),
            statCard("Appointments",    apptCountLabel,  AppTheme.C_GREEN, AppTheme.C_GREEN_LIGHT, "A")
        );

        VBox recordsCard = card();

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setPadding(new Insets(0, 0, 12, 0));

        Region accentBar = new Region();
        accentBar.setPrefSize(4, 22); accentBar.setMinSize(4, 22);
        accentBar.setStyle("-fx-background-color: " + AppTheme.C_BLUE + "; -fx-background-radius: 2;");

        Label title = new Label("Patient Records");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
        title.setTextFill(Color.web(AppTheme.C_TEXT_DARK));
        titleRow.getChildren().addAll(accentBar, title);

        searchField = new TextField();
        searchField.setPromptText("Search patients...");
        searchField.setStyle(
            "-fx-background-radius: 12;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14;" +
            "-fx-background-color: #F3F6FA;" +
            "-fx-border-color: #D6DFEA;" +
            "-fx-text-fill: #1E293B;" +
            "-fx-prompt-text-fill: #94A3B8;"
        );
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredPatients.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String search = newValue.toLowerCase();

                return patient.getPatientId().toLowerCase().contains(search)
                        || patient.getName().toLowerCase().contains(search)
                        || patient.getDiagnosis().toLowerCase().contains(search);
            });
        });

        ListView<PatientDTO> listView = new ListView<>(filteredPatients);
        listView.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        listView.setPlaceholder(
        new Label("No patients found in database.")
        );

        listView.setFixedCellSize(76);
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setCellFactory(lv -> new PatientCell());

        recordsCard.getChildren().addAll(titleRow, searchField, listView);
        VBox.setVgrow(recordsCard, Priority.ALWAYS);

        col.getChildren().addAll(statsRow, recordsCard);
        VBox.setVgrow(col, Priority.ALWAYS);
        return col;
    }

    // ===================== LIST CELL =====================

    class PatientCell extends ListCell<PatientDTO> {
        @Override
        protected void updateItem(PatientDTO p, boolean empty) {
            super.updateItem(p, empty);
            setStyle("-fx-background-color: transparent; -fx-padding: 0;");
            if (empty || p == null) {
                setText(null); setGraphic(null);
            } else {
                boolean isBed  = p.getType().equals("BED");
                String accent  = isBed ? AppTheme.C_TEAL  : AppTheme.C_BLUE;
                String bgCol   = isBed ? AppTheme.C_TEAL_LIGHT : AppTheme.C_BLUE_LIGHT;

                HBox row = new HBox(14);
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(10, 14, 10, 14));
                row.setStyle(
                    "-fx-background-color: " + AppTheme.C_WHITE + ";" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: " + AppTheme.C_BORDER + ";" +
                    "-fx-border-radius: 12;" +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 6, 0, 0, 2);"
                );

                Region stripe = new Region();
                stripe.setPrefSize(4, 42); stripe.setMinSize(4, 42);
                stripe.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 2;");

                StackPane avatar = new StackPane();
                avatar.setPrefSize(42, 42); avatar.setMinSize(42, 42);
                avatar.setStyle(
                    "-fx-background-color: " + bgCol + ";" +
                    "-fx-background-radius: 21;" +
                    "-fx-border-color: " + accent + ";" +
                    "-fx-border-radius: 21; -fx-border-width: 1.5;"
                );
                Label initLbl = new Label(String.valueOf(p.getName().charAt(0)).toUpperCase());
                initLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
                initLbl.setTextFill(Color.web(accent));
                avatar.getChildren().add(initLbl);

                VBox info = new VBox(3);
                HBox nameRow = new HBox(8);
                nameRow.setAlignment(Pos.CENTER_LEFT);

                Label nameLbl = new Label(p.getName());
                nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                nameLbl.setTextFill(Color.web(AppTheme.C_TEXT_DARK));

                Label badge = new Label(p.getType());
                badge.setFont(Font.font("Arial", FontWeight.BOLD, 9));
                badge.setPadding(new Insets(2, 8, 2, 8));
                badge.setTextFill(Color.web(accent));
                badge.setStyle(
                    "-fx-background-color: " + bgCol + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-color: " + accent + ";" +
                    "-fx-border-radius: 8; -fx-border-width: 1;"
                );
                nameRow.getChildren().addAll(nameLbl, badge);

                Label detailLbl = new Label(p.getDiagnosis() + "   |   " + p.getDetails());
                detailLbl.setFont(Font.font("Arial", 12));
                detailLbl.setTextFill(Color.web(AppTheme.C_TEXT_MED));

                Label idLbl = new Label("ID: " + p.getPatientId());
                idLbl.setFont(Font.font("Arial", 11));
                idLbl.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));

                info.getChildren().addAll(nameRow, detailLbl, idLbl);
                HBox.setHgrow(info, Priority.ALWAYS);

                Button editBtn = new Button("Edit");
                editBtn.setStyle(
                    "-fx-background-color: #2B7FD4;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
                );
                editBtn.setOnAction(e -> {
                    idField.setText(p.getPatientId());
                    nameField.setText(p.getName());
                    diagnosisField.setText(p.getDiagnosis());
                    extraField.setText(p.getDetails());

                    if (p.getType().equals("BED")) {
                        bedBtn.setSelected(true);
                    } else {
                        apptBtn.setSelected(true);
                    }

                    editingMode = true;
                    editingPatientId = p.getId();
                    addBtn.setText("Update Patient");
                });

                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle("-fx-background-color: #ff4d4d;" +"-fx-text-fill: white;" + "-fx-background-radius: 10;" +
                    "-fx-cursor: hand;"
                );
                deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert( Alert.AlertType.CONFIRMATION );

            confirm.setTitle("Delete Patient");

            confirm.setHeaderText(null);

            confirm.setContentText("Delete this patient?");

            confirm.showAndWait().ifPresent(response -> {
                        if (response ==
                                ButtonType.OK) {

                            boolean success =ApiService.deletePatient(p.getId());

                            if (success) {

                                loadPatientsFromDatabase();

                            } else {

                                Alert error =
                                        new Alert(
                                                Alert.AlertType.ERROR
                                        );

                                error.setContentText(
                                        "Delete failed!"
                                );

                                error.showAndWait();
                            }
                        }
                    });
        });

                HBox actions = new HBox(8, editBtn, deleteBtn);

                row.getChildren().addAll(stripe, avatar, info, actions);
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
            "-fx-background-color: " + AppTheme.C_WHITE + ";" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: " + AppTheme.C_BORDER + ";" +
            "-fx-border-radius: 16;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );
        return box;
    }

    private HBox statCard(String title, Label value,
                          String accent, String bg, String letter) {
        HBox card = new HBox(0);
        card.setStyle(
            "-fx-background-color: " + AppTheme.C_WHITE + ";" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: " + AppTheme.C_BORDER + ";" +
            "-fx-border-radius: 14;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 12, 0, 0, 3);"
        );
        HBox.setHgrow(card, Priority.ALWAYS);

        Region bar = new Region();
        bar.setPrefWidth(5); bar.setMinWidth(5);
        bar.setStyle("-fx-background-color: " + accent + "; -fx-background-radius: 14 0 0 14;");

        StackPane ic = new StackPane();
        ic.setPrefSize(44, 44); ic.setMinSize(44, 44);
        ic.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-background-radius: 22;" +
            "-fx-border-color: " + accent + "; -fx-border-radius: 22; -fx-border-width: 1.5;"
        );
        Label icLbl = new Label(letter);
        icLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        icLbl.setTextFill(Color.web(accent));
        ic.getChildren().add(icLbl);

        VBox content = new VBox(3);
        content.setPadding(new Insets(14, 16, 14, 14));
        content.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(content, Priority.ALWAYS);

        value.setFont(Font.font("Georgia", FontWeight.BOLD, 34));
        value.setTextFill(Color.web(accent));

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Arial", 12));
        titleLbl.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));

        content.getChildren().addAll(ic, value, titleLbl);
        card.getChildren().addAll(bar, content);
        return card;
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefHeight(40);
        tf.setStyle(
            "-fx-background-color: " + AppTheme.C_PAGE_BG + ";" +
            "-fx-background-radius: 8;" +
            "-fx-border-color: " + AppTheme.C_BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + AppTheme.C_TEXT_DARK + ";"
        );
        tf.focusedProperty().addListener((obs, old, focused) ->
            tf.setStyle(
                "-fx-background-color: " + AppTheme.C_WHITE + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: " + (focused ? AppTheme.C_TEAL : AppTheme.C_BORDER) + ";" +
                "-fx-border-radius: 8;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + AppTheme.C_TEXT_DARK + ";"
            )
        );
        return tf;
    }

    private ToggleButton typeToggle(String text, ToggleGroup group, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setSelected(selected);
        btn.setPrefHeight(36);
        String on  = "-fx-background-color: " + AppTheme.C_BLUE + "; -fx-text-fill: white;" +
                     "-fx-background-radius: 8; -fx-font-size: 12px; -fx-font-weight: bold;";
        String off = "-fx-background-color: " + AppTheme.C_PAGE_BG + "; -fx-text-fill: " + AppTheme.C_TEXT_MED + ";" +
                     "-fx-border-color: " + AppTheme.C_BORDER + "; -fx-background-radius: 8;" +
                     "-fx-border-radius: 8; -fx-font-size: 12px;";
        btn.setStyle(selected ? on : off);
        btn.selectedProperty().addListener((obs, old, nw) -> btn.setStyle(nw ? on : off));
        return btn;
    }

    private Button primaryButton(String text) {
        Button btn = new Button("+ " + text);
        btn.setPrefHeight(44); btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        String base  = "-fx-background-color: " + AppTheme.C_BLUE  + "; -fx-text-fill: white;" +
                       "-fx-background-radius: 10;" +
                       "-fx-effect: dropshadow(gaussian,rgba(43,127,212,0.30),8,0,0,3);";
        String hover = "-fx-background-color: " + AppTheme.C_BLUE_DARK + "; -fx-text-fill: white;" +
                       "-fx-background-radius: 10;" +
                       "-fx-effect: dropshadow(gaussian,rgba(43,127,212,0.45),10,0,0,4);";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Button dangerButton(String text) {
        Button btn = new Button("x  " + text);
        btn.setPrefHeight(40); btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Arial", 12));
        String base  = "-fx-background-color: " + AppTheme.C_WHITE + "; -fx-text-fill: " + AppTheme.C_RED + ";" +
                       "-fx-border-color: " + AppTheme.C_BORDER + "; -fx-background-radius: 10;" +
                       "-fx-border-radius: 10;";
        String hover = "-fx-background-color: " + AppTheme.C_RED_LIGHT + "; -fx-text-fill: " + AppTheme.C_RED + ";" +
                       "-fx-border-color: " + AppTheme.C_RED + "; -fx-background-radius: 10;" +
                       "-fx-border-radius: 10;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lbl.setTextFill(Color.web(AppTheme.C_TEXT_LIGHT));
        lbl.setPadding(new Insets(6, 0, 0, 0));
        return lbl;
    }

    private Region thinDivider() {
        Region r = new Region();
        r.setPrefHeight(1); r.setMaxHeight(1);
        r.setStyle("-fx-background-color: " + AppTheme.C_BORDER + ";");
        VBox.setMargin(r, new Insets(6, 0, 2, 0));
        return r;
    }

    private void showInlineStatus(Label lbl, String msg, boolean success) {
        lbl.setText((success ? "✓  " : "!  ") + msg);
        lbl.setTextFill(Color.web(success ? AppTheme.C_GREEN : AppTheme.C_RED));
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

    public static void main(String[] args) { launch(args); }
}
