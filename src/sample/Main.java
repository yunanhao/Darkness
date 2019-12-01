package sample;

import com.mona.bean.Graph;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
//        launch(args);
        Graph<String, Integer> graph = new Graph<>();
        graph.link("a", "b", 2);
        graph.link("b", "c", 5);
        graph.link("a", "d", 7);
        graph.createVertex("e");
        System.out.println(graph.size());
        System.out.println(graph);
    }
}
