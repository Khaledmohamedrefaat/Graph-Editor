package GraphEditor;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

import java.util.HashMap;

class UndirectedGraph extends Graph{
    private HashMap<Pair, Boolean> visitedEdge = new HashMap<>();
    public String[] addEdges(String from[], String to[]){
        String ret[] = new String[from.length];
        for (int i = 0; i < from.length; ++i){
            if (index.get(from[i]) == null|| index.get(to[i]) == null){
                ret[i] = "One of the nodes are not found";
                Main.errorLabel.setText(ret[i]);
            }
            else if (values.get(index.get(from[i])).equals("-1") || values.get(index.get(to[i])).equals("-1")){
                ret[i] = "One of the nodes are not found";
                Main.errorLabel.setText(ret[i]);
            }
            else{
                graphRep[index.get(from[i])][index.get(to[i])] = 1;
                visitedEdge.put(new Pair(index.get(from[i]), index.get(to[i])), true);
                //graphRep[index.get(to[i])][index.get(from[i])] = 1;
                ret[i] = "Edge Added Successfully";
                Main.errorLabel.setText("");
            }
        }
        draw();
        return ret;
    }
    public String[] removeEdges(String from[], String to[]){
        String ret[] = new String[from.length];
        for (int i = 0; i < from.length; ++i){
            if (!values.containsValue(from[i])|| !values.containsValue(to[i])) {
                ret[i] = "One of the nodes are not found";
                Main.errorLabel.setText(ret[i]);
            }
            else{
                graphRep[index.get(from[i])][index.get(to[i])] = 0;
                graphRep[index.get(to[i])][index.get(from[i])] = 0;
                ret[i] = "Edge Removed Successfully";
                Main.errorLabel.setText("");
            }
        }
        draw();
        return ret;
    }
    public String[] updateWeights(String from[], String to[], int weight[]){
        String ret[] = new String[from.length];
        for (int i = 0; i < from.length; ++i) {
            if (!values.containsValue(from[i]) || !values.containsValue(to[i])){
                ret[i] = "One of the nodes are not found";
                Main.errorLabel.setText(ret[i]);
            }
            else{
                if(visitedEdge.containsKey(new Pair(index.get(from[i]), index.get(to[i]))))
                    graphRep[index.get(from[i])][index.get(to[i])] = weight[i];
                else if(visitedEdge.containsKey(new Pair(index.get(to[i]), index.get(from[i]))))
                    graphRep[index.get(to[i])][index.get(from[i])] = weight[i];

                ret[i] = "Edge Updated Successfully";
            }
        }
        Main.errorLabel.setText("");
        draw();
        return ret;
    }
    public int sumWeights(String from[], String to[]){
        int ret = 0;
        for (int i = 0; i < from.length; ++i){
            if (!values.containsValue(from[i])|| !values.containsValue(to[i])){
                ret = -1000000000; //9
                break;
            }
            if (visitedEdge.containsKey(new Pair(index.get(from[i]), index.get(to[i]))))
                ret += graphRep[index.get(from[i])][index.get(to[i])];
            else if (visitedEdge.containsKey(new Pair(index.get(to[i]), index.get(from[i]))))
                ret += graphRep[index.get(to[i])][index.get(from[i])];
            nodeInSumPath.put(index.get(from[i]), true);
            nodeInSumPath.put(index.get(to[i]), true);
            edgeInSumPath.put(new Pair(index.get(from[i]), index.get(to[i])), true);
        }
        Main.errorLabel.setText("");
        draw();
        return ret;
    }
    public void draw(){
        Main.nodes.clear();
        Main.edges.clear();
        Main.weights.clear();
        Main.rays.clear();
        drawNodes(1400/2, 950/2);
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) {
                if (graphRep[i][j] >= 1) {
                    Color color = Color.BLACK;
                    if (edgeInSumPath.containsKey(new Pair(i, j)) || edgeInSumPath.containsKey(new Pair(j, i))) color = Color.RED;
                    //if (visitedEdge.containsKey(new Pair(j, i)) == false){
                    System.out.println(values.get(i) + " " + values.get(j));
                    Main.addGUIEdge(values.get(i), values.get(j),graphRep[i][j], color);
                    //  visitedEdge.put(new Pair(i, j), true);
                    //}

                }
            }
        renderLayout();
    }
    public void renderLayout(){
        Group layout = new Group();
        for (int i = 0; i < Main.nodes.size(); ++i){
            layout.getChildren().add(Main.nodes.get(i));
        }
        for (int i = 0; i < Main.edges.size(); ++i)
            layout.getChildren().add(Main.edges.get(i));
        for (int i = 0; i < Main.weights.size(); ++i)
            layout.getChildren().add(Main.weights.get(i));
        layout.getChildren().addAll(Main.menuLayout);
        Line vLine = new Line(1400, 0, 1400, 950);
        layout.getChildren().addAll(vLine);
        Scene scene = new Scene(layout, 1900, 950);
        Main.window.setScene(scene);
    }
}