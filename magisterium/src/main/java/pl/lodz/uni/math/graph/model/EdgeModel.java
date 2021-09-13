package pl.lodz.uni.math.graph.model;

public class EdgeModel {

    private String startName = "";
    private String endName = "";
    private int startIndex = -1;
    private int endIndex = -1;
    private double weight = 1;
    
    
    
    public EdgeModel(int startIndex, int endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public EdgeModel(String startName, String endName) {
        this.startName = startName;
        this.endName = endName;
    }

    public EdgeModel(String startName, String endName, double weight) {
        this.startName = startName;
        this.endName = endName;
        this.weight = weight;
    }

    public EdgeModel(String startName, String endName, int start, int end, double weight) {
        this.startName = startName;
        this.endName = endName;
        this.startIndex = start;
        this.endIndex = end;
        this.weight = weight;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int start) {
        this.startIndex = start;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int end) {
        this.endIndex = end;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "MyEdge{" +
                "startName='" + startName + '\'' +
                ", endName='" + endName + '\'' +
                ", start=" + startIndex +
                ", end=" + endIndex +
                ", weight=" + weight +
                '}';
    }
}
