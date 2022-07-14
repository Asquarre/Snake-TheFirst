import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Snake extends BorderPane
{
    private Pane Fon;
    private Rectangle rectanglee;
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
        rect = new ArrayList<Rectangle>();
        Tra = new TranslateTransition();

        //making a head of a snake
        rectanglee =new Rectangle();
        rectanglee.setWidth(50);
        rectanglee.setHeight(50);
        rectanglee.setX(400);
        rectanglee.setY(400);
        rectanglee.setFill(Color.web("#562C2C"));


        Fon = new Pane();
        setCenter(Fon);

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

        Fon.getChildren().add(rectanglee);

        Fon.setStyle("-fx-background-color: #329F5B;");

        Apples();

        Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //using runLater, so all tasks are done in one thread
                //System.out.println(score);
                System.out.println(getHeight()+" " +getWidth());

                Platform.runLater(new Runnable() {
                    public void run() {
                        traceX.add((rectanglee.getBoundsInParent().getMinX()));
                        traceY.add((rectanglee.getBoundsInParent().getMinY()));
                        if(rectanglee.getBoundsInParent().getMaxX()>circle.getCenterX()-13 && rectanglee.getBoundsInParent().getMinX()<circle.getCenterX()+13)
                        {
                            if (rectanglee.getBoundsInParent().getMaxY()>circle.getCenterY()-13 && rectanglee.getBoundsInParent().getMinY()<circle.getCenterY()+13)
                            {
                                Fon.getChildren().remove(circle);
                                score++;
                                Apples();
                                tail(rectanglee.getBoundsInParent().getMaxX(), rectanglee.getBoundsInParent().getMaxY());



                            }
                        }
                        if (getWidth() == 0.0){
                            Width = 1000;
                            Height = 800;
                        }
                        else
                        {
                            Width = (int)getWidth();
                            Height = (int)getHeight();
                        }
                        if(rectanglee.getBoundsInParent().getMaxX() > Width+2 || rectanglee.getBoundsInParent().getMinX() < -2 || rectanglee.getBoundsInParent().getMaxY() > Height +2 || rectanglee.getBoundsInParent().getMinY() < -2)
                        {
                            try {
                                Thread.sleep(100000000);
                            }
                            catch (InterruptedException ex)
                            {

                            }

                        }

                        if (rect.isEmpty())
                        {
                            traceX.remove(0);
                            traceY.remove(0);
                        }
                        if(!rect.isEmpty()) {
                            for (int i = 0; i < rect.size(); i++) {
                                Rectangle Tail = new Rectangle();
                                Tail = rect.get(i);
                                //здесь находится самый главный код для хвоста
                                    Tail.setX(traceX.get(SecondThread - i * 12 ));
                                    Tail.setY(traceY.get(SecondThread - i * 12 ));
                            }
                            SecondThread++;
                        }

                        int Tailsize = rect.size();
                        if(!rect.isEmpty())
                        {
                            for (int i = 3; i < Tailsize; i++)
                            {
                                Rectangle Tail = new Rectangle();
                                Tail = rect.get(i);
                                double TailCenterX = Tail.getX()+25;// tail center coordinate X
                                double TailCenterY = Tail.getY()+25;// tail center coordinate X
                                if(rectanglee.getBoundsInParent().getMaxX()>TailCenterX-15 && rectanglee.getBoundsInParent().getMinX()<TailCenterX+15)
                                {
                                    if (rectanglee.getBoundsInParent().getMaxY()>TailCenterY-15 && rectanglee.getBoundsInParent().getMinY()<TailCenterY+15
                                    )
                                    {
                                        try {
                                            System.out.println(TailCenterX + " " + TailCenterY);
                                            System.out.println(rectanglee.getBoundsInParent().getMaxX() + " " +rectanglee.getBoundsInParent().getMinY());
                                            Thread.sleep(100000000);
                                        }
                                        catch (InterruptedException ex)
                                        {

                                        }




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
        axisX = false;
        axisY = true;
        TranslateTransition TraDo = new TranslateTransition();
        TraDo.setNode(rectanglee);
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
        axisX = true;
        axisY = false;
        TranslateTransition TraRi = new TranslateTransition();
        TraRi.setNode(rectanglee);
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
        axisX = false;
        axisY = true;
        TranslateTransition TraUp = new TranslateTransition();
        TraUp.setNode(rectanglee);
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
        axisX = true;
        axisY = false;
        TranslateTransition TraLe = new TranslateTransition();
        TraLe.setNode(rectanglee);
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


  public boolean AxisX()
  {
      return axisX;
  }

  public boolean AxisY()
  {
      return axisY;
  }

  public int TailEmpty()
  {
      if (rect.isEmpty())
          return 0;
      else
          return 1;
  }




}

