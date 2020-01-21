package sample;

import com.mona.bean.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Test
    public void test() {
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone.getDisplayName());
        System.out.println(Calendar.getInstance());
        Graph<String, Integer> graph = new Graph<>();
        graph.link("a", "b", 2);
        graph.link("b", "a", 9);
        graph.link("a", "c", 11);
        graph.link("c", "a", 12);
        graph.link("b", "c", 5);
        graph.link("a", "d", 7);
        graph.createVertex("e");
        graph.createVertex("f");
        graph.link("e", "f", 3);
        graph.link("f", "e", 3);
        graph.createVertex("g");
        graph.link("a", "f", 17);
        graph.createVertex("g");
        graph.link("g", "g", 0);
        graph.createVertex("g");
        System.out.println(graph);
        graph.deleteVertex("a");
        System.out.println(graph);
    }

    @Test
    public void nio() throws IOException {
        WritableByteChannel writableByteChannel = Channels.newChannel(System.out);
        ByteBuffer src = ByteBuffer.allocate(1024);
        src.put("hello world".getBytes());
        for (int i = 0; i < 256; i++) {
            src.put((byte) i);
        }
        src.flip();
        writableByteChannel.write(src);
        src.clear();
        Logger logger = Logger.getGlobal();
        logger.log(Level.INFO, "hello world");
    }

    @Test
    public void t1() {
        System.out.println(Integer.parseInt("78", 16));
    }
}
