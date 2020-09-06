package kalah;

import com.qualitascorpus.testsupport.IO;
import com.qualitascorpus.testsupport.MockIO;
import player.Human;
import player.Player;
import player.Robot;

public class Kalah{
	private Player _player1, _player2;
	private GameManager _gameManager;
	private GameView _gameView;

	public static void main(String[] args) {
		new Kalah().play(new MockIO());
	}

	public void play(IO io) {
		_gameView = new GameView(io, GameView.Modes.H_VIEW);
		_gameManager = new GameManager();
		_gameManager.setIsGameRunning(true);
		_gameManager.setGameMode(GameManager.GameMode.P2_ROBOT);

		while(_gameManager.getIsGameRunning()){
			tick();
		}

	}

	public void initGameObjects(){

		if(!(_gameManager.isGlobalGameObjectArrayEmpty())) _gameManager.clearGlobalGameObjects();

		switch (_gameManager.getGameMode()){

			case ALL_HUMAN:
				_player1 = new Human(1);
				_player2 = new Human(2);

				break;

			case P1_ROBOT:
				_player1 = new Robot(1);
				_player2 = new Human(2);

				break;

			case P2_ROBOT:
				_player1 = new Human(1);
				_player2 = new Robot(2);

				break;

			case ALL_ROBOT:
				_player1 = new Robot(1);
				_player2 = new Robot(2);

				break;
		}

		_player1.acquireHouses(6);
		_player2.acquireHouses(6);
		_player1.acquireStore();
		_player2.acquireStore();

		_gameManager.addGlobalGameObject(_player1);
		_gameManager.addGlobalGameObject(_player2);

	}

	public void tick(){
		boolean isExtraTurn = false;
		int userInput;
		int houseNo;
		String speech;

		switch (_gameManager.getGameState()) {

			case INIT:
				_gameView.initStringBuilders();
				initGameObjects();
				_gameManager.setGameState(GameManager.States.P1);

				break;

			case P1:
				_gameView.printBoard(_player1, _player2);

				if (_player1.areAllHousesEmpty()) {
					_gameManager.setGameState(GameManager.States.CHECK_WIN);
				}else{

					if (_player1.getIsRobot()) {
						isExtraTurn = _player1.makeMove(_player2);
						speech = _player1.getSpeech();
						_gameView.printPlayerSpeech(speech);

						if (isExtraTurn) {
							_gameManager.setGameState(GameManager.States.P2);
						} else {
							_gameManager.setGameState(GameManager.States.P1);
						}

					}else{

						userInput = _gameView.getUserInput(_player1.getplayerID(), _player1.getHousesCount());

						if (userInput == -1) {
							_gameManager.setGameState(GameManager.States.QUIT);
						}else{
							houseNo = userInput;

							if (_player1.isHouseEmpty(houseNo)) {
								_gameView.printHouseEmpty();
								_gameManager.setGameState(GameManager.States.P1);

							} else {
								isExtraTurn = _player1.makeMove(houseNo, _player2);

								if (isExtraTurn) {
									_gameManager.setGameState(GameManager.States.P1);
								} else {
									_gameManager.setGameState(GameManager.States.P2);
								}
							}
						}
					}

				}

				break;

			case P2:
				_gameView.printBoard(_player1, _player2);

				if (_player2.areAllHousesEmpty()) {
					_gameManager.setGameState(GameManager.States.CHECK_WIN);

				}else{

					if (_player2.getIsRobot()) {

						isExtraTurn = _player2.makeMove(_player1);
						speech = _player2.getSpeech();
						_gameView.printPlayerSpeech(speech);

						if (isExtraTurn) {
							_gameManager.setGameState(GameManager.States.P2);
						} else {
							_gameManager.setGameState(GameManager.States.P1);
						}

					}else{

						userInput = _gameView.getUserInput(_player2.getplayerID(), _player2.getHousesCount());

						if (userInput == -1) {
							_gameManager.setGameState(GameManager.States.QUIT);
						} else {
							houseNo = userInput;

							if (_player2.isHouseEmpty(houseNo)) {
								_gameView.printHouseEmpty();
								_gameManager.setGameState(GameManager.States.P2);
							} else {
								isExtraTurn = _player2.makeMove(houseNo, _player1);

								if (isExtraTurn) {
									_gameManager.setGameState(GameManager.States.P2);
								} else {
									_gameManager.setGameState(GameManager.States.P1);
								}
							}
						}
					}
				}

				break;

			case CHECK_WIN:
				_gameView.printGameOver();
				_gameView.printBoard(_player1, _player2);
				_gameView.printEndGame(_player1, _player2);
				_gameManager.setIsGameRunning(false);

				break;

			case QUIT:
				_gameView.printGameOver();
				_gameView.printBoard(_player1, _player2);
				_gameManager.setIsGameRunning(false);

				break;

			case DEBUG:

				_gameView.printDebugMode();
				_gameView.printBoard(_player1, _player2);
				userInput = _gameView.getUserInput(_player1.getplayerID(), _player1.getHousesCount());

				if (userInput == -1) {
					_gameManager.setGameState(GameManager.States.QUIT);
				}else{
					_gameManager.setGameState(GameManager.States.P2);
				}

				break;

		}
	}

}
