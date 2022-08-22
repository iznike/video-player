import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LolomoRow extends VBox{
    
    Label lblTitle;
    HBox nodeList;

    public LolomoRow() {

        //Row title node
        lblTitle = new Label();
        lblTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Paint.valueOf("white"));
        lblTitle.setPadding(new Insets(0, 0, 0, 40));
        
        //Other row nodes
        nodeList = new HBox(10);
        nodeList.setStyle("-fx-background-color: #141414");
        ScrollPane scrollPane = new ScrollPane(nodeList);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent");

        Button btnLeft = new Button(Icon.CHEVRON_LEFT);
        btnLeft.setFont(Icon.fontAwesome18);
        // btnLeft.setTextFill(Paint.valueOf("white"));
        btnLeft.prefHeightProperty().bind(nodeList.heightProperty());
        // btnLeft.setCursor(Cursor.HAND);
        btnLeft.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");
        
        Button btnRight = new Button(Icon.CHEVRON_RIGHT);
        btnRight.setFont(Icon.fontAwesome18);
        // btnRight.setTextFill(Paint.valueOf("white"));
        btnRight.prefHeightProperty().bind(nodeList.heightProperty());
        // btnRight.setCursor(Cursor.HAND);
        btnRight.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand;");

        HBox row = new HBox(btnLeft, scrollPane, btnRight);

        getChildren().addAll(lblTitle, row);
        setSpacing(10);

        btnLeft.setOnAction((e) -> {
            scrollPane.setHvalue(scrollPane.getHvalue() - 0.5);
        });

        btnRight.setOnAction((e) -> {
            scrollPane.setHvalue(scrollPane.getHvalue() + 0.5);
        });

    }

    public LolomoRow(String title, Node... content) {
        
        this();

        setTitle(title);
        nodeList.getChildren().addAll(content);
    }

    public LolomoRow(String title) {

        this();
        setTitle(title);
    }

    // @Override
    // public ObservableList<Node> getChildren() {
    //     return nodeList.getChildren();
    // }

    public void addChildren(Node... content) {
        nodeList.getChildren().addAll(content);
    }

    public String getTitle() {
        return lblTitle.getText();
    }

    public void setTitle(String title) {
        lblTitle.setText(title);
    }

}
