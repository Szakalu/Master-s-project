package pl.lodz.uni.math.graph.model;

public class NodeModel {

    private int number = 0;
    private String name = "";
    private double heuristicValue = 0;

    public NodeModel(String name) {
        this.name = name;
    }

    public NodeModel(String name, double heuristicValue) {
        this.name = name;
        this.heuristicValue = heuristicValue;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", heuristicValue=" + heuristicValue +
                '}';
    }
}
