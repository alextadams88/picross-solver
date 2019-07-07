package com.atadams.picross.data;

import java.util.List;

public class Board {
	private List<Vector> rows;
	private List<Vector> columns;
	
	public List<Vector> getRows() {
		return rows;
	}
	public void setRows(List<Vector> rows) {
		this.rows = rows;
	}
	public List<Vector> getColumns() {
		return columns;
	}
	public void setColumns(List<Vector> columns) {
		this.columns = columns;
	}
	
	public void solve() {
		int iterationCount = 0;
		System.out.println(this);
		long startTime = System.nanoTime();
		while (!isSolved()) {
			iterateClues();
			doUpdate();
			System.out.println(this);
		}
		long timeElapsed = System.nanoTime() - startTime;
		System.out.println("Execution time in nanoseconds  : " + timeElapsed);

		System.out.println("Execution time in milliseconds : " + 
								timeElapsed / 1000000);
	}
	
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		for (Vector row : rows) {
			outputString.append(row.toString());
			outputString.append('\n');
			for (int i = 0; i < columns.size() * 2; i++) {
				outputString.append('-');
			}
			outputString.append('\n');
		}
		return outputString.toString();
	}
	
	public void iterateClues() {
		for (Vector row: rows) {
			row.applyClues();
		}
		System.gc();
		doUpdate();
		
		for (Vector column : columns) {
			column.applyClues();
		}
	}
	
	public void doUpdate() {
		for (Vector row: rows) {
			row.doUpdate();
		}
		
		for (Vector column : columns) {
			column.doUpdate();
		}
	}
	
	public boolean isSolved() {
		for (Vector row: rows) {
			if (!row.isSolved()) {
				return false;
			}
		}
		
		for (Vector column : columns) {
			if (!column.isSolved()) {
				return false;
			}
		}
		return true;
	}

}
