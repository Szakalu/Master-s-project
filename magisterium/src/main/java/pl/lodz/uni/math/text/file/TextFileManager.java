package pl.lodz.uni.math.text.file;

import pl.lodz.uni.math.graph.model.*;

import java.io.*;

public class TextFileManager {

    private static final String NODE_STRING = "NODE";
    private static final String EDGE_STRING = "EDGE";
    private static final int NODE_NUMBER = 1;
    private static final int EDGE_NUMBER = 2;
    
    public TextFileManager() {

    }

    public static void readGraphFromFile(UndirectedGraphModel undirectedGraphModel, String graphPath){
        File graphFile = new File(graphPath);
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(graphFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String st;
        int mode = 0;
        try {
            String [] splittedLineFromFile;
            while ((st = bufferedReader.readLine()) != null) {

                splittedLineFromFile = st.split(" ");
                if(splittedLineFromFile.length == 1){
                    if(splittedLineFromFile[0].equals(EDGE_STRING)){
                        mode = EDGE_NUMBER;
                    	undirectedGraphModel.refactorNeighborTableCreate();
                        continue;
                    }
                    else if(splittedLineFromFile[0].equals(NODE_STRING)){
                    	if(mode != EDGE_NUMBER){
                    		mode = NODE_NUMBER;
                    	}
                    	continue;
                    }
                }

                switch (mode){
                    case NODE_NUMBER:
                        addNode(undirectedGraphModel, splittedLineFromFile);
                        break;
                    case EDGE_NUMBER:
                        addEdge(undirectedGraphModel, splittedLineFromFile);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addNode(UndirectedGraphModel undirectedGraphModel, String [] stringNode){
        NodeModel myNode;
        switch (stringNode.length){
            case 1:
                myNode = new NodeModel(stringNode[0]);
                undirectedGraphModel.addNodeFromFile(myNode);
                break;
            case 2:
                try {
                    myNode = new NodeModel(stringNode[0], Double.parseDouble(stringNode[1]));
                    undirectedGraphModel.addNodeFromFile(myNode);
                } catch (NumberFormatException e){
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    private static void addEdge(UndirectedGraphModel undirectedGraphModel, String [] stringEdge){
        EdgeModel myEdge;
        switch (stringEdge.length){
            case 2:
                myEdge = new EdgeModel(stringEdge[0],stringEdge[1]);
                undirectedGraphModel.addEdge(myEdge);
                break;
            case 3:
                try {
                    myEdge = new EdgeModel(stringEdge[0],stringEdge[1], Double.parseDouble(stringEdge[2]));
                    undirectedGraphModel.addEdge(myEdge);
                } catch (NumberFormatException e){
                    //TODO
                }
                break;
            default:
                break;
        }
    }
    
    public static void saveGraphToFile(String filePathToSave, UndirectedGraphModel undirectedGraphModel){
    	PrintWriter writer = null;
		try {
			writer = new PrintWriter(filePathToSave, "UTF-8");
			writer.println("NODE");
	    	for(NodeModel myNode: undirectedGraphModel.getNodesList()){
	    		writer.println(myNode.getName() + " " + myNode.getHeuristicValue());
	    	}
	    	int nodesListSize = undirectedGraphModel.getNodesList().size();
	    	double [][] neighborsMatrix = undirectedGraphModel.getNeighborsMatrix();
	    	writer.println("EDGE");
			for(int i = 0; i < nodesListSize; ++i){
				for(int j = i; j < nodesListSize; ++j){
					if(i!=j && neighborsMatrix[i][j] > 0){
						writer.println(undirectedGraphModel.getNodesList().get(i).getName() + " " + undirectedGraphModel.getNodesList().get(j).getName() + " " + neighborsMatrix[i][j]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			writer.close();
		}
    }
}
