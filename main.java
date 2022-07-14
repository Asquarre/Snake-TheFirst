import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class main extends Application
{
    Snake Pane = new Snake();
    Scene scene = new Scene(Pane,1000,800);
    int PrefHeight = 800;
    int PrefWidth = 1000;
    public void start(Stage mainS) throws Exception
    {
        Pane.setPrefSize(PrefWidth,PrefHeight);




        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event)
            {
                switch(event.getCode())
                {
                    case S:
                        if(Pane.AxisY() == false || Pane.TailEmpty() == 0)
                            Pane.goDown();
                        break;
                    case D:
                        if(Pane.AxisX() == false|| Pane.TailEmpty() == 0)
                            Pane.goRight();
                        break;
                    case W:
                        if(Pane.AxisY() == false|| Pane.TailEmpty() == 0)
                            Pane.goUp();
                        break;
                    case A:
                        if(Pane.AxisX() == false|| Pane.TailEmpty() == 0)
                            Pane.goLeft();
                        break;

                }
            }
        });

        mainS.setScene(scene);
        mainS.setTitle("Snake");
        mainS.show();

    }
    public double getHeight()
    {
        return Pane.getHeight() +10;
    }

    public double getWidth()
    {

        return Pane.getWidth() +10;
    }
}


