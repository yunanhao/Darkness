package com.mona.rpg.control;


import com.mona.common.container.map.Graph;
import com.mona.common.util.Astar;
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
import javafx.scene.image.PixelReader;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class Launcher extends Application {
    public static final double cos = Math.cos(Math.toRadians(30));
    private Scene mScene;
    private Group mGroup;
    private GraphicsContext mGraphics;
    List<Astar.Node> nodeList;
    private int[][] mapArray;
    private int[][] my_map_array;
    private Image mapImage;
    private Image newMap;

    public static void main(String[] args) {
//        ControllerImpl.getInstance().start();
        Application.launch(com.mona.rpg.control.Launcher.class, args);
    }

    private int scale = 7;

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
    public void stop() {

    }

    @Override
    public void init() throws FileNotFoundException {
        Canvas mCanvas = new Canvas(2560, 1440);
        mGroup = new Group(mCanvas);
        mScene = new Scene(mGroup, 1344, 896, true, SceneAntialiasing.BALANCED);
        mGraphics = mCanvas.getGraphicsContext2D();
        mapImage = new Image("file:///../res/foreground.png");
        Scanner scanner = new Scanner(new File("file:///../res/foreground.csv"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(",");
            mapArray = new int[40][];
            mapArray[0] = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                mapArray[0][i] = Integer.parseInt(split[i]);
            }
        }
        newMap = new Image("file:///../res/80 by 50 orthogonal maze.png");
        int width = (int) newMap.getWidth() / 80;
        int height = (int) newMap.getHeight() / 50;
        int base_x, base_y;
        PixelReader pixelReader = newMap.getPixelReader();
        Graph<String, Integer> graph = new Graph<>();
        my_map_array = new int[50][80];
        int value = 0;
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 80; x++) {
                base_x = x * width;
                base_y = y * height;
                int left = pixelReader.getArgb(1 + base_x, 1 + (height >> 1) + base_y);
                int top = pixelReader.getArgb(1 + (width >> 1) + base_x, 1 + base_y);
                int right = pixelReader.getArgb(1 + width + base_x, 1 + (height >> 1) + base_y);
                int down = pixelReader.getArgb(1 + (width >> 1) + base_x, 1 + height + base_y);
                graph.createVertex("(" + x + "," + y + ")");
                if (left == 0xFFFFFFFF) {
                    value |= 0b1000;
                    graph.link("(" + (x - 1) + "," + y + ")", "(" + x + "," + y + ")", 1);
                }
                if (top == 0xFFFFFFFF) {
                    value |= 0b0100;
                    graph.link("(" + x + "," + (y - 1) + ")", "(" + x + "," + y + ")", 1);
                }
                if (right == 0xFFFFFFFF) {
                    value |= 0b0010;
                    graph.link("(" + x + "," + y + ")", "(" + (x + 1) + "," + y + ")", 1);
                }
                if (down == 0xFFFFFFFF) {
                    value |= 0b0001;
                    graph.link("(" + x + "," + y + ")", "(" + x + "," + (y + 1) + ")", 1);
                }
                my_map_array[y][x] = value;
                value = 0;
//                System.out.print(Integer.toHexString(left) + Integer.toHexString(top) + Integer.toHexString(right) + Integer.toHexString(down) + "...");
            }
//            System.out.println();
        }

//        System.out.println(graph);
        for (int y = 0; y < 50; y++) {
            for (int x = 0; x < 80; x++) {
                System.out.print(my_map_array[y][x] + ",");
            }
            System.out.println();
        }
    }

    @Override
    public void start(Stage primaryStage) {
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

        Role role = new Role(new Image("file:///../res/role-1.png", false));
        role.moveTo(100, 100);

        mScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaY() > 0) scale++;
            else scale--;
            if (scale <= 0) scale = 1;
        });

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                mGraphics.setFill(Color.WHITE);
                mGraphics.fillRect(0, 0, mScene.getWidth(), mScene.getHeight());
//                mGraphics.drawImage(mapImage, 0, 0, mapImage.getWidth() * scale / 10, mapImage.getHeight() * scale / 10);
                mGraphics.drawImage(newMap, 0, 0, newMap.getWidth() * scale / 10, newMap.getHeight() * scale / 10);
                role.onDraw(mGraphics);
                mGraphics.setFill(Color.GRAY);
                if (nodeList != null)
                    for (Astar.Node n : nodeList) {
                        mGraphics.fillRect(n.index_x * 16, n.index_y * 16, 16, 16);
                    }
            }
        }.start();

        new Thread(() -> {
            nodeList = new ArrayList<>();
            Astar astar = new Astar(my_map_array);
            astar.setSearchListener(node -> {
                nodeList.add(node);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            astar.setOrigin(39, 0);
            astar.setTarget(40, 49);
            astar.find();
        }).start();

        Result result = JUnitCore.runClasses(Astar.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
}
