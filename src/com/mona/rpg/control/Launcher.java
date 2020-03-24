package com.mona.rpg.control;


import com.mona.rpg.model.Role;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class Launcher extends Application {
    public static final double cos = Math.cos(Math.toRadians(30));
    private Scene mScene;
    private Group mGroup;
    private GraphicsContext mGraphics;

    public static void main(String[] args) {
//        ControllerImpl.getInstance().start();
        Application.launch(com.mona.rpg.control.Launcher.class, args);
    }

    int d = 1;

    @Override
    public void stop() {
        System.out.println("启动器 停止");
    }

    /**
     * zlib解压+base64
     */
    public static String decompressData(String encdata) {
        String result = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InflaterOutputStream zos = new InflaterOutputStream(bos);
            zos.write(Base64.getDecoder().decode(encdata.getBytes(StandardCharsets.UTF_8)));
            zos.close();
            result = bos.toString("UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * zlib压缩+base64
     */
    public static String compressData(String data) {
        ByteArrayOutputStream bos;
        DeflaterOutputStream zos;
        try {
            bos = new ByteArrayOutputStream();
            zos = new DeflaterOutputStream(bos);
            zos.write(data.getBytes());
            zos.close();
            return new String(Base64.getEncoder().encode(bos.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public void init() {
        System.out.println("启动器 初始化");
        Canvas mCanvas = new Canvas(800, 600);
        mGroup = new Group(mCanvas);
        mScene = new Scene(mGroup, -1, -1, true, SceneAntialiasing.BALANCED);
        mGraphics = mCanvas.getGraphicsContext2D();
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("启动器 开始");
        mGroup.setAutoSizeChildren(true);
        mGroup.setDepthTest(DepthTest.ENABLE);

        mGraphics.setFill(Color.GRAY);
        mGraphics.fillRect(0, 0, 1000, 1000);

        primaryStage.setScene(mScene);
        primaryStage.setTitle("World Battle");
        primaryStage.show();

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

        Image image = new Image("file:///../res/方向指示器.png", false);
        Role role = new Role(new Image("file:///../res/role-1.png", false));
        mScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            mGraphics.setFill(Color.GRAY);
            mGraphics.fillRect(0, 0, 1000, 1000);
            role.onDraw(mGraphics);
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                mGraphics.setFill(Color.GRAY);
                mGraphics.fillRect(0, 0, 1000, 1000);
                role.onDraw(mGraphics);
                mGraphics.save();
                mGraphics.translate(200, 200);
                mGraphics.scale(0.5, 0.25);
                mGraphics.rotate(d++);
                mGraphics.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
//            mGraphics.scale(2, 4);
//            mGraphics.translate(-200, -200);
                mGraphics.restore();
            }
        }.start();

    }
}
