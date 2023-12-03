package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;
import models.Dish;
import models.Restaurant;
import models.User;

import java.util.List;
import java.util.Optional;

public class AdminPageDishesController extends AdminPageControllerAbstract{

    @FXML private TableView dish_table;

    @FXML private Label sub_header;

    @FXML private Label name_label;
    @FXML private Label price_label;
    @FXML private Label ingredients_label;
    @FXML private Label info_label;

    @FXML private TextField name_field;
    @FXML private TextField price_field;

    @FXML private TextArea info_field;
    @FXML private TextArea ingredients_field;

    private ObservableList<Dish> dishes;

    private Label[] labels;

    public void initializeWithData(User user) {
        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();
        labels = new Label[]{name_label, price_label, ingredients_label, info_label};
        createKeyboard();

        //TODO DATABASE RETRIEVE DATA

        List<Dish> dishList = Dish.retrieveAllDishes();
        fillTable(dishList);

        dishes = dish_table.getItems();

        dish_table.getSelectionModel().select(dishes.get(0));

        name_field.setText(dishes.get(0).getName());
        ingredients_field.setText(dishes.get(0).getIngredients());
        info_field.setText(dishes.get(0).getInfo());
        price_field.setText(dishes.get(0).getPrice()+"");


        price_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*\\.?\\d*")) {
                    price_field.setText(newValue.replaceAll("[^\\d.]+", "").replaceAll("(\\.\\d*)\\.", "$1"));
                }
            }
        });
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return true;
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            dishesButton.getStyleClass().remove("active_buttonWhite");
            dishesButton.getStyleClass().add("active_buttonDark");

        }else {
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

            dishesButton.getStyleClass().remove("active_buttonDark");
            dishesButton.getStyleClass().add("active_buttonWhite");
        }
        super.handleChangeColorMode();
    }

    private void fillTable(List<Dish> dishList){
        TableColumn<User, Integer> id_column = new TableColumn<>("Name");
        TableColumn<User, String> name_column = new TableColumn<>("Ingredients");
        TableColumn<User, String> password_column = new TableColumn<>("Info");
        TableColumn<User, Integer> permission_column = new TableColumn<>("Price");

        id_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        password_column.setCellValueFactory(new PropertyValueFactory<>("info"));
        permission_column.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Can adjust the size
        id_column.setPrefWidth(100);
        name_column.setPrefWidth(167);
        password_column.setPrefWidth(167);
        permission_column.setPrefWidth(75);

        dish_table.setMouseTransparent(true);

        dish_table.getColumns().addAll(id_column, name_column, password_column, permission_column);

        dish_table.setItems(FXCollections.observableArrayList(dishList));
    }


    @FXML
    private void handleGoUpButton() {
        int selectedIndex = dish_table.getSelectionModel().getSelectedIndex();
        if (selectedIndex > 0) {
            dish_table.getSelectionModel().select(selectedIndex - 1);
            Dish selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();
            updateFields(selectedDish);
        }
    }
    @FXML
    private void handleGoDownButton() {
        int selectedIndex = dish_table.getSelectionModel().getSelectedIndex();

        if (selectedIndex < dishes.size() - 1) {
            dish_table.getSelectionModel().select(selectedIndex + 1);
            Dish selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();
            updateFields(selectedDish);
        }
    }
    private void updateFields(Dish dish) {
        if (dish != null) {
            name_field.setText(dish.getName());
            price_field.setText(dish.getPrice()+"");
            ingredients_field.setText(dish.getIngredients());
            info_field.setText(dish.getInfo());
        }
    }

    @FXML
    private void handleChangeDataButton() {
        Dish selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();

        if (selectedDish != null) {
            String newName = name_field.getText();
            String oldName = selectedDish.getName();
            double newPrice = Double.parseDouble(price_field.getText());
            String newIngredients = ingredients_field.getText();
            String newInfo = info_field.getText();


            if (!selectedDish.getName().equals(newName)
                    || selectedDish.getPrice() != newPrice
                    || !selectedDish.getIngredients().equals(newIngredients)
                    || !selectedDish.getInfo().equals(newInfo))
            {
                selectedDish.setName(newName);
                selectedDish.setIngredients(newIngredients);
                selectedDish.setPrice(newPrice);
                selectedDish.setInfo(newInfo);

                Dish.updateDish(selectedDish,oldName);
                Dish.updateDishStatus(selectedDish.getName(),restaurant.getName(),"Available",oldName);
                dish_table.refresh();
            }
        }
    }
    @FXML
    private void handleDeleteButton(){
        showYesNoDialogButtons("Are you sure you want to delete a Dish?");
    }
    @FXML
    private void handleAddButton() {
        showYesNoDialogButtons("Are you sure you want to add a Dish?");
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

        if (msg.equals("Are you sure you want to add a Dish?")){
            if (result.isPresent() && result.get() == yesButton) {
                name_field.setText("Change name");
                ingredients_field.setText("Change ingredients");
                info_field.setText("Change info");
                price_field.setText("0.00");

                Dish newDish = new Dish("Change name", "Change ingredients", "Change info",0);

                dish_table.getItems().add(newDish);

                dish_table.getSelectionModel().select(newDish);

                Dish.insertDish(newDish, restaurant.getName(),"Available");
            }
        }

        else if (msg.equals("Are you sure you want to delete a Dish?")){
            if (result.isPresent() && result.get() == yesButton) {

                Dish selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();

                if (selectedDish != null) {
                    Dish.deleteDish(selectedDish.getName());
                    dish_table.getItems().remove(selectedDish);

                    int selectedIndex = dish_table.getSelectionModel().getSelectedIndex();

                    if (selectedIndex >= 0 && selectedIndex < dish_table.getItems().size()) {
                        dish_table.getSelectionModel().selectNext();
                        selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();
                        updateFields(selectedDish);

                    } else if (selectedIndex > 0) {
                        dish_table.getSelectionModel().selectPrevious();
                        selectedDish = (Dish) dish_table.getSelectionModel().getSelectedItem();
                        updateFields(selectedDish);
                    }
                }

            }
        }
    }

}
