package com.atadams.picross.data;

import com.atadams.picross.exception.PicrossSolverException;

public class Cell {
	
	public enum Status {
		UNKNOWN,
		FILLED,
		EMPTY
	}
	
	private Status status = Status.UNKNOWN;
	private Status nextStatus = null; //a null nextStatus indicates that this cell has never been updated
	private Status tempStatus = null; //just used for calculations

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Status getNextStatus() {
		return nextStatus;
	}
	public void setNextStatus(Status nextStatus) {
		this.nextStatus = nextStatus;
	}
	
	public void updateStatus(Status nextStatus) throws PicrossSolverException {
		if (this.nextStatus != null && nextStatus != this.nextStatus) {
			throw new PicrossSolverException("Cannot update a previously updated cell. Previously updated to: " + this.nextStatus + ". Trying to update to: " + nextStatus + ".");
		}
		this.nextStatus = nextStatus;
	}
	
	public void doUpdate() {
		if (this.nextStatus != null) {
			this.status = this.nextStatus;
		}
	}
	
	public boolean isSolved() {
		if (status != Status.UNKNOWN) {
			return true;
		}
		return false;
	}
	
	public Cell deepClone() {
		Cell cellClone = new Cell();
		cellClone.setStatus(this.status);
		return cellClone;
	}
	
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		switch (this.status) {
			case UNKNOWN:
				outputString.append(" ");
				break;
			case FILLED:
				outputString.append('\u25A0');
				break;
			case EMPTY:
				outputString.append('x');
		}
		return outputString.toString();
	}

}
