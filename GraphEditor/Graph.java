/* Comment */
package GraphEditor;

import javafx.scene.paint.Color;
import javafx.util.Pair;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.*;

abstract class Graph{
    protected final int size;
    protected int cntr;
    protected int graphRep[][];
    protected int level[];
    protected int inDeg[];
    protected int outDeg[];
    protected HashMap<Integer, String> values;
    protected HashMap<String, Integer> index;
    protected HashMap<Integer, Pair> GUIIndex;
    protected HashMap<Pair, Integer> idxInGUI;
    protected HashMap<Integer, Boolean> nodeInSumPath;
    protected HashMap<Pair, Boolean> edgeInSumPath;
    protected int premutation[] = {2, 1, 3, 4, 0};
    public Graph(){
        this.size = 1000;
        this.cntr = 0;
        this.graphRep = new int[this.size][this.size];
        this.values = new HashMap<>();
        this.index = new HashMap<>();
        this.nodeInSumPath = new HashMap<>();
        this.edgeInSumPath = new HashMap<>();
        this.idxInGUI = new HashMap<>();
        this.GUIIndex = new HashMap<>();
        this.level = new int[size];
        this.inDeg = new int[size];
        this.outDeg = new int[size];
    }

    public String[] addVertices(String[] name){
        String ret[] = new String[name.length];
        for (int i = 0; i < name.length; ++i){
            if (values.containsValue(name[i])){
                ret[i] = name[i] + " is not Added";
                Main.errorLabel.setText(name[i] + " is already added !");
                return ret;
            }
            for (int j = 0; j < size; ++j){
                if (!values.containsKey(j)){
                    values.put(j, name[i]);
                    index.put(name[i], j);
                    ret[i] = name[i] + " is Added";
                    ++cntr;
                    break;
                }
                else if ("-1".equals(values.get(j))){
                    values.put(j, name[i]);
                    index.put(name[i], j);
                    ret[i] = name[i] + " is Added";
                    ++cntr;
                    break;
                }
            }
            Main.errorLabel.setText("");
        }
        draw();
        return ret;
    }
    public String[] removeVertices(String[] name){
        String ret[] = new String[name.length];
        for (int i = 0; i < name.length; ++i){
            if(!values.containsValue(name[i])) {
                ret[i] = name[i] + " Doesn't exist";
                Main.errorLabel.setText(name[i] + " is not Found !");
                return  ret;
            }
            else{
                Main.errorLabel.setText("");
                for (int j = 0; j < size; ++j){
                    if (values.get(j).equals(name[i])){
                        values.put(j, "-1");
                        for(int adj = 0; adj < size; ++adj)
                            graphRep[index.get(name[i])][adj] = 0;
                        for(int adj = 0; adj < size; ++adj)
                            graphRep[adj][index.get(name[i])] = 0;
                        ret[i] = name[i] + " is Removed";
                        --cntr;
                        break;
                    }
                }
            }
        }
        draw();
        return ret;
    }
    abstract public String[] addEdges(String from[], String to[]);
    abstract public String[] removeEdges(String from[], String to[]);
    abstract public String[] updateWeights(String from[], String to[], int weight[]);
    abstract public int sumWeights(String from[], String to[]);
    public void clearGraph(){
        Main.errorLabel.setText("");
        this.cntr = 0;
        this.graphRep = new int[this.size][this.size];
        this.values = new HashMap<>();
        this.index = new HashMap<>();
        this.nodeInSumPath = new HashMap<>();
        this.edgeInSumPath = new HashMap<>();
        this.idxInGUI = new HashMap<>();
        this.GUIIndex = new HashMap<>();
        this.level = new int[size];
        this.inDeg = new int[size];
        this.outDeg = new int[size];
        Main.nodes.clear();
        Main.edges.clear();
        Main.weights.clear();
        Main.rays.clear();
        renderLayout();
    }

    public void clearSumPath(){
        Main.errorLabel.setText("");
        this.nodeInSumPath = new HashMap<>();
        this.edgeInSumPath = new HashMap<>();
        draw();
    }
    public void drawNodes(int centerX, int centerY){
        if(cntr == 0) return;
        int numNodes = cntr;
        System.out.println(numNodes);
        String[] labels = new String[cntr];
        int itr = 0;
        for (Map.Entry entry : values.entrySet())
            if(!entry.getValue().equals("-1"))labels[itr++] = (String)entry.getValue();
        int nodeGap = 250;
        int zo2 = 50;
        Color color;
        if(nodeInSumPath.containsKey(index.get(labels[0]))) color = Color.RED;
        else color = Color.BLACK;
        Main.addGUINode(centerX + nodeGap - zo2, centerY - zo2, labels[0], color);
        Point2D result = new Point2D.Double(centerX + nodeGap, centerY);
        for(int i = 2; i <= numNodes; ++i){
            Point2D point = new Point2D.Double(result.getX(), result.getY());
            AffineTransform rotation = new AffineTransform();
            double angleInRadians = ((360/numNodes) * Math.PI / 180);
            rotation.rotate(angleInRadians, 1400/2, 950/2);
            rotation.transform(point, result);
            if(nodeInSumPath.containsKey(index.get(labels[i - 1]))) color = Color.RED;
            else color = Color.BLACK;
            Main.addGUINode((int)result.getX() - zo2, (int)result.getY() - zo2, labels[i - 1], color);
        }

    }
    abstract public void draw();
    abstract public void renderLayout();
}