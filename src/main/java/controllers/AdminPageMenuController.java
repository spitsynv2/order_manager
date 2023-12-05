package controllers;

import controllers.fragments.MenuItemController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Dish;
import models.Restaurant;
import models.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AdminPageMenuController extends AdminPageControllerAbstract {

    @FXML
    private Label sub_header;
    @FXML
    private Label sub_header2;
    @FXML
    private GridPane menu_gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea text_area;
    @FXML
    private ChoiceBox sort_choiceBox;

    private List<Dish> dishList = new LinkedList<>();
    List<Dish> dishListByPopularity = new ArrayList<>();

    private List<VBox> products = new ArrayList<>();

    private List<Dish> selectedDish = new ArrayList<>();

    private List<String> types = new ArrayList<>();

    private String text;

    private Map<Dish, Integer> selectedDishCountMap = new LinkedHashMap<>();

    public void initializeWithData(User user, boolean islightMode) throws IOException {

        this.user = user;
        restaurant = Restaurant.retrieveRestaurant();

        scrollPane.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getEventType() == ScrollEvent.SCROLL) {
                double deltaY = event.getDeltaY();
                scrollPane.setVvalue(scrollPane.getVvalue() - deltaY / scrollPane.getHeight());
                event.consume();
            }
        });
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        dishList = Dish.retrieveAllDishes();
        dishListByPopularity = sortDishesByPopularity(dishList, Dish.retrieveDishesByPopularity());

        text = "Cashier: " + user.getName() + ", Tax: " + restaurant.getTax() + "\n";
        text = text + "Product Name, Product Price:"+"\n";
        text = text + "--------------------------------------" + "\n";
        text_area.setText(text);

        types.add("All");
        types.add("Popularity");
        types.addAll(Dish.getUniqueTypes(dishList));

        sort_choiceBox.getItems().addAll(FXCollections.observableArrayList(types));

        sort_choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            List<Dish> filteredDishes;
            if ("All".equals(newValue)) {
                filteredDishes = dishList;
            } else if ("Popularity".equals(newValue)) {
                filteredDishes = dishListByPopularity;
            } else {
                filteredDishes = dishList.stream().filter(dish -> dish.getType().equals(newValue)).collect(Collectors.toList());
            }

            products.clear();
            menu_gridPane.getChildren().clear();

            int column = 0;
            int row = 1;
            for (int i = 0; i < filteredDishes.size(); i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml_files/fragments/menu-item.fxml"));
                VBox[] root = new VBox[1];
                try {
                    root[0] = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                MenuItemController controller = loader.getController();
                controller.setData(filteredDishes.get(i), i);

                products.add(root[0]);

                if (selectedDish.contains(filteredDishes.get(i))) {
                    root[0].getStyleClass().add("vbox_item_selected");
                }

                root[0].setOnMouseClicked(event -> {
                    if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                        root[0].getStyleClass().add("vbox_item_selected");
                    }

                    int id = Integer.parseInt(((Label) root[0].getChildren().get(3)).getText());
                    Dish selected = filteredDishes.get(id);
                    selectedDish.add(selected);

                    selectedDishCountMap.put(selected, selectedDishCountMap.getOrDefault(selected, 0) + 1);

                    updateTextArea();
                });

                root[0].setOnTouchPressed(event -> {
                    if (!root[0].getStyleClass().contains("vbox_item_selected")) {
                        root[0].getStyleClass().add("vbox_item_selected");
                    }

                    int id = Integer.parseInt(((Label) root[0].getChildren().get(3)).getText());
                    Dish selected = filteredDishes.get(id);
                    selectedDish.add(selected);

                    selectedDishCountMap.put(selected, selectedDishCountMap.getOrDefault(selected, 0) + 1);

                    updateTextArea();
                });

                if (column == 3) {
                    column = 0;
                    row++;
                }

                menu_gridPane.add(root[0], column++, row);
                GridPane.setMargin(root[0], new Insets(42.5));

                if (!this.islightMode) {
                    root[0].getChildren().forEach(x -> {
                        x.getStyleClass().remove("small_text_label_black");
                        x.getStyleClass().add("small_text_label_white");
                    });
                }
            }
        });

        sort_choiceBox.getSelectionModel().select(0);

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            scrollPane.getStyleClass().remove("myScrollPane_white");
            scrollPane.getStyleClass().add("myScrollPane_dark");

            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

            menuButton.getStyleClass().remove("active_buttonWhite");
            menuButton.getStyleClass().add("active_buttonDark");

            products.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_black");
                x.getStyleClass().add("small_text_label_white");
            }));

        }else {
            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

            scrollPane.getStyleClass().remove("myScrollPane_dark");
            scrollPane.getStyleClass().add("myScrollPane_white");

            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");

            menuButton.getStyleClass().remove("active_buttonDark");
            menuButton.getStyleClass().add("active_buttonWhite");

            products.forEach(vBox -> vBox.getChildren().forEach(x->{
                x.getStyleClass().remove("small_text_label_white");
                x.getStyleClass().add("small_text_label_black");
            }));
        }
        super.handleChangeColorMode();
    }

    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }

    @FXML
    public void handleMakeOrder(){
        selectedDishCountMap.entrySet().forEach(System.out::println);
    }

    @FXML
    public void handleClearLast() {
        if (!selectedDish.isEmpty()) {
            Dish removedDish = selectedDish.remove(selectedDish.size() - 1);

            int count = selectedDishCountMap.get(removedDish);
            if (count > 1) {
                selectedDishCountMap.put(removedDish, count - 1);
            } else {
                selectedDishCountMap.remove(removedDish);
            }

            products.stream()
                    .filter(product -> ((Label) product.getChildren().get(0)).getText().equals(removedDish.getName()))
                    .findFirst()
                    .ifPresent(product -> {
                        if (!selectedDishCountMap.containsKey(removedDish)) {
                            product.getStyleClass().remove("vbox_item_selected");
                        }
                    });

            updateTextArea();
        }
    }

    private void updateTextArea() {
        text = "Cashier: "+user.getName()+", Tax: "+restaurant.getTax()+"\n";
        text = text+ "Product Name, Product Price: "+"\n";
        text = text + "--------------------------------------"+"\n";
        for (Dish dish: selectedDish){
            text = text + dish.getName()+", "+dish.getPrice()+"\n";
            text = text + "--------------------------------------"+"\n";
        }
        text_area.setText(text);
    }

    private static List<Dish> sortDishesByPopularity(List<Dish> dishList, List<Dish> dishListByPopularity) {
        List<Integer> dishIdByPopularity = dishListByPopularity.stream()
                .map(Dish::getId)
                .collect(Collectors.toList());

        Comparator<Dish> popularityComparator = Comparator.comparingInt(dish -> dishIdByPopularity.indexOf(dish.getId()));
        return dishList.stream()
                .sorted(popularityComparator)
                .collect(Collectors.toList());
    }


}
