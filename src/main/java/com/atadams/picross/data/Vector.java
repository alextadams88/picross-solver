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
			copy.add(Integer.valueOf(inputCell.intValue()));
		}
		return copy;		
	}
	
	public void applyClues() {
		//here is the meat of the logic of the solver
		
		Set<List<Cell>> possibilities = new HashSet<List<Cell>>();
		checkClues(deepCopy(cells), deepCopyInt(clues), 0, possibilities);
		
		for (int i = 0; i < cells.size(); i++) {
			boolean updateCell = true;
			Cell.Status potentialStatus = null;
			if (cells.get(i).getStatus() == Cell.Status.UNKNOWN) {
				for (List<Cell> possibility : possibilities) {
					//check each possible application of the clues to see if this cell has the same status in all possibilities
					Cell possibilityCell = possibility.get(i);
					if (potentialStatus != null && potentialStatus != possibilityCell.getStatus()) {
						updateCell = false;
					}
					if (potentialStatus == null) {
						potentialStatus = possibilityCell.getStatus();
					}
				}

				if (updateCell) {
					try {
						cells.get(i).updateStatus(potentialStatus);
					}
					catch (PicrossSolverException ex) {
						System.out.println("Unable to update cell: " + ex.getMessage());
					}
				}
			}
		}
	}
	
	private void checkClues(List<Cell> cells, List<Integer> remainingClues, int startingPos, Set<List<Cell>> possibilities) {
		if (remainingClues.size() == 0) {
			for (int i = startingPos; i < cells.size(); i++) {
				if (cells.get(i).getStatus() != Cell.Status.FILLED) {
					cells.get(i).setStatus(Cell.Status.EMPTY);
				}
				else {
					return; // failure condition - a cell needs to be marked as EMPTY but it's already FILLED
				}
			}
			possibilities.add(cells); // we found a possibility!
			return;
		}
		else {
			int clueSum = 0;
			for (Integer clue : remainingClues) {
				clueSum += clue;
			}
			int endingPosition = cells.size() - clueSum - remainingClues.size() + 1 + 1;
			//int endingPosition = clueSum + remainingClues.size() - 1;
			for (int i = startingPos; i < endingPosition; i++) {
				if (cells.get(i).getStatus() != Cell.Status.EMPTY) {
					//check if the previous square is FILLED, if it is, this is not valid and in fact we need to stop
					if (i > 0 && cells.get(i - 1).getStatus() == Cell.Status.FILLED) {
						break;
					}
					//attempt to apply the clue here
					boolean operationSuccess = true;
					List<Cell> cellsCopy = deepCopy(cells);
					for (int j = 0; j < remainingClues.get(0); j++) {
						if (cellsCopy.get(j + i).getStatus() != Cell.Status.EMPTY) {
							cellsCopy.get(j + i).setStatus(Cell.Status.FILLED);
						}
						else {
							operationSuccess = false;
						}
					}
					if (operationSuccess && remainingClues.size() > 1) {
						//if there are more clues left we need to add a separator space
						if (cells.get(i + remainingClues.get(0)).getStatus() != (Cell.Status.FILLED)) {
							cellsCopy.get(i + remainingClues.get(0)).setStatus(Cell.Status.EMPTY);
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
						checkClues(cellsCopy, cluesCopy, newPosition, possibilities);
						//we were able to apply the first clue so do the recursive call
					}
				}
				if (cells.get(i).getStatus() != Cell.Status.FILLED) {
					cells.get(i).setStatus(Cell.Status.EMPTY);
				}
			}
		}
	}
}
