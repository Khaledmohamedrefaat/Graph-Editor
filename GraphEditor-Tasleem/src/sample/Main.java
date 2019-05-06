package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class Main extends Application{
    Graph graph = new UndirectedGraph();
    static boolean[][] occupied = new boolean[5][5];
    static int xGap = 90, yGap = 60, xThreshold = 280, yThreshold = 190;
    static Vector<Label> nodes = new Vector<Label>();
    static Vector<Line> edges = new Vector<Line>();
    static Vector<Label> weights = new Vector<Label>();
    static Vector<Circle> rays = new Vector<Circle>();
    static Stage window;
    static Group menuLayout;
    static Label errorLabel = new Label("");

    public static void addGUIEdge(String node1, String node2, int w, Color color){
        System.out.println("This Is Here : " + w + " Thank You.");
        double x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        for(int i = 0; i < nodes.size(); ++i){
            if (nodes.get(i).getText().equals(node1)){
                x1 = nodes.get(i).getLayoutX();
                y1 = nodes.get(i).getLayoutY();
            }
            else if (nodes.get(i).getText().equals(node2)){
                x2 = nodes.get(i).getLayoutX();
                y2 = nodes.get(i).getLayoutY();
            }
        }
        double linex1, linex2, liney1, liney2;
        int r1 = 84/2, r2 = 84/2;
        if (node1.equals("M") || node1.equals("O") || node1.equals("W") || node1.equals("Q") || node1.equals("m"))
            r1 = 90/2;
        if (node2.equals("M") || node2.equals("O") || node2.equals("W") || node2.equals("Q") || node2.equals("m"))
            r2 = 90/2;
        double angleRad = Math.atan2((y2 - y1), (x2 - x1));
        Point2D result = new Point2D.Double();
        Point2D point = new Point2D.Double(x1 + r1 + r1, y1 + r1);
        AffineTransform rotation = new AffineTransform();
        rotation.rotate(angleRad, x1 + r1, y1 + r1);
        rotation.transform(point, result);
        linex1 = result.getX();
        liney1 = result.getY();

        result = new Point2D.Double();
        point = new Point2D.Double(x2 + r2 + r2, y2 + r2);
        rotation = new AffineTransform();
        rotation.rotate(angleRad + Math.PI, x2 + r2, y2 + r2);
        rotation.transform(point, result);
        linex2 = result.getX();
        liney2 = result.getY();

        Line line = new Line(linex1, liney1, linex2, liney2);

        Label weight = new Label(String.valueOf(w));
        weight.setLayoutX(linex1 + ((linex2-linex1)/2) - 10);
        weight.setLayoutY(liney1 + ((liney2-liney1)/2) - 45);
        weight.setStyle("-fx-font-size: 30px;");
        line.setStroke(color);
        weight.setTextFill(color);
        Circle circle = new Circle(line.getEndX(), line.getEndY(), 8, Color.RED);
        rays.add(circle);
        edges.add(line);
        weights.add(weight);

    }
    public static void addGUINode(int x, int y, String txt, Color color){
        Label lbl = new Label(txt);
        lbl.setLayoutX(x);
        lbl.setLayoutY(y);
        lbl.setTextFill(color);
        lbl.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, new CornerRadii(40), new BorderWidths(2, 2, 2, 2))));
        lbl.setPrefWidth(84);
        lbl.setPrefHeight(84);
        if (lbl.getText().equals("M") || lbl.getText().equals("O") || lbl.getText().equals("W") || lbl.getText().equals("Q") || lbl.getText().equals("m")){
            lbl.setPrefWidth(90);
            lbl.setPrefHeight(90);
        }

        lbl.setStyle( "-fx-font-size: 40px;" + "-fx-padding: 8 25 8 25;"  );
        nodes.add(lbl);
    }

    public static Pair getIndex(Label node){
        double x = node.getLayoutX();
        double y = node.getLayoutY();
        return new Pair( (y - yGap) / yThreshold, (x - xGap) / xThreshold);
    }
    public void setMenu(){
        menuLayout = new Group();
        int mergeMenuX = 0;
        // Graph Label
        Label graph_lbl = new Label("Graph");
        graph_lbl.setTextFill(Color.grayRgb(0, 0.7));
        graph_lbl.setStyle( "-fx-font-size: 90px; -fx-font-weight:bold");
        graph_lbl.setLayoutX(80 + mergeMenuX);
        graph_lbl.setLayoutY(70);
        // Editor Label
        Label editor_lbl = new Label("Editor");
        editor_lbl.setTextFill(Color.grayRgb(0, 0.5));
        editor_lbl.setStyle( "-fx-font-size: 90px; -fx-font-weight:bold");
        editor_lbl.setLayoutX(170+ mergeMenuX);
        editor_lbl.setLayoutY(150);
        int pushDown = 80;
        // Enter Vertex Label
        Label enter_vertex_lbl = new Label("Enter Vertex");
        enter_vertex_lbl.setLayoutX(30+ mergeMenuX);
        enter_vertex_lbl.setLayoutY(320 + pushDown);
        enter_vertex_lbl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // Add or Remove Vertex TextField
        TextField add_vertex_tf = new TextField();
        add_vertex_tf.setPrefWidth(35);
        add_vertex_tf.setLayoutX(180+ mergeMenuX);
        add_vertex_tf.setLayoutY(320+ pushDown);
        // Add Vertex Button
        Button addVertex_btn = new Button("Add");
        addVertex_btn.setPrefWidth(90);
        addVertex_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        addVertex_btn.setLayoutX(240+ mergeMenuX);
        addVertex_btn.setLayoutY(320+ pushDown);
        addVertex_btn.setOnAction(event -> {
            String[] names = new String[1];
            if(add_vertex_tf.getText().length() == 1) {
                names[0] = add_vertex_tf.getText();
                graph.addVertices(names);
                add_vertex_tf.clear();
            }else{
                if(add_vertex_tf.getText().equals("")) Main.errorLabel.setText("You must enter a name for the node");
                else Main.errorLabel.setText("The node must not exceed 1 letter");
                graph.draw();
            }
            add_vertex_tf.clear();
        });
        // Remove Vertex Button
        Button remVertex_btn = new Button("Remove");
        remVertex_btn.setPrefWidth(90);
        remVertex_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        remVertex_btn.setLayoutX(345+ mergeMenuX);
        remVertex_btn.setLayoutY(320+ pushDown);
        Line verticalLine = new Line(0, 380+ pushDown, 500, 380+ pushDown);
        remVertex_btn.setOnAction(event -> {
            String[] names = new String[1];
            if(add_vertex_tf.getText().length() == 1) {
                names[0] = add_vertex_tf.getText();
                graph.removeVertices(names);
                add_vertex_tf.clear();
            }else{
                if(add_vertex_tf.getText().equals("")) Main.errorLabel.setText("You must enter a name for the node");
                else Main.errorLabel.setText("The node must not exceed 1 letter");
                graph.draw();
            }
            add_vertex_tf.clear();
        });
        // From Vertex Label( Edges Panel )
        int x = 45;
        Label from_vertex_lbl = new Label("From");
        from_vertex_lbl.setLayoutX(30 + x+ mergeMenuX);
        from_vertex_lbl.setLayoutY(400+ pushDown);
        from_vertex_lbl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // From Vertex Textfield( Edges Panel )
        TextField fromVertex_tf = new TextField();
        fromVertex_tf.setPrefWidth(35);
        fromVertex_tf.setLayoutX(90+ x+ mergeMenuX);
        fromVertex_tf.setLayoutY(400+ pushDown);
        // To Vertex Label( Edges Panel )
        Label to_Vertex_lvl = new Label("To");
        to_Vertex_lvl.setLayoutX(140+ x+ mergeMenuX);
        to_Vertex_lvl.setLayoutY(400+ pushDown);
        to_Vertex_lvl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // To Vertex Textfield( Edges Panel )
        TextField toVertex_tf = new TextField();
        toVertex_tf.setPrefWidth(35);
        toVertex_tf.setLayoutX(180+ x+ mergeMenuX);
        toVertex_tf.setLayoutY(400+ pushDown);
        // Weight Vertex Label( Edges Panel )
        Label weight_Vertex_lbl = new Label("Weight");
        weight_Vertex_lbl.setLayoutX(240+ x+ mergeMenuX);
        weight_Vertex_lbl.setLayoutY(400+ pushDown);
        weight_Vertex_lbl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // Weight Vertex Textfield( Edges Panel )
        TextField weightVertex_tf = new TextField();
        weightVertex_tf.setPrefWidth(80);
        weightVertex_tf.setLayoutX(320+ x+ mergeMenuX);
        weightVertex_tf.setLayoutY(400+ pushDown);
        // Add Edge
        Button addEdge_btn = new Button("Add");
        addEdge_btn.setPrefWidth(90);
        addEdge_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        addEdge_btn.setLayoutX(75+ mergeMenuX);
        addEdge_btn.setLayoutY(450+ pushDown);
        addEdge_btn.setOnAction(event -> {
            String[] from = new String[1];
            String[] to = new String[1];
            from[0] = fromVertex_tf.getText();
            to[0] = toVertex_tf.getText();
            graph.addEdges(from, to);
            fromVertex_tf.clear();
            toVertex_tf.clear();
        });
        // Remove Edge
        Button remEdge_btn = new Button("Remove");
        remEdge_btn.setPrefWidth(90);
        remEdge_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        remEdge_btn.setLayoutX(175+ mergeMenuX);
        remEdge_btn.setLayoutY(450+ pushDown);
        remEdge_btn.setOnAction(event -> {
            String[] from = new String[1];
            String[] to = new String[1];
            from[0] = fromVertex_tf.getText();
            to[0] = toVertex_tf.getText();
            graph.removeEdges(from, to);
            fromVertex_tf.clear();
            toVertex_tf.clear();
        });
        // Edit Edge
        Button editEdge_btn = new Button("Edit Weight");
        editEdge_btn.setPrefWidth(120);
        editEdge_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        editEdge_btn.setLayoutX(280+ mergeMenuX);
        editEdge_btn.setLayoutY(450+ pushDown);
        editEdge_btn.setOnAction(event -> {
            String[] from = new String[1];
            String[] to = new String[1];
            String[] weights = new String[1];
            from[0] = fromVertex_tf.getText();
            to[0] = toVertex_tf.getText();
            weights[0] = weightVertex_tf.getText();
            graph.updateWeights(from, to, weights);
            graph.draw();
            fromVertex_tf.clear();
            toVertex_tf.clear();
            weightVertex_tf.clear();
        });
        Line verticalLine1 = new Line(0, 500+ pushDown, 500, 500+ pushDown);

        // From Vertex Label (Sum Panel)
        Label count_from_vertex_lbl = new Label("From");
        count_from_vertex_lbl.setLayoutX(30 + x+ mergeMenuX);
        count_from_vertex_lbl.setLayoutY(520+ pushDown);
        count_from_vertex_lbl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // From Vertex Textfield (Sum Panel)
        TextField count_fromVertex_tf = new TextField();
        count_fromVertex_tf.setPrefWidth(35);
        count_fromVertex_tf.setLayoutX(90+ x+ mergeMenuX);
        count_fromVertex_tf.setLayoutY(520+ pushDown);
        // To Vertex Label (Sum Panel)
        Label count_to_Vertex_lbl = new Label("To");
        count_to_Vertex_lbl.setLayoutX(140+ x+ mergeMenuX);
        count_to_Vertex_lbl.setLayoutY(520+ pushDown);
        count_to_Vertex_lbl.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        // To Vertex Textfield (Sum Panel)
        TextField count_toVertex_tf = new TextField();
        count_toVertex_tf.setPrefWidth(35);
        count_toVertex_tf.setLayoutX(180+ x+ mergeMenuX);
        count_toVertex_tf.setLayoutY(520+ pushDown);
        // Add To Sum Button (Sum Panel)
        Button sumEdge_btn = new Button("Add To Sum");
        sumEdge_btn.setPrefWidth(120);
        sumEdge_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        sumEdge_btn.setLayoutX(280+ mergeMenuX);
        sumEdge_btn.setLayoutY(520+ pushDown);
        // COUNTER Label
        Label counter_lbl = new Label("Count is : ");
        counter_lbl.setLayoutX(30 + x+ mergeMenuX);
        counter_lbl.setLayoutY(580+ pushDown);
        counter_lbl.setStyle("-fx-font-size: 20px; -fx-font-weight:bold");
        // Dynamic Counter Label
        Label cntr_lbl = new Label("0");
        cntr_lbl.setLayoutX(150 + x+ mergeMenuX);
        cntr_lbl.setLayoutY(580+ pushDown);
        cntr_lbl.setTextFill(Color.RED);
        cntr_lbl.setStyle("-fx-font-size: 20px; -fx-font-weight:bold");
        Line verticalLine2 = new Line(0, 625+ pushDown, 500, 625+ pushDown);

        sumEdge_btn.setOnAction(event -> {
            String[] from = new String[1];
            String[] to = new String[1];
            from[0] = count_fromVertex_tf.getText();
            to[0] = count_toVertex_tf.getText();
            int curr = Integer.parseInt(cntr_lbl.getText());
            curr += graph.sumWeights(from, to);
            cntr_lbl.setText(String.valueOf(curr));
            count_fromVertex_tf.clear();
            count_toVertex_tf.clear();
        });
        Button clear_cntr = new Button("Clear");
        clear_cntr.setPrefWidth(80);
        clear_cntr.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold");
        clear_cntr.setLayoutX(280+ mergeMenuX);
        clear_cntr.setLayoutY(580+ pushDown);
        clear_cntr.setOnAction(event -> {
            graph.clearSumPath();
            cntr_lbl.setText("0");
        });
        // Clear Graph Button
        Button clearGraph = new Button("Clear Graph");
        clearGraph.setPrefWidth(140);
        clearGraph.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold;");
        clearGraph.setLayoutX(175+ mergeMenuX);
        clearGraph.setLayoutY(650+ pushDown);
        clearGraph.setOnAction(event -> {
            graph.clearGraph();
        });

        Line verticalLine3 = new Line(0, 670+ pushDown, 500, 670+ pushDown);

        Button newDirected_btn = new Button("New Directed Graph");
        newDirected_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold;");
        newDirected_btn.setLayoutX(70 + mergeMenuX);
        newDirected_btn.setLayoutY(700 + pushDown);
        newDirected_btn.setOnAction(event -> {
            graph.clearGraph();
            graph = new DirectedGraph();
        });

        Button newUnDirected_btn = new Button("New Undirected Graph");
        newUnDirected_btn.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold;");
        newUnDirected_btn.setLayoutX(250 + mergeMenuX);
        newUnDirected_btn.setLayoutY(700 + pushDown);
        newUnDirected_btn.setOnAction(event -> {
            graph.clearGraph();
            graph = new UndirectedGraph();
        });

        Button completeGraph = new Button("Complete Graph");
        completeGraph.setPrefWidth(140);
        completeGraph.setStyle( "-fx-font-size: 15px; -fx-font-weight:bold;");
        completeGraph.setLayoutX(175+ mergeMenuX);
        completeGraph.setLayoutY(750+ pushDown);
        completeGraph.setOnAction(event -> {
            graph.completeGraph();
        });

        // Error Label
        errorLabel.setLayoutX(50);
        errorLabel.setLayoutY(315);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setStyle( "-fx-font-size: 20px; -fx-font-weight:bold");
        menuLayout.getChildren().addAll(
                newDirected_btn, newUnDirected_btn,
                clear_cntr, verticalLine, verticalLine1, verticalLine2, clearGraph,
                count_from_vertex_lbl, count_fromVertex_tf, count_to_Vertex_lbl, count_toVertex_tf, sumEdge_btn,
                counter_lbl, cntr_lbl, graph_lbl, editor_lbl, enter_vertex_lbl, add_vertex_tf, addVertex_btn, remVertex_btn,
                from_vertex_lbl, fromVertex_tf, to_Vertex_lvl, toVertex_tf, addEdge_btn, remEdge_btn, weight_Vertex_lbl,
                weightVertex_tf, editEdge_btn, errorLabel, completeGraph
        );
        menuLayout.setLayoutX(1400);
        menuLayout.setLayoutY(0);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Title");
        setMenu();

        graph.renderLayout();
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

