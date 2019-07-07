package com.atadams.picross;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.atadams.picross.data.Board;
import com.atadams.picross.data.Cell;
import com.atadams.picross.data.Vector;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args )
    {
        if (args.length > 0) {
        	for (int i = 0; i < args.length; i++) {
        		try {
        			Board board = buildBoard(new File(args[i]));
        			board.solve();
        		}
            	catch (FileNotFoundException ex) {
            		System.out.println("File not found: " + args[i]);
            		ex.printStackTrace();
            	}
            	catch (IOException ex) {
            		ex.printStackTrace();
            	}
        		
        	}
        }
    }
    
    public static Board buildBoard(File inputFile) throws FileNotFoundException, IOException {
		Board newBoard = new Board();
		List<Vector> rows = new ArrayList<Vector>();
		List<Vector> columns = new ArrayList<Vector>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		int rowCount = Integer.parseInt(reader.readLine());
		int colCount = Integer.parseInt(reader.readLine());
		List<List<Integer>> rowClues = new ArrayList<List<Integer>>();
		List<List<Integer>> colClues = new ArrayList<List<Integer>>();
		
		//read in all the row clues
		for (int i = 0; i < rowCount; i++) {
			List<Integer> currRowClues = 
					Arrays.asList(
							reader.readLine()
							.split(" "))
					.stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());
			rowClues.add(currRowClues);
		}
		
		//read in all the column clues
		for (int i = 0; i < colCount; i++) {
			List<Integer> currColClues = 
					Arrays.asList(
							reader.readLine()
							.split(" "))
					.stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());
			colClues.add(currColClues);
		}
		
		reader.close();
		
		//build all the cells
		Cell[][] board = new Cell[rowCount][colCount];
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				board[i][j] = new Cell();
			}
		}
		
		//create the row vectors
		for (int i = 0; i < rowCount; i++) {
			Vector newRow = new Vector();
			List<Cell> cells = new ArrayList<Cell>();
			for (int j = 0; j < colCount; j++) {
				cells.add(board[i][j]);
			}
			newRow.setCells(cells);
			newRow.setClues(rowClues.get(i));
			rows.add(newRow);
		}
		
		//create the column vectors
		for (int i = 0; i < colCount; i++) {
			Vector newCol = new Vector();
			List<Cell> cells = new ArrayList<Cell>();
			for (int j = 0; j < rowCount; j++) {
				cells.add(board[j][i]);
			}
			newCol.setCells(cells);
			newCol.setClues(colClues.get(i));
			columns.add(newCol);
		}
		
		//add them to the board and return the board
		newBoard.setRows(rows);
		newBoard.setColumns(columns);
		return newBoard;
    }
}
