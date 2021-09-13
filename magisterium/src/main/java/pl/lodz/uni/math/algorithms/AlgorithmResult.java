package pl.lodz.uni.math.algorithms;

import java.math.BigDecimal;

public class AlgorithmResult {

    private BigDecimal costTable[];
    private int visitedTable[];
    private long time;
    private int startIndex;
    private int endIndex;
    
    

	public AlgorithmResult(BigDecimal[] costTable, int[] visitedTable, long time, int startIndex, int endIndex) {
		super();
		this.costTable = costTable;
		this.visitedTable = visitedTable;
		this.time = time;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public BigDecimal[] getCostTable() {
		return costTable;
	}

	public int[] getVisitedTable() {
		return visitedTable;
	}

	public long getTime() {
		return time;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}
	
    
}
