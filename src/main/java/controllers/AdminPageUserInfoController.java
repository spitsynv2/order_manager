package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import models.Restaurant;
import models.User;

import java.util.*;

public class AdminPageUserInfoController extends AdminPageControllerAbstract {

    @FXML private TableView user_table;

    @FXML private Label username_label;
    @FXML private Label password_label;
    @FXML private Label permission_label;
    @FXML private Label sub_header;

    @FXML private TextField username_field;
    @FXML private TextField password_field;;
    @FXML private ChoiceBox<String> permission_box;
    private ObservableList<User> users;

    public void initializeWithData(User user, boolean islightMode) {
        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();
        createKeyboard();

        List<String> items = new ArrayList<>();
        items.add("Admin");
        items.add("Cashier");

        List<User> userList = User.retrieveAllUsers();
        fillTable(userList);
        users = user_table.getItems();
        int userId = user.getId();

        for (User userTemp : users) {
            if (userTemp.getId() == userId) {
                user_table.getSelectionModel().select(userTemp);
                break;
            }
        }

        permission_box.setItems(FXCollections.observableArrayList(items));

        permission_box.getSelectionModel().select("Admin");
        username_field.setText(user.getName());
        password_field.setText(user.getPassword());

        permission_box.setDisable(userId == 1);

        if (!islightMode){
            handleChangeColorMode();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            user_table.getStyleClass().remove("table-White");
            user_table.getStyleClass().add("table-Dark");
            username_label.getStyleClass().remove("text_label_black_medium");
            username_label.getStyleClass().add("text_label_white_medium");
            password_label.getStyleClass().remove("text_label_black_medium");
            password_label.getStyleClass().add("text_label_white_medium");
            permission_label.getStyleClass().remove("text_label_black_medium");
            permission_label.getStyleClass().add("text_label_white_medium");
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");
            userInfoButton.getStyleClass().remove("active_buttonWhite");
            userInfoButton.getStyleClass().add("active_buttonDark");
        }else {
            user_table.getStyleClass().remove("table-Dark");
            user_table.getStyleClass().add("table-White");
            username_label.getStyleClass().remove("text_label_white_medium");
            username_label.getStyleClass().add("text_label_black_medium");
            password_label.getStyleClass().remove("text_label_white_medium");
            password_label.getStyleClass().add("text_label_black_medium");
            permission_label.getStyleClass().remove("text_label_white_medium");
            permission_label.getStyleClass().add("text_label_black_medium");
            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");
            userInfoButton.getStyleClass().remove("active_buttonDark");
            userInfoButton.getStyleClass().add("active_buttonWhite");
        }
        super.handleChangeColorMode();
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return true;
    }

    private void fillTable(List<User> userList){
        TableColumn<User, Integer> id_column = new TableColumn<>("ID");
        TableColumn<User, String> name_column = new TableColumn<>("Name");
        TableColumn<User, String> password_column = new TableColumn<>("Password");
        TableColumn<User, Integer> permission_column = new TableColumn<>("Permission");

        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        password_column.setCellValueFactory(new PropertyValueFactory<>("password"));
        permission_column.setCellValueFactory(new PropertyValueFactory<>("permission"));

        // Can adjust the size
        id_column.setPrefWidth(75);
        name_column.setPrefWidth(167);
        password_column.setPrefWidth(167);
        permission_column.setPrefWidth(100);

        user_table.setMouseTransparent(true);

        user_table.getColumns().addAll(id_column, name_column, password_column, permission_column);

        user_table.setItems(FXCollections.observableArrayList(userList));
    }
    @FXML
    private void handleGoUpButton() {
        int selectedIndex = user_table.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            user_table.getSelectionModel().select(selectedIndex - 1);
            User selectedUser = (User) user_table.getSelectionModel().getSelectedItem();
            updateFields(selectedUser);
        }
    }
    @FXML
    private void handleGoDownButton() {
        int selectedIndex = user_table.getSelectionModel().getSelectedIndex();

        if (selectedIndex < users.size() - 1) {
            user_table.getSelectionModel().select(selectedIndex + 1);
            User selectedUser = (User) user_table.getSelectionModel().getSelectedItem();
            updateFields(selectedUser);
        }
    }
    private void updateFields(User user) {
        if (user != null) {
            if (user.getPermission() == 1){
                permission_box.setValue("Admin");
            }else {
                permission_box.setValue("Cashier");
            }
            username_field.setText(user.getName());
            password_field.setText(user.getPassword());

            permission_box.setDisable(user.getId() == 1);
        }
    }

    @FXML
    private void handleChangeDataButton() {
        User selectedUser = (User) user_table.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            String newName = username_field.getText();
            String newPassword = password_field.getText();
            String permission = permission_box.getValue();

            int newPermission = 0;
            if (permission.equals("Admin")){
                newPermission = 1;
            }

            if (!selectedUser.getName().equals(newName) || !selectedUser.getPassword().equals(newPassword) || selectedUser.getPermission() != newPermission){
                selectedUser.setName(newName);
                selectedUser.setPassword(newPassword);
                selectedUser.setPermission(newPermission);

                User.updateUser(selectedUser);
                user_table.refresh();
            }
        }
    }
    @FXML
    private void handleDeleteButton(){
        showYesNoDialogButtons("Are you sure you want to delete a User?");
    }
    @FXML
    private void handleAddButton() {
        showYesNoDialogButtons("Are you sure you want to add a User?");
    }
    private void showYesNoDialogButtons(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(msg);
        alert.initStyle(StageStyle.UNDECORATED);

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.initOwner(stage);

        Optional<ButtonType> result = alert.showAndWait();

        if (msg.equals("Are you sure you want to add a User?")){
            if (result.isPresent() && result.get() == yesButton) {
                username_field.setText("Change name");
                password_field.setText("Change password");
                permission_box.getSelectionModel().select("Cashier");
                permission_box.setDisable(false);

                User newUser = new User(User.getNextUserId(),"Change name","Change password",0);

                user_table.getItems().add(newUser);

                user_table.getSelectionModel().select(newUser);

                User.insertUser(newUser);
                User.insertRestaurantUser(newUser,restaurant);
            }
        }

        else if (msg.equals("Are you sure you want to delete a User?")){
            if (result.isPresent() && result.get() == yesButton) {

                User selectedUser = (User) user_table.getSelectionModel().getSelectedItem();

                if (selectedUser != null && selectedUser.getId() != 1) {
                    User.deleteUser(selectedUser,restaurant.getId());
                    user_table.getItems().remove(selectedUser);

                    int selectedIndex = user_table.getSelectionModel().getSelectedIndex();

                    if (selectedIndex >= 0 && selectedIndex < user_table.getItems().size()) {
                        user_table.getSelectionModel().selectNext();
                        selectedUser = (User) user_table.getSelectionModel().getSelectedItem();
                        updateFields(selectedUser);

                        if (selectedUser.getId() == 1){
                            permission_box.setDisable(true);
                        }

                    } else if (selectedIndex > 0) {
                        user_table.getSelectionModel().selectPrevious();
                        selectedUser = (User) user_table.getSelectionModel().getSelectedItem();
                        updateFields(selectedUser);

                        if (selectedUser.getId() == 1){
                            permission_box.setDisable(true);
                        }

                    }
                }else {
                    String errorMessage = "Cannot delete empty or admin user";
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(errorMessage);
                    alert.initOwner(stage);
                    alert.showAndWait();
                }

            }
        }
    }

}
