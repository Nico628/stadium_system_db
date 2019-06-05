import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUIFan extends Application implements EventHandler<ActionEvent>{

    Button eventButton;
    Button storeButton;
    Button parkingButton;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("The Best Stadium");

        eventButton = new Button();
        storeButton = new Button();
        parkingButton = new Button();

        eventButton.setText("Events");
        storeButton.setText("Store");
        parkingButton.setText("Parking");

        eventButton.setOnAction(this);

        StackPane layout = new StackPane();
        layout.getChildren().add(eventButton);

        //layout.getChildren().add(storeButton);

        //layout.getChildren().add(parkingButton);



        Scene scene = new Scene(layout, 400, 300);

        //storeButton.setAlignment(Pos.TOP_CENTER);
       // parkingButton.setAlignment(Pos.TOP_RIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    @Override

    public void handle(ActionEvent event){
        if(event.getSource() == eventButton){
            // temporary
            System.out.println("EVENTS");
        }else if(event.getSource() == storeButton){
            //temporary
            System.out.println("STORE");
        }else if(event.getSource() == parkingButton){
            //temporary
            System.out.println("PARKING");
        }
    }

}
