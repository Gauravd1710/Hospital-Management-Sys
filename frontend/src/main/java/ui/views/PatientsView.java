package ui.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.PatientDTO;

public class PatientsView extends BorderPane {

    public PatientsView(
            ObservableList<PatientDTO> patientList,
            FilteredList<PatientDTO> filteredPatients,
            Runnable refreshAction,
            PatientActionHandler handler
    ) {

        setPadding(new Insets(20));

        Label title
                = new Label("Patient Management");

        title.setStyle(
        "-fx-font-size: 24;"
        + "-fx-font-weight: bold;"
        + "-fx-text-fill: #1E293B;"
);

        TableView<PatientDTO> table
                = new TableView<>();

        table.setItems(filteredPatients);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        table.setStyle(
        "-fx-background-color: white;"
        + "-fx-control-inner-background: white;"
        + "-fx-table-cell-border-color: #E2E8F0;"
        + "-fx-text-background-color: #0F172A;"
        + "-fx-selection-bar: #DBEAFE;"
        + "-fx-selection-bar-text: black;"
        );
        table.lookupAll(".column-header").forEach(header ->
        header.setStyle(
                "-fx-background-color: #1E293B;"
                + "-fx-text-fill: white;"
        )
);

        TableColumn<PatientDTO, String> idCol
                = new TableColumn<>("Patient ID");

        idCol.setCellValueFactory(data
                -> new SimpleStringProperty(
                        data.getValue().getPatientId()
                )
        );

        TableColumn<PatientDTO, String> nameCol
                = new TableColumn<>("Name");

        nameCol.setCellValueFactory(data
                -> new SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        TableColumn<PatientDTO, String> diagnosisCol
                = new TableColumn<>("Diagnosis");

        diagnosisCol.setCellValueFactory(data
                -> new SimpleStringProperty(
                        data.getValue().getDiagnosis()
                )
        );

        TableColumn<PatientDTO, String> typeCol
                = new TableColumn<>("Type");

        typeCol.setCellValueFactory(data
                -> new SimpleStringProperty(
                        data.getValue().getType()
                )
        );

        TableColumn<PatientDTO, String> detailsCol
                = new TableColumn<>("Details");

        detailsCol.setCellValueFactory(data
                -> new SimpleStringProperty(
                        data.getValue().getDetails()
                )
        );

        TableColumn<PatientDTO, Void> actionCol
                = new TableColumn<>("Actions");

        actionCol.setCellFactory(param
                -> new TableCell<>() {

            private final Button editBtn
                    = new Button("Edit");

            private final Button deleteBtn
                    = new Button("Delete");

            private final HBox pane
                    = new HBox(8, editBtn, deleteBtn);

            {

                editBtn.setStyle(
                "-fx-background-color: #3B82F6;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 6;"
                + "-fx-cursor: hand;"
        );

        deleteBtn.setStyle(
                "-fx-background-color: #EF4444;"
                + "-fx-text-fill: white;"
                + "-fx-background-radius: 6;"
                + "-fx-cursor: hand;"
        );

                editBtn.setOnAction(event -> {

                    PatientDTO patient
                            = getTableView()
                                    .getItems()
                                    .get(getIndex());

                    handler.onEdit(patient);
                });

                deleteBtn.setOnAction(event -> {

                    PatientDTO patient
                            = getTableView()
                                    .getItems()
                                    .get(getIndex());

                    handler.onDelete(patient);
                });
            }

            @Override
            protected void updateItem(
                    Void item,
                    boolean empty
            ) {

                super.updateItem(item, empty);

                if (empty) {

                    setGraphic(null);

                } else {

                    setGraphic(pane);
                }
            }
        });

        table.getColumns().addAll(
                idCol,
                nameCol,
                diagnosisCol,
                typeCol,
                detailsCol,
                actionCol
        );

        VBox layout
                = new VBox(20, title, table);

        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );

        setCenter(layout);
    }

    public interface PatientActionHandler {

        void onEdit(PatientDTO patient);

        void onDelete(PatientDTO patient);
    }
}
