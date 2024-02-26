package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Dish;
import models.Order;
import models.Restaurant;
import models.User;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminPageChequeController extends AdminPageControllerAbstract{

    @FXML
    private Label sub_header;
    @FXML
    private Label sub_header2;

    @FXML private Label name_label;
    @FXML private Label address_label;
    @FXML private Label phone_label;
    @FXML private Label email_label;
    @FXML private Label info_label;

    @FXML private ChoiceBox name_box;
    @FXML private ChoiceBox address_box;
    @FXML private ChoiceBox phone_box;
    @FXML private ChoiceBox email_box;
    @FXML private ChoiceBox info_box;

    @FXML private ImageView cheque_view;
    private Label[] labels;
    private ChoiceBox[] boxes;

    @Override
    protected boolean shouldUseKeyboard() {
        return false;
    }

    public void initializeWithData(User user, boolean islightMode) {

        this.user = user;
        this.restaurant = Restaurant.retrieveRestaurant();

        String[] toPrint = restaurant.getToPrint().split(",");

        labels = new Label[]{name_label,address_label,phone_label,email_label,info_label};
        boxes = new ChoiceBox[]{name_box,address_box,phone_box,email_box,info_box};

        String[] boxValues = {"Yes","No"};
        for (ChoiceBox box : boxes) {
            box.getItems().addAll(boxValues);
            box.getSelectionModel().select("No");
        }

        for (String s : toPrint) {
            switch (s) {
                case "Name":
                    name_box.getSelectionModel().select("Yes");
                    break;
                case "Address":
                    address_box.getSelectionModel().select("Yes");
                    break;
                case "Phone":
                    phone_box.getSelectionModel().select("Yes");
                    break;
                case "Email":
                    email_box.getSelectionModel().select("Yes");
                    break;
                case "Additional info":
                    info_box.getSelectionModel().select("Yes");
                    break;
            }

        }

        loadPdf();

        if (!islightMode) {
            handleChangeColorMode();
        }
    }

    private void loadPdf(){
        Restaurant.retrieveToPrint(restaurant);

        String toPrintString = restaurant.getToPrint();
        String[] toPrint;
        if (!toPrintString.equals("")){
            toPrint = toPrintString.split(",");
        }else {
            toPrint = new String[0];
        }

        List<String> orderDetails = new ArrayList<>();
        if (Arrays.asList(toPrint).contains("Name")){
            orderDetails.add(restaurant.getName());
        }
        orderDetails.add("Cheque: ");
        orderDetails.add("--------------------");

        orderDetails.add("Dishes: ");
        orderDetails.add("Caesar Salad x 3");
        orderDetails.add("Price: 8.99");
        orderDetails.add("--------------------");

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        double sum = 26.97;
        orderDetails.add("Total: " + decimalFormat.format(sum));
        orderDetails.add("And");
        orderDetails.add("Tax: "+restaurant.getTax()+"%");
        orderDetails.add("--------------------");
        double sumTax = sum+((sum*restaurant.getTax())/100);
        orderDetails.add("To pay: " + decimalFormat.format(sumTax));
        orderDetails.add("With");
        orderDetails.add("Cash/Card Payment");
        if (toPrint.length != 0 && (Arrays.asList(toPrint).contains("Name") && toPrint.length != 1)){
            orderDetails.add("--------------------");
        }

        for (String s : toPrint) {
            switch (s) {
                case "Address":
                    List<String> tempAddress = Arrays.stream(restaurant.getAddress().split("\n")).toList();
                    for (String part: tempAddress){
                        orderDetails.add(part);
                    }
                    break;
                case "Phone":
                    orderDetails.add(restaurant.getPhone());
                    break;
                case "Email":
                    orderDetails.add(restaurant.getEmail());
                    break;
                case "Additional info":
                    List<String> tempInfo = Arrays.stream(restaurant.getInfo().split("\n")).toList();
                    for (String part: tempInfo){
                        orderDetails.add(part);
                    }
                    break;
            }

        }

        orderDetails.add("--------------------");
        orderDetails.add("Cashier: "+user.getName());
        orderDetails.add("--------------------");
        orderDetails.add("Order Id: "+"1");

        createPdf(orderDetails, Integer.parseInt(restaurant.getPaperSize().split("mm")[0]));
    }

    private void createPdf(List<String> orderDetails, int paperSize) {
        float lineHeight = 14;
        float paperWidthPoints = paperSize * 72f / 25.4f;  // Convert mm to points
        float AVERAGE_CHAR_WIDTH = 0.55f;
        float margin = 20;
        int fontSize = 10;

        float maxWidth = paperWidthPoints - 2 * margin;
        int maxSymbolsPerLine = (int) (maxWidth / (AVERAGE_CHAR_WIDTH * fontSize));

        orderDetails = splitStrings(orderDetails, maxSymbolsPerLine);

        float paperHeightPoints = orderDetails.size() * lineHeight + 1 * margin;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(paperWidthPoints, paperHeightPoints));
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                PDType0Font font = PDType0Font.load(document, AdminPageMenuController.class.getResourceAsStream("/styles/fonts/DejaVuSans.ttf"));
                contentStream.setFont(font, fontSize);

                float yStart = paperHeightPoints - margin;
                float yPosition = yStart;

                for (String line : orderDetails) {

                    float textWidth = font.getStringWidth(line) / 1000 * fontSize;
                    float xPosition = (paperWidthPoints - textWidth) / 2;

                    while (line.length() > maxSymbolsPerLine) {
                        String part = line.substring(0, maxSymbolsPerLine);
                        line = line.substring(maxSymbolsPerLine);

                        contentStream.beginText();
                        contentStream.newLineAtOffset(xPosition, yPosition);
                        contentStream.showText(part);
                        contentStream.endText();

                        yPosition -= lineHeight;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(xPosition, yPosition);
                    contentStream.showText(line);
                    contentStream.endText();
                    yPosition -= lineHeight;
                }
            }

            String directory = System.getProperty("user.home");
            String filePath = directory + File.separator + "order_example";

            document.save(filePath+".pdf");


        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PDDocument orderExample = Loader.loadPDF(new File(System.getProperty("user.home") + File.separator + "order_example.pdf"))) {
            PDFRenderer renderer = new PDFRenderer(orderExample);

            final float dpi = 350;
            BufferedImage bufferedImage = renderer.renderImageWithDPI(0, dpi);

            File path = new File(System.getProperty("user.home") + File.separator + "example_image.png");
            ImageIO.write(bufferedImage, "png", path);
            Image image = new Image(path.toURI().toString());
            cheque_view.setImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public  List<String> splitStrings(List<String> orderList, int maxSymbolsPerLine) {
        List<String> newList = new ArrayList<>();

        for (String string : orderList) {
            if (string.length() > maxSymbolsPerLine) {
                for (int i = 0; i < string.length(); i += maxSymbolsPerLine) {
                    int endIndex = Math.min(i + maxSymbolsPerLine, string.length());
                    String part = string.substring(i, endIndex);
                    newList.add(part);
                }
            } else {
                newList.add(string);
            }
        }

        return newList;
    }

    @Override
    protected void handleChangeColorMode() {
        if (islightMode){
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_black_medium");
                label.getStyleClass().add("text_label_white_medium");
            }

            chequeButton.getStyleClass().remove("active_buttonWhite");
            chequeButton.getStyleClass().add("active_buttonDark");

            sub_header.getStyleClass().remove("text_label_black");
            sub_header.getStyleClass().add("text_label_white");

            sub_header2.getStyleClass().remove("text_label_black");
            sub_header2.getStyleClass().add("text_label_white");

        }else {
            for (Label label : labels) {
                label.getStyleClass().remove("text_label_white_medium");
                label.getStyleClass().add("text_label_black_medium");
            }
            chequeButton.getStyleClass().remove("active_buttonDark");
            chequeButton.getStyleClass().add("active_buttonWhite");

            sub_header.getStyleClass().remove("text_label_white");
            sub_header.getStyleClass().add("text_label_black");

            sub_header2.getStyleClass().remove("text_label_white");
            sub_header2.getStyleClass().add("text_label_black");
        }
        super.handleChangeColorMode();
    }

    @FXML private void handleChangeChequeInfo(){
        StringBuilder newToPrint = new StringBuilder("");
        int count = 0;
        for (ChoiceBox box : boxes) {
            if (box.getSelectionModel().getSelectedItem().equals("Yes")){
                count++;
            }
        }

        if (name_box.getSelectionModel().getSelectedItem().equals("Yes")){
            count--;
            if (count <=0){
                newToPrint.append("Name");
            }else {
                newToPrint.append("Name,");
            }
        }
        if (address_box.getSelectionModel().getSelectedItem().equals("Yes")){
            count--;
            if (count <=0){
                newToPrint.append("Address");
            }else {
                newToPrint.append("Address,");
            }
        }
        if (phone_box.getSelectionModel().getSelectedItem().equals("Yes")){
            count--;
            if (count <=0){
                newToPrint.append("Phone");
            }else {
                newToPrint.append("Phone,");
            }

        }
        if (email_box.getSelectionModel().getSelectedItem().equals("Yes")){
            count--;
            if (count <=0){
                newToPrint.append("Email");
            }else {
                newToPrint.append("Email,");
            }

        }
        if (info_box.getSelectionModel().getSelectedItem().equals("Yes")){
            count--;
            if (count <=0){
                newToPrint.append("Additional info");
            }else {
                newToPrint.append("Additional info,");
            }
        }
        restaurant.setToPrint(newToPrint.toString());
        Restaurant.updateToPrint(restaurant);

        loadPdf();
    }

}
