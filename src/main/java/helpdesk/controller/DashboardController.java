package helpdesk.controller;

import helpdesk.model.RequestForm;
import helpdesk.model.RequestFormDAO;
import helpdesk.utils.Validation;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {
    @FXML
    TextField id;
    @FXML
    private Label message;
    @FXML
    private ComboBox<String> request_type;
    @FXML
    private TextField email;
    @FXML
    private TextField title;
    @FXML
    private TextField description;
    @FXML
    private RadioButton picture_not_included;
    @FXML
    private RadioButton picture_included;
    @FXML
    private CheckBox it;
    @FXML
    private CheckBox hr;
    @FXML
    private CheckBox logistics;
    @FXML
    private CheckBox marketing;
//    @FXML
//    private Button return_button;
    @FXML
    private Button button_search;
    @FXML
    private Button button_create;
    @FXML
    private TableView table;
    private ResultSet rsAllEntries;
    private ObservableList<ObservableList> data = FXCollections.observableArrayList();
    // Jei nerodo ikonėlių prie FXML elementų patikrinti ar FXML'e yra nurodytas kontroleris

    public void initialize() {
        try {
            this.updateTableFromDB("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Funkcija grąžinantį vartotoją į login langą
     */
    public void goToLogin(ActionEvent actionEvent) {
        try {
            // Sukuriamas dashboard langas
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("view/login.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 550, 450));
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public void onCreateAction(ActionEvent actionEvent) {
        this.message.setText("");
        String requestType = "";
        if (this.request_type.getSelectionModel().isEmpty()) {
            this.message.setText("Request type required");
            return;
        } else {
            requestType = this.request_type.getSelectionModel().getSelectedItem().toString();
        }

        String email = "";
        if (Validation.isValidEmail(this.email.getText())) {
            email = this.email.getText();
        } else {
            this.message.setText("Email is required or incorrect");
            return;
        }

        String title = "";
        if (Validation.isTitleValid(this.title.getText())) {
            title = this.title.getText();
        } else {
            this.message.setText("Title is not valid");
            return;
        }

        String description = "";
        if (Validation.isDesciptionValid(this.description.getText())) {
            description = this.description.getText();
        } else {
            this.message.setText("Description is not valid");
            return;
        }

        boolean isPictureIncluded = false;
        if (this.picture_included.isSelected()) {
            isPictureIncluded = true;
        }

        String interests = "";
        boolean isInterestSelected = false;
        if (this.marketing.isSelected()) {
            interests += this.marketing.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.it.isSelected()) {
            interests += this.it.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.hr.isSelected()) {
            interests += this.hr.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.logistics.isSelected()) {
            interests += this.logistics.getText() + ", ";
            isInterestSelected = true;
        }

        if (!isInterestSelected) {
            this.message.setText("Please select an interest");
            return;
        }

        try {
            RequestFormDAO.create(new RequestForm(requestType, email, title, description, isPictureIncluded, interests));
            this.message.setTextFill(Color.GREEN);
            this.message.setText("Successfully added new entry to database");

            this.updateTableFromDB("");
        } catch (SQLException throwables) {
            this.message.setText("Failed to add an entry to database");
        }
    }

    public void onActionEdit(ActionEvent actionEvent) {
        int id = 0;
        if (Validation.isIdValid(this.id.getText())) {
            id = Integer.parseInt(this.id.getText());
        } else {
            this.message.setText("Id is not valid");
        }

        this.message.setText("");
        String requestType = "";
        if (this.request_type.getSelectionModel().isEmpty()) {
            this.message.setText("Request type required");
            return;
        } else {
            requestType = this.request_type.getSelectionModel().getSelectedItem().toString();
        }

        String email = "";
        if (Validation.isValidEmail(this.email.getText())) {
            email = this.email.getText();
        } else {
            this.message.setText("Email is required or incorrect");
            return;
        }

        String title = "";
        if (Validation.isTitleValid(this.title.getText())) {
            title = this.title.getText();
        } else {
            this.message.setText("Title is not valid");
            return;
        }

        String description = "";
        if (Validation.isDesciptionValid(this.description.getText())) {
            description = this.description.getText();
        } else {
            this.message.setText("Description is not valid");
            return;
        }

        boolean isPictureIncluded = false;
        if (this.picture_included.isSelected()) {
            isPictureIncluded = true;
        }

        String interests = "";
        boolean isInterestSelected = false;
        if (this.marketing.isSelected()) {
            interests += this.marketing.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.it.isSelected()) {
            interests += this.it.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.hr.isSelected()) {
            interests += this.hr.getText() + ", ";
            isInterestSelected = true;
        }
        if (this.logistics.isSelected()) {
            interests += this.logistics.getText() + ", ";
            isInterestSelected = true;
        }

        if (!isInterestSelected) {
            this.message.setText("Please select an interest");
            return;
        }

        try {
            RequestFormDAO.update(new RequestForm(id, requestType, email, title, description, isPictureIncluded, interests));
            this.message.setTextFill(Color.GREEN);
            this.message.setText("Successfully updated entry");

            this.updateTableFromDB("");
        } catch (SQLException throwables) {
            this.message.setText("Failed to update entry");
        }
    }

    public void onActionDelete(ActionEvent actionEvent) {
        if (Validation.isIdValid(this.id.getText())) {
            try {
                RequestFormDAO.delete(Integer.parseInt(this.id.getText()));
                this.message.setTextFill(Color.GREEN);
                this.message.setText("Form deleted successfully");

                this.updateTableFromDB("");
            } catch (SQLException throwables) {
                this.message.setText("Failed to delete a request");
            }
        } else {
            this.message.setText("ID is not valid");
        }
    }

    public void onSearchAction(ActionEvent actionEvent) {
        try {
            this.updateTableFromDB(this.title.getText());
            this.table.setItems(this.data);
        } catch (SQLException throwables) {
            this.message.setText("Search failed");
            throwables.printStackTrace();
        }
    }

    private void updateTableFromDB(String title) throws SQLException {
        this.rsAllEntries = RequestFormDAO.search(title);
        this.fetchColumnList();
        this.fetchRowList();
    }

    //only fetch columns
    private void fetchColumnList() {
        try {
            table.getColumns().clear();
            //SQL FOR SELECTING ALL OF ENTRIES
            for (int i = 0; i < rsAllEntries.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rsAllEntries.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                table.getColumns().removeAll(col);
                table.getColumns().addAll(col);
            }
        } catch (SQLException e) {
            this.message.setText("Failure in getting all entries");
            e.printStackTrace();
        }
    }

    //fetches rows and data from the list
    private void fetchRowList() {
        try {
            data.clear();
            while (rsAllEntries.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rsAllEntries.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rsAllEntries.getString(i));
                }
                data.add(row);
            }
            //Connects table with list
            table.setItems(data);
        } catch (SQLException ex) {
            this.message.setText("Failure in getting all entries");
        }
    }
}
