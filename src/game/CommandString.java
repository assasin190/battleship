package game;

public class CommandString {
	
	//Includes all the message sent between server and client
	public static final String CLIENT_GAME_SETUP_READY = "CLIENT_GAME_SETUP_READY";
	public static final String CLIENT_GAME_START_READY = "CLIENT_GAME_START_READY";
	public static final String CLIENT_START_GAME_SETUP = "CLIENT_START_GAME_SETUP";
	public static final String CLIENT_LOSE = "CLIENT_LOSE";
	
	public static final String SERVER_OTHER_CLIENT_NOT_AVAILABLE = "SERVER_OTHER_CLIENT_NOT_AVAILABLE";
	public static final String SERVER_OTHER_CLIENT_AVAILABLE = "SERVER_OTHER_CLIENT_AVAILABLE";
	public static final String SERVER_START_GAME_SETUP = "SERVER_START_GAME_SETUP";
	public static final String SERVER_OPPONENT_NOT_READY = "SERVER_OPPONENT_NOT_READY";
	public static final String SERVER_OPPONENT_READY = "SERVER_OPPONENT_READY";
	public static final String SERVER_START_GAME = "SERVER_START_GAME";
	public static final String SERVER_GRANT_TURN = "SERVER_GRANT_TURN";
	public static final String SERVER_INDICATE_YOU_WIN = "SERVER_INDICATE_YOU_WIN";
;	
	public static final String SERVER_REQUEST_BOARD_EXCHANGE = "SERVER_REQUEST_BOARD_EXCHANGE";
	public static final String CLIENT_SUBMIT_BOARD_EXCHANGE = "CLIENT_SUBMIT_EXCHANGE_REQUEST";
	
	/*	Validate a command string
		If the command string is valid, return true
		else return false
	*/
	public static boolean validateCommand(String command) {
		switch(command) {
			case CLIENT_GAME_SETUP_READY:	 	return true;
			case CLIENT_GAME_START_READY:		return true;
			default: 							return false;
		}
	}
}
