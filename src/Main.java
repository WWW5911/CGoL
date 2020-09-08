import java.util.ArrayList;
import javax.sound.sampled.Line;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import execute.source;
import execute.cell;
public class Main extends Application {
    static int size = 15;
    static Canvas canvas;
    static source sour;
    static boolean flag = false;
    static GraphicsContext gc;
    static double dif = 0;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        canvas = new Canvas(1024, 720);
        canvas.relocate(0, 52);
        Pane root = new Pane();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root, 1024, 720));
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        Label seedl = new Label("Seed:"), sizel = new Label("Size:"), speedl = new Label("Times in a second:");
        TextField seed = new TextField(), sizet = new TextField(), speed = new TextField();
        HBox seedh = new HBox(), sizeh = new HBox(), speedh = new HBox();
        seedh.getChildren().addAll(seedl, seed);
        seedh.setSpacing(10);
        sizeh.getChildren().addAll(sizel, sizet);
        sizeh.setSpacing(10);
        speedh.getChildren().addAll(speedl, speed);
        speedh.setSpacing(10);

        sizeh.relocate(210, 0);
        speedh.relocate(0, 25);


        root.getChildren().add(seedh);
        root.getChildren().add(sizeh);
        root.getChildren().add(speedh);

        Button NextB = new Button("Next");
        NextB.relocate(300, 25);
        root.getChildren().add(NextB);
        
        NextB.setOnAction(x -> {
            printArea();
            if(!flag){
                size = Integer.parseInt(sizet.getText());
                dif = canvas.getWidth()/size;
                //  dif = canvas.getWidth()-(canvas.getHeight()-52);
                printArea();
                sour = new source(size, Integer.parseInt(seed.getText()));
                flag = true;
            }else source.execute();
            for(int i = 0; i < sour.getaliveCount(); ++i){
                printcell(i);
            }
            
        });

    }


    static void printArea(){
        double last = 0;
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(Color.BLACK);
        double w = (canvas.getWidth()-dif)/size, h = (canvas.getHeight()-52)/size;
        for(double i = dif/2; i <= w*size+dif/2+1; i+=w){
            gc.strokeLine(i, 0, i, canvas.getHeight()-52);
            last = i;
        }
        double down = 0;
        for(double i = 0; i < h*size; i += h){
            gc.strokeLine(dif/2, i, last, i);
            down = i;
        }
        gc.setFill(Color.AQUA);
        gc.fillRect(0, down, canvas.getWidth(), canvas.getHeight()/size);
    }
    static void printcell(int index){
        cell cel = sour.getCell(index);
        int x = cel.x, y = cel.y;
        gc.setFill(Color.BLACK);
        double w = (canvas.getWidth()-dif)/size, h = (canvas.getHeight()-52)/size;
        gc.fillRect(x*w+dif/2, y*h, w, h);
    }
    public static void main(String[] args) {
    launch(args);
    
    }
    
}
