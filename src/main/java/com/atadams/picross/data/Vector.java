package com.atadams.picross.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.atadams.picross.exception.PicrossSolverException;

public class Vector {
	private List<Cell> cells;
	private List<Integer> clues;
	
	public List<Cell> getCells() {
		return cells;
	}
	public void setCells(List<Cell> cells) {
		this.cells = cells;
	}
	public List<Integer> getClues() {
		return clues;
	}
	public void setClues(List<Integer> clues) {
		this.clues = clues;
	}
	
	public void doUpdate() {
		for (Cell cell : cells) {
			cell.doUpdate();
		}		
	}
	
	public boolean isSolved() {
		for (Cell cell : cells) {
			if (!cell.isSolved()) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		
		for (Cell cell : cells) {
			outputString.append(cell.toString());
			outputString.append('|');
		}
		
		return outputString.toString();
	}
	
	public Vector deepClone() {
		Vector clone = new Vector();
		List<Cell> cellsClone = new ArrayList<Cell>();
		for (Cell cell : cells) {
			cellsClone.add(cell.deepClone());
		}
		clone.setCells(cellsClone);
		List<Integer> cluesClone = new ArrayList<Integer>();
		for (Integer clue : clues) {
			cluesClone.add(Integer.valueOf(clue.intValue()));
		}
		clone.setClues(cluesClone);
		return clone;
	}
	
	public List<Cell> deepCopy(List<Cell> inputList){
		List<Cell> copy = new ArrayList<Cell>();
		for (Cell inputCell : inputList) {
			copy.add(inputCell.deepClone());
		}
		return copy;
	}
	
	public List<Integer> deepCopyInt(List<Integer> inputList){
		List<Integer> copy = new ArrayList<Integer>();
		for (Integer inputCell : inputList) {
			copy.add(inputCell);
		}
		return copy;		
	}
	
	public List<Cell.Status> copyToCellStatusList(List<Cell> inputList){
		List<Cell.Status> copy = new ArrayList<Cell.Status>();
		for (Cell cell : inputList) {
			copy.add(cell.getStatus());
		}
		return copy;
	}
	
	public List<Cell> copyToCellList(List<Cell.Status> inputList){
		List<Cell> copy = new ArrayList<Cell>();
		for (Cell.Status status : inputList) {
			Cell cell = new Cell();
			cell.setStatus(status);
			copy.add(cell);
		}
		return copy;
	}
	
	public List<Cell.Status> statusCopy(List<Cell.Status> inputList) {
		List<Cell.Status> copy = new ArrayList<Cell.Status>();
		for (Cell.Status status : inputList) {
			copy.add(status);
		}
		return copy;
	}
	
	public void applyClues() {
		if (!isSolved()) {
			//here is the meat of the logic of the solver
			
			List<Cell.Status> possibilities = checkClues(copyToCellStatusList(cells), deepCopyInt(clues), 0, null);
			
			for (int i = 0; i < possibilities.size(); i++) {
				if (possibilities.get(i) != Cell.Status.UNKNOWN && cells.get(i).getStatus() != possibilities.get(i)) {
					try {
						cells.get(i).updateStatus(possibilities.get(i));	
					}
					catch (PicrossSolverException ex) {
						System.out.println("Unable to update cell: " + ex.getMessage());
					}					
				}
			}
		}
	}
	
	private List<Cell.Status> checkClues(List<Cell.Status> cells, List<Integer> remainingClues, int startingPos, List<Cell.Status> possibilities) {
		if (remainingClues.size() == 0) {
			for (int i = startingPos; i < cells.size(); i++) {
				if (cells.get(i) != Cell.Status.FILLED) {
					cells.set(i, Cell.Status.EMPTY);
				}
				else {
					return possibilities; // failure condition - a cell needs to be marked as EMPTY but it's already FILLED
				}
			}
			possibilities = addPossibilities(possibilities, cells);
			//possibilities.add(cells); // we found a possibility!
			
		}
		else {
			int clueSum = 0;
			for (Integer clue : remainingClues) {
				clueSum += clue;
			}
			int endingPosition = cells.size() - clueSum - remainingClues.size() + 1 + 1;
			for (int i = startingPos; i < endingPosition; i++) {
				//check if the previous square is FILLED, if it is, this is not valid and in fact we need to stop
				if (i > 0 && cells.get(i - 1) == Cell.Status.FILLED) {
					break;
				}
				if (cells.get(i) != Cell.Status.EMPTY) {
					//attempt to apply the clue here
					boolean operationSuccess = true;
					List<Cell.Status> cellsCopy = statusCopy(cells);
					for (int j = 0; j < remainingClues.get(0); j++) {
						if (cellsCopy.get(j + i) != Cell.Status.EMPTY) {
							cellsCopy.set(j + i, Cell.Status.FILLED);
						}
						else {
							operationSuccess = false;
							break;
						}
					}
					if (operationSuccess && remainingClues.size() > 1) {
						//if there are more clues left we need to add a separator space
						if (cells.get(i + remainingClues.get(0)) != (Cell.Status.FILLED)) {
							cellsCopy.set(i + remainingClues.get(0), Cell.Status.EMPTY);
						}
						else {
							operationSuccess = false;
						}
					}
					if (operationSuccess) {
						List<Integer> cluesCopy = deepCopyInt(remainingClues);
						cluesCopy.remove(0);
						int newPosition = (i + remainingClues.get(0));
						if (cluesCopy.size() > 0) {
							newPosition++;
						}
						possibilities = checkClues(cellsCopy, cluesCopy, newPosition, possibilities);
						//we were able to apply the first clue so do the recursive call
					}
				}
				if (cells.get(i) != Cell.Status.FILLED) {
					cells.set(i, Cell.Status.EMPTY);
				}
			}
		}
		return possibilities;
	}
	
	public List<Cell.Status> addPossibilities (List<Cell.Status> possibilities, List<Cell.Status> newPossibility){
		if (possibilities == null) {
			return newPossibility;
		}
		for (int i = 0; i < possibilities.size(); i++) {
			if (possibilities.get(i) != newPossibility.get(i)) {
				possibilities.set(i, Cell.Status.UNKNOWN);
			}
		}
		return possibilities;
	}
}
