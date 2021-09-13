package pl.lodz.uni.math.algorithms;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.graphstream.algorithm.util.FibonacciHeap.Node;

import pl.lodz.uni.math.graph.model.NodeModel;

public class Ant {
	
	private int number;
	private ArrayList<Integer> tabuList;
	private ArrayList<Integer> withdrawnList;
	private boolean foundEnd;
	private ArrayList<NodeNumberAndProbablistic> candidateNodesList;
	
	public Ant(int number) {
		this.number = number;
		this.tabuList = new ArrayList<Integer>();
		this.withdrawnList = new ArrayList<Integer>();
		this.foundEnd = false;
		this.candidateNodesList = new ArrayList<Ant.NodeNumberAndProbablistic>();
	}
	
	public void addNewNodeCandidate(int nodeIndex, double visibilityMultPheromones){
//		if(candidateNodesList.size() == 0){
//			candidateNodesList.add(new NodeNumberAndProbablistic(nodeIndex, visibilityMultPheromones));
//		}
//		else{
//			for(int index = 0; index < candidateNodesList.size(); ++index){
//				if(candidateNodesList.get(index).getProbabilistic() > visibilityMultPheromones){
//					candidateNodesList.add(index, new NodeNumberAndProbablistic(nodeIndex, visibilityMultPheromones));
//					return;
//				}
//			}
//			candidateNodesList.add(new NodeNumberAndProbablistic(nodeIndex, visibilityMultPheromones));
//		}
		candidateNodesList.add(new NodeNumberAndProbablistic(nodeIndex, visibilityMultPheromones));
	}
	
	public void calculateProbablistics(){
		double sum = 0;
		for(NodeNumberAndProbablistic nodeNumberAndProbablistic: candidateNodesList){
			sum = BigDecimal.valueOf(sum).add(BigDecimal.valueOf(nodeNumberAndProbablistic.getProbabilistic())).doubleValue();
		}
		for(NodeNumberAndProbablistic nodeNumberAndProbablistic: candidateNodesList){
			nodeNumberAndProbablistic.setProbabilistic(nodeNumberAndProbablistic.getProbabilistic()/sum);
		}
	}
	
	public void calculateProbablisticForDraw(){
		//ustawianie probabilistycznej list w postaci wartoscProbablistyczna(index) = wartoscProbablistyczna(index) + wartoscProbablistyczna(index-1) od index = 1
		for(int index = 1; index < candidateNodesList.size(); index++){
			candidateNodesList.get(index).setProbabilistic(BigDecimal.valueOf(candidateNodesList.get(index).getProbabilistic()).add(BigDecimal.valueOf(candidateNodesList.get(index-1).getProbabilistic())).doubleValue());
		}
	}
	
	public int drawNextNode(double drawnNumber, ArrayList<NodeModel> nodesList){
		for(NodeNumberAndProbablistic nodeNumberAndProbablistic: candidateNodesList){
			if(drawnNumber <= nodeNumberAndProbablistic.getProbabilistic()){
				tabuList.add(nodeNumberAndProbablistic.getNodeNumber());
//				System.out.println(drawnNumber + " " + nodeNumberAndProbablistic.getNodeNumber() + " " + nodesList.get(tabuList.get(tabuList.size()-2)).getName() + " -> " + nodesList.get(nodeNumberAndProbablistic.getNodeNumber()).getName());
				return nodeNumberAndProbablistic.getNodeNumber();
			}
		}
		if(candidateNodesList.size() > 0){
			tabuList.add(candidateNodesList.get(candidateNodesList.size()-1).getNodeNumber());
			return candidateNodesList.get(candidateNodesList.size()-1).getNodeNumber();
		}
		return -1;
	}
	
	public void clearProbablisticNodeList(){
		candidateNodesList.clear();
	}
	
	public void clearTabuList(){
		tabuList.clear();
	}
	
	public void clearWithdawnList(){
		withdrawnList.clear();
	}

	public int getNumber() {
		return number;
	}

	public ArrayList<Integer> getTabuList() {
		return tabuList;
	}

	public ArrayList<Integer> getWithdrawnList() {
		return withdrawnList;
	}

	public boolean isFoundEnd() {
		return foundEnd;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setTabuList(ArrayList<Integer> tabuList) {
		this.tabuList = tabuList;
	}

	public void setWithdrawnList(ArrayList<Integer> withdrawnList) {
		this.withdrawnList = withdrawnList;
	}

	public void setFoundEnd(boolean foundEnd) {
		this.foundEnd = foundEnd;
	}
	
	public ArrayList<NodeNumberAndProbablistic> getProbablisticNodesList() {
		return candidateNodesList;
	}

	public void setProbablisticNodesList(ArrayList<NodeNumberAndProbablistic> probablisticNodesList) {
		this.candidateNodesList = probablisticNodesList;
	}



	class NodeNumberAndProbablistic{
		
		private int nodeNumber;
		private double probabilistic;
		
		public NodeNumberAndProbablistic(int nodeNumber, double probabilistic) {
			super();
			this.nodeNumber = nodeNumber;
			this.probabilistic = probabilistic;
		}
		public int getNodeNumber() {
			return nodeNumber;
		}
		public void setNodeNumber(int nodeNumber) {
			this.nodeNumber = nodeNumber;
		}
		public double getProbabilistic() {
			return probabilistic;
		}
		public void setProbabilistic(double probabilistic) {
			this.probabilistic = probabilistic;
		}
		
		
	}
}
