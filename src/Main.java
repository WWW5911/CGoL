import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import execute.source;
import execute.cell;
public class Main extends Application {
    static int size = 15;
    static Canvas canvas;
    static source sour;
    static boolean flag = false, autof = false, wipef = false;
    static GraphicsContext gc;
    static double dif = 0;
    static Timer timer;
    static TimerTask tt;
    static double mouseX, mouseY, lastX, oriW, oriH;
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Conway-s-Game-of-Life by Astria");
        canvas = new Canvas(1024, 720);
        canvas.relocate(0, 52);
        oriW = 1024;
        oriH = 720;
        Pane root = new Pane();
        Canvas head = new Canvas(1024, 52);
        GraphicsContext gch = head.getGraphicsContext2D();
        gch.setFill(Color.web("#F4F4F4"));
        gch.fillRect(0, 0, 1024, 52);
        root.getChildren().add(canvas);
        root.getChildren().add(head);
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
            if (check(seed.getText(), sizet.getText())) {
                printArea();
                if (!flag) {
                    size = Integer.parseInt(sizet.getText());
                    dif = canvas.getWidth() / size;
                    // dif = canvas.getWidth()-(canvas.getHeight()-52);
                    printArea();
                    sour = new source(size, Integer.parseInt(seed.getText()));
                    flag = true;
                    seed.setDisable(true);
                    sizet.setDisable(true);
                } else if(!autof) source.execute();
                for (int i = 0; i < sour.getaliveCount(); ++i) {
                    printcell(i);
                }
                
            }
        });

        
        Button AutoB = new Button("Auto");
        AutoB.relocate(350, 25);
        root.getChildren().add(AutoB);
        AutoB.setOnAction(x -> {
            if(check(seed.getText(), sizet.getText(), speed.getText()) ){
            if(!flag){
                size = Integer.parseInt(sizet.getText());
                dif = canvas.getWidth()/size;
                //  dif = canvas.getWidth()-(canvas.getHeight()-52);
                printArea();
                sour = new source(size, Integer.parseInt(seed.getText()));
                for(int i = 0; i < sour.getaliveCount(); ++i){
                   printcell(i);
                }
                flag = true;
                seed.setDisable(true);
                sizet.setDisable(true);
                speed.setDisable(true);
            }else if(!speed.isDisabled()) speed.setDisable(true);
            if(!autof){
                createTask();
                int temp = Integer.parseInt(speed.getText());
                autof = true;
                timer = new Timer();
                if(temp == 0)
                    timer.schedule(tt, 2000, 2000);
                else timer.schedule(tt, 1000/temp, 1000/temp);
            }else {
                autof = false;
                timer.cancel();
                tt = null;
                speed.setDisable(false);
            }
            }
            
        });

        Button RestartB = new Button("Clear");
        RestartB.relocate(400, 25);
        root.getChildren().add(RestartB);
        RestartB.setOnAction(x->{
            flag = false;
            autof = false;
            if(timer != null)
                timer.cancel();
            tt = null;
            seed.setDisable(false);
            sizet.setDisable(false);
            speed.setDisable(false);
            gc.setFill(Color.web("#F4F4F4"));
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        });

        lastX = 0;
        canvas.setOnMouseDragged(x ->{
            if(!flag){
                mouseX = x.getX();
                mouseY = x.getY();
                flag = true;
            } 
            if(!wipef) wipef = true;
            else wipef = false;
          //  if((lastX+100 > 100 && x.getX()-mouseX+100 > 100) || (lastX+100 < 100 && x.getX()-mouseX+100 < 100))
            if(!wipef)
                canvas.relocate(canvas.getLayoutX()+x.getX()-mouseX, canvas.getLayoutY()+x.getY()-mouseY);
            lastX = x.getX()-mouseX;
            
            mouseX = x.getX();
            mouseY = x.getY();
            
        });
        canvas.setOnMouseReleased(x -> flag = false);
        
        canvas.setOnScroll(x ->{
            double scale = 1.1, nowx = canvas.getWidth(), nowy = canvas.getHeight();
            if(x.getDeltaY()>0 && nowx*1.1 < oriW*9 && nowy*1.1 < oriH*9){
                canvas.setHeight(canvas.getHeight()*scale);
                canvas.setWidth(canvas.getWidth()*scale);
                canvas.relocate(canvas.getLayoutX()+(x.getX())/canvas.getWidth()*canvas.getWidth()*(1-scale), 
                    canvas.getLayoutY()+(x.getY())/canvas.getHeight()*canvas.getHeight()*(1-scale));
                
            }
            if(x.getDeltaY()<0 && nowx/1.1 > oriW/10 && nowy/1.1 > oriH/10){
                canvas.setHeight(canvas.getHeight()/scale);
                canvas.setWidth(canvas.getWidth()/scale);
                canvas.relocate(canvas.getLayoutX()-(x.getX())/canvas.getWidth()*canvas.getWidth()*(1-scale), 
                    canvas.getLayoutY()-(x.getY())/canvas.getHeight()*canvas.getHeight()*(1-scale));
            }
            printArea();
            for (int i = 0; i < sour.getaliveCount(); ++i) {
                printcell(i);
            }
        });
        
    }
    static boolean check(String a, String b){
        return a.matches("[0-9]+") && b.matches("[0-9]+");
    }
    static boolean check(String a, String b, String c){
        return a.matches("[0-9]+") && b.matches("[0-9]+") && c.matches("[0-9]+");
    }
    static void createTask(){
        tt = new TimerTask(){
            @Override
            public void run() {
                if(autof){
                    source.execute();
                    printArea();
                    for(int i = 0; i < sour.getaliveCount(); ++i){
                       printcell(i);
                    }
                }

            }
            
        };
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
       // double down = 0;
        for(double i = 0; i <= h*size+1; i += h){
            gc.strokeLine(dif/2, i, last, i);
      //      down = i;
        }
     //   gc.setFill(Color.AQUA);
     //   gc.fillRect(0, down, canvas.getWidth(), canvas.getHeight()/size);
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
