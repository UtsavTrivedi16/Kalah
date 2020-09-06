package kalah;

import java.util.HashMap;

public class GameManager{
    private States _gameState;
    private GameMode _gameMode;
    private boolean _isGameRunning;
    private HashMap<Integer, GameObject> _globalGameObjects;

    public GameManager() {
        this._isGameRunning = true;
        this._gameState = States.INIT;
        _globalGameObjects = new HashMap<Integer, GameObject>();
    }

    public enum States{
        INIT, P1, P2, CHECK_WIN, DEBUG, QUIT
    }

    public enum GameMode{
        ALL_ROBOT, P2_ROBOT, P1_ROBOT, ALL_HUMAN
    }

    public States getGameState() {
        return _gameState;
    }

    public void setGameState(States gameState) {
        this._gameState = gameState;
    }

    public GameMode getGameMode() {
        return _gameMode;
    }

    public void setGameMode(GameMode _gameMode) {
        this._gameMode = _gameMode;
    }

    public boolean getIsGameRunning() {
        return _isGameRunning;
    }

    public void setIsGameRunning(boolean isGameRunning) {
        this._isGameRunning = isGameRunning;
    }

    public void addGlobalGameObject(GameObject gameObject){
        if(!(_globalGameObjects.containsKey(gameObject.getObjectID()))) {
            _globalGameObjects.put(gameObject.gameObjectID, gameObject);
        }
    }

    public void clearGlobalGameObjects(){
        this._globalGameObjects.clear();
    }

    public boolean isGlobalGameObjectArrayEmpty(){
        return _globalGameObjects.isEmpty();
    }

}