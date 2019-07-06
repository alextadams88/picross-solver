package com.atadams.picross;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atadams.picross.data.Board;
import com.atadams.picross.data.Cell;
import com.atadams.picross.data.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	Board board = buildBoard();
    	board.solve();
        assertTrue( true );
    }
    
    private Board buildBoard() {
    	Cell[][] board = new Cell[6][6];
    	for (int i = 0; i < 6; i++) {
    		for (int j = 0; j < 6; j++) {
    			board[i][j] = new Cell(); 
    		}
    	}
    	List<Vector> rows = new ArrayList<Vector>();
    	List<Vector> columns = new ArrayList<Vector>();
    	for (int i = 0; i < 6; i++){
    		Vector newRow = new Vector();
    		List<Cell> cells = new ArrayList<Cell>();
    		for (int j = 0; j < 6; j++) {
    			cells.add(board[i][j]);
    		}
    		newRow.setCells(cells);
    		rows.add(newRow);
    	}
    	for (int i = 0; i < 6; i++){
    		Vector newCol = new Vector();
    		List<Cell> cells = new ArrayList<Cell>();
    		for (int j = 0; j < 6; j++) {
    			cells.add(board[j][i]);
    		}
    		newCol.setCells(cells);
    		columns.add(newCol);
    	}
    	List<Integer> cluesR1 = Arrays.asList(2, 1);
    	List<Integer> cluesR2 = Arrays.asList(1, 3);
    	List<Integer> cluesR3 = Arrays.asList(1, 2);
    	List<Integer> cluesR4 = Arrays.asList(3);
    	List<Integer> cluesR5 = Arrays.asList(4);
    	List<Integer> cluesR6 = Arrays.asList(1);
    	
    	List<Integer> cluesC1 = Arrays.asList(1);
    	List<Integer> cluesC2 = Arrays.asList(5);
    	List<Integer> cluesC3 = Arrays.asList(2);
    	List<Integer> cluesC4 = Arrays.asList(5);
    	List<Integer> cluesC5 = Arrays.asList(2, 1);
    	List<Integer> cluesC6 = Arrays.asList(2);
    	
    	rows.get(0).setClues(cluesR1);
    	rows.get(1).setClues(cluesR2);
    	rows.get(2).setClues(cluesR3);
    	rows.get(3).setClues(cluesR4);
    	rows.get(4).setClues(cluesR5);
    	rows.get(5).setClues(cluesR6);
    	
    	columns.get(0).setClues(cluesC1);
    	columns.get(1).setClues(cluesC2);
    	columns.get(2).setClues(cluesC3);
    	columns.get(3).setClues(cluesC4);
    	columns.get(4).setClues(cluesC5);
    	columns.get(5).setClues(cluesC6);
    	
    	Board myBoard = new Board();
    	myBoard.setColumns(columns);
    	myBoard.setRows(rows);
    	
    	return myBoard;
    }
}
