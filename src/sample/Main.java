package sample;

import com.mona.bean.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;

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
    }
}
