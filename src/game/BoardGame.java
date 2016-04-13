package game;

import java.util.ArrayList;

import game.Square;
import userInterface.GameSetupUIState.SquareLabel;

public class BoardGame {
	private Square [][] board;
	private Ship [] ships;
	
	/*	a Board Game contains 4 ships
	 * 	
	 */
	
	public BoardGame() {
		this.board = new Square[8][8];
		ships = new Ship[4];
		//Create a square for slot
		for(int y=0; y<8; y++) {
			for(int x=0; x<8; x++) {
				board[y][x] = new Square(y,x);
			}
		}
	}
	
	public boolean setShip(Ship ship, int shipNumber, SquareLabel[] occupationLabel) {
		//Check if square has already been occupied (by the other ships)
		//...
		//Set occupation
		Square[] occupation = new Square[4];
		for(int i=0; i<4; i++) {
			Square square = board[occupationLabel[i].getYIndex()][occupationLabel[i].getXIndex()];
			square.setOccupyingShip(ship);
			occupation[i] = square;
		}
		ship.setOccupation(occupation);
		//Set ship
		ships[shipNumber] = ship;
		//If setShip succeed, return true
		return true;
	}
	
	public Square[][] getBoard() {
		return board;
	}
	
	public Ship[] getAllShips() {
		return ships;
	}
	
	public Ship getShip(int index) {
		return ships[index];
	}
	
	public boolean checkOccupation(SquareLabel[] highlighting) {
		for(SquareLabel label : highlighting) {
			if(label.getSquare().isOccupied) //If the square is occupied, return true
			return true;
		}
		return false;
	}
	
	public void clearOccupation(Ship ship) {
		//Set array value as null
		ships[ship.shipNumber] = null;
		Square [] occupation = ship.occupation;
		for(Square square : occupation) {
			square.isOccupied = false;
			//Remove ship graphically
			square.label.setText("0");
		}
	}
	
	
	
	// TODO implements a boardgame's actions
}
