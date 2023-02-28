import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Snake extends BorderPane
{
    private Pane Fon;
    private Rectangle Head;
    private Rectangle tail;
    private Circle circle;
    private TranslateTransition TraRi;
    private TranslateTransition TraDo;
    private TranslateTransition TraLe;
    private TranslateTransition TraUp;
    private TranslateTransition Tra;
    private int score = 0;
    private ArrayList<Rectangle> rect;
    private ArrayList<Double> traceX = new ArrayList<Double>();
    private ArrayList<Double> traceY = new ArrayList<Double>();
    private int SecondThread = 0;
    private boolean axisX = false;
    private boolean axisY = false;
    private int Width;
    private int Height;

    public Snake()
    {
        //we need Arraylist of rectangles to store information about all peices of snakes tail
        rect = new ArrayList<Rectangle>();
        Tra = new TranslateTransition();

        //making a head of the snake
        Head =new Rectangle();
        Head.setWidth(50);
        Head.setHeight(50);
        Head.setX(400);
        Head.setY(400);
        Head.setFill(Color.web("#562C2C"));


        Fon = new Pane();
        setCenter(Fon);

        //making chess pattern for the background
        for (int i = 1; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                Rectangle backGround = new Rectangle();
                backGround.setWidth(50);
                backGround.setHeight(50);
                backGround.setFill(Color.web("#0C8346"));
                backGround.setX(i*50 -50);
                if(i%2 == 0)
                    backGround.setY(j*100+50);
                else
                    backGround.setY(j*100);
                Fon.getChildren().add(backGround);
            }
        }

        //adding head to the Pane
        Fon.getChildren().add(Head);

        Fon.setStyle("-fx-background-color: #329F5B;");

        //adding First apple to the field
        Apples();

        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //using runLater, so all tasks are done in one thread
                //without runLater code will not work because JavaFx doesn't work with multithreading
                Platform.runLater(new Runnable() {
                    public void run() {
                        traceX.add((Head.getBoundsInParent().getMinX()));
                        traceY.add((Head.getBoundsInParent().getMinY()));

                        //detecting if the head of the snake touched an apple
                        if(Head.getBoundsInParent().getMaxX()>circle.getCenterX()-13 && Head.getBoundsInParent().getMinX()<circle.getCenterX()+13)
                        {
                            if (Head.getBoundsInParent().getMaxY()>circle.getCenterY()-13 && Head.getBoundsInParent().getMinY()<circle.getCenterY()+13)
                            {
                                Fon.getChildren().remove(circle);
                                score++;
                                Apples();
                                tail(Head.getBoundsInParent().getMaxX(), Head.getBoundsInParent().getMaxY());



                            }
                        }

                        //the code below is needed for making an invisible borders for snake
                        //borders of a game can be changed just by changing the size of a window of the app

                        //when the code is just launched, the first seconds, it reads borders of a window as zero
                        //this piece of code is needed to prevent this problem
                        if (getWidth() == 0.0){
                            Width = 1000;
                            Height = 800;
                        }

                        else
                        {
                            //reading the width and length of a window
                            Width = (int)getWidth();
                            Height = (int)getHeight();
                        }

                        //if snakes head touches borders of a window the game ends
                        if(Head.getBoundsInParent().getMaxX() > Width+2 || Head.getBoundsInParent().getMinX() < -2 || Head.getBoundsInParent().getMaxY() > Height +2 || Head.getBoundsInParent().getMinY() < -2)
                        {
                                GameOver();

                                //setting the background color of a pane to red to indicate end of a game
                                Fon.setStyle("-fx-background-color: #AA4A44;");
                        }

                        //this piece of code deletes all recordings of a snakes trail, if a snake doesnt have a tail yet
                        if (rect.isEmpty())
                        {
                            traceX.remove(0);
                            traceY.remove(0);
                        }


                        // if snake has a tail, tail will follow the trace of head from the moment of creating a tail block
                        if(!rect.isEmpty()) {
                            for (int i = 0; i < rect.size(); i++) {
                                Rectangle Tail = new Rectangle();
                                Tail = rect.get(i);


                                //each new block of tail moves with bigger latency
                                    Tail.setX(traceX.get(SecondThread - i * 12 ));
                                    Tail.setY(traceY.get(SecondThread - i * 12 ));
                            }

                            //secondthread counts how many times does the the function run() repeated actions, from the moment snake got its tail
                            SecondThread++;
                        }


                        int Tailsize = rect.size();

                        //this function checks if snakes head touched the tail
                        if(!rect.isEmpty())
                        {
                            //for i loop starts from 3, in order to prevent sudden gameovers, because the first 3 or 4 peices of tail
                            //can touch the head
                            for (int i = 3; i < Tailsize; i++)
                            {
                                Rectangle Tail = new Rectangle();
                                Tail = rect.get(i);
                                double TailCenterX = Tail.getX()+25;// tail center coordinate X
                                double TailCenterY = Tail.getY()+25;// tail center coordinate X
                                if(Head.getBoundsInParent().getMaxX()>TailCenterX-15 && Head.getBoundsInParent().getMinX()<TailCenterX+15)
                                {
                                    if (Head.getBoundsInParent().getMaxY()>TailCenterY-15 && Head.getBoundsInParent().getMinY()<TailCenterY+15
                                    )
                                    {
                                            System.out.println(TailCenterX + " " + TailCenterY);
                                            System.out.println(Head.getBoundsInParent().getMaxX() + " " +Head.getBoundsInParent().getMinY());
                                            GameOver();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, 0, 8);



    }


    public void Apples()
    {
        //creating an apple and placing it on random coordinates
        //within the borders of the window
        circle = new Circle();
        Random random = new Random();
        double paneWidth = getWidth();
        double paneHeight = getHeight();
        int randomY;
        int randomX;
        if (paneHeight == 0.0) {
             randomY = random.nextInt(780 );
             randomX = random.nextInt(980);
        }
        else
        {
             randomY = random.nextInt((int) paneHeight -20);
             randomX = random.nextInt((int) paneWidth- 20);
        }

        circle.setRadius(13);
        circle.setCenterX(randomX+10);
        circle.setCenterY(randomY+10);
        circle.setFill(Color.RED);
        Fon.getChildren().add(circle);
    }

    public void goDown()
    {
        // writing on which axis does the snake moves
        axisX = false;
        axisY = true;
        TranslateTransition TraDo = new TranslateTransition();
        TraDo.setNode(Head);
        TraDo.setDuration(Duration.millis(0));
        //animation will be at a constant speed
        TraDo.setInterpolator(Interpolator.LINEAR);
        //this block is needed so the previous animation will stop
        TraDo.setByX(0.1);
        TraDo.setByX(-0.1);
        TraDo.setDuration(Duration.millis(5000));
        TraDo.setByY(1600 );
        TraDo.play();
    }

    public void goRight()
    {
        // writing on which axis does the snake moves
        axisX = true;
        axisY = false;
        TranslateTransition TraRi = new TranslateTransition();
        TraRi.setNode(Head);
        TraRi.setDuration(Duration.millis(0));
        //this block is needed so the previous animation will stop

        //animation will be at a constant speed
        TraRi.setInterpolator(Interpolator.LINEAR);
        TraRi.setByY(0.1);
        TraRi.setByY(-0.1);
        TraRi.setDuration(Duration.millis(5000));
        TraRi.setByX(1600);
        TraRi.play();
    }

    public void goUp()
    {
         // writing on which axis does the snake moves
        axisX = false;
        axisY = true;
        TranslateTransition TraUp = new TranslateTransition();
        TraUp.setNode(Head);
        TraUp.setDuration(Duration.millis(0));
        //animation will be at a constant speed
        TraUp.setInterpolator(Interpolator.LINEAR);
        //this block is needed so the previous animation will stop
        TraUp.setByX(0.1);
        TraUp.setByX(-0.1);
        TraUp.setDuration(Duration.millis(5000));
        TraUp.setByY(-1600);
        TraUp.play();
    }

    public void goLeft()
    {
        // writing on which axis does the snake moves
        axisX = true;
        axisY = false;

        TranslateTransition TraLe = new TranslateTransition();
        TraLe.setNode(Head);
        TraLe.setDuration(Duration.millis(0));
        //animation will be at a constant speed
        TraLe.setInterpolator(Interpolator.LINEAR);
        //this block is needed so the previous animation will stop
        TraLe.setByY(0.1);
        TraLe.setByY(-0.1);
        TraLe.setDuration(Duration.millis(5000));
        TraLe.setByX(-1600);
        TraLe.play();
    }

    // tail class which creates a tail for a snake
    public void tail(double x, double y)
    {
        tail = new Rectangle();
        tail.setWidth(50);
        tail.setHeight(50);
        tail.setX(x);
        tail.setY(y);
        tail.setFill(Color.web("#562C2C"));
        rect.add(tail);
        Fon.getChildren().add(tail);
        Tra.setNode(tail);
    }

//accessor for the AxisX
  public boolean AxisX()
  {
      return axisX;
  }
//accessor for the AxisY
  public boolean AxisY()
  {
      return axisY;
  }
//returns 0 if snake dosnt have a tail
// and 1 if it has a tail
  public int TailEmpty()
  {
      if (rect.isEmpty())
          return 0;
      else
          return 1;
  }

//GameOver class, which is clearing the pane, and shows "GameOver" message
//called when snake hit border or itself
public void GameOver()
{
    Fon.getChildren().clear();
    Text gameover = new Text("Game Over");
    gameover.setFill(Color.WHITE);
    gameover.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 60));
    Fon.getChildren().add(gameover);
    this.setStyle("-fx-background-color: #AA4A44;");
    setCenter(gameover);
}



}

