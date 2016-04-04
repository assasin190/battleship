package GameState;
import javax.swing.JFrame;

import Game.Main;
import GameState.GameStateManager;
import UserInterface.MainMenuUI;

public class MainGameState extends GameState {
	
	public MainGameState(Main main) {
		super(main);
	}

	@Override
	public void entered() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaving() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString() {
		return GameStateManager.MAIN_GAME_STATE;
				
	}

}
