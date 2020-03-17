package com.mona.rpg.control;


import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Launcher extends Application {
    public static final double cos = Math.cos(Math.toRadians(30));
    private Scene mScene;
    private Group mGroup;
    private Canvas mCanvas;
    private GraphicsContext mGraphics;
    private Node node;

    public static void main(String[] args) {
//        ControllerImpl.getInstance().start();
        System.out.printf("%1.5f\n", 3.1415926);
        Application.launch(com.mona.rpg.control.Launcher.class, args);
    }

    @Override
    public void init() {
        System.out.println("启动器 初始化");
        mCanvas = new Canvas(800, 600);
        mGroup = new Group(mCanvas);
        mScene = new Scene(mGroup, -1, -1, true, SceneAntialiasing.BALANCED);
        mGraphics = mCanvas.getGraphicsContext2D();

        node = new Box(100, 80, 30);
        node.setDepthTest(DepthTest.ENABLE);
    }

    @Override
    public void stop() {
        System.out.println("启动器 停止");
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("启动器 开始");
        mGroup.setAutoSizeChildren(true);
        mGroup.setDepthTest(DepthTest.ENABLE);

        mGraphics.setFill(Color.BLUE);
        mGraphics.fillRect(0, 0, 1000, 1000);
        mGroup.getChildren().add(node);

        TranslateTransition translate =
                new TranslateTransition(Duration.millis(1500));
        translate.setToX(450);
        translate.setToY(400);
        translate.setToZ(-120);

        FillTransition fill = new FillTransition(Duration.millis(1500));
        fill.setFromValue(Color.BLACK);
        fill.setToValue(Color.BLUE);
        fill.setShape(new Circle(100, 100, 100));

        RotateTransition rotate = new RotateTransition(Duration.millis(1500));
        rotate.setToAngle(600);

        ScaleTransition scale = new ScaleTransition(Duration.millis(1500));
        scale.setToX(0.5);
        scale.setToY(0.5);

        ParallelTransition transition = new ParallelTransition(node,
                translate, fill, rotate, scale);
        transition.setCycleCount(Timeline.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();

        Image image = new Image("file:///../res/tile.png", false);
        System.out.println(image.getProgress());


        mScene.setOnKeyPressed(event -> {
//            System.out.println(":"+event.getText());
        });
        mScene.setOnMouseClicked(event -> {
//            System.out.println(":"+event.toString());
        });

        mScene.addEventHandler(EventType.ROOT, event -> {
//            System.out.print("scene:");
//            System.out.println(event);
        });
        primaryStage.addEventHandler(EventType.ROOT, event -> {
//            System.out.print("stage:");
//            System.out.println(event);
        });

        primaryStage.setScene(mScene);
        primaryStage.setTitle("World Battle");
        primaryStage.show();

        System.out.println(image.getProgress());
        mGraphics.drawImage(image, 0, 0);

        mGraphics.setStroke(Color.YELLOW);
        int x = 0;
        int y = 0;
        int r = 32;
        for (int i = 0; i < 99; i++) {
            if ((i & 1) == 1) {
                //奇数
                drawHex(x + r * cos * 2 * i + r * cos, y + r * 1.5 * i, r);
            } else {
                //偶数
                drawHex(x + r * cos * 2 * i, y + r * 1.5 * 0, r);
            }
//            drawHex(x+r*cos*2*i+r*cos*0,y+r*1.5*0,r);
//            drawHex(x+r*cos*2*i+r*cos,y+r*1.5*1,r);
//            drawHex(x+r*cos*2*i+r*cos*0,y+r*1.5*2,r);
//            drawHex(x+r*cos*2*i+r*cos,y+r*1.5*3,r);
//            drawHex(x+r*cos*2*i+r*cos*0,y+r*1.5*4,r);
        }
    }

    private void drawHex(double x, double y, double r) {
        double rCos = r * cos;
        mGraphics.strokeLine(x, y - r, x + rCos, y - r * 0.5);
        mGraphics.strokeLine(x + rCos, y - r * 0.5, x + rCos, y + r * 0.5);
        mGraphics.strokeLine(x + rCos, y + r * 0.5, x, y + r);
        mGraphics.strokeLine(x, y + r, x - rCos, y + r * 0.5);
        mGraphics.strokeLine(x - rCos, y + r * 0.5, x - rCos, y - r * 0.5);
        mGraphics.strokeLine(x - rCos, y - r * 0.5, x, y - r);
    }
}
