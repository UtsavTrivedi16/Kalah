package player;

import kalah.GameObject;
import kalah.House;
import kalah.Store;

import java.util.ArrayList;

public class Player extends GameObject {

    protected final int _playerID;
    protected ArrayList<House> _houses, _dummyHouses, _dummyEnemyHouses;
    protected Store _store, _dummyStore;
    protected boolean _isRobot;
    protected StringBuilder _speechStr;

    public Player(int playerID) {
        super();
        this._playerID = playerID;
        this._houses = new ArrayList<House>(6);
        this._speechStr = new StringBuilder();
    }

    public boolean makeMove(int houseNo, Player enemyPlayer){return false;}

    public boolean makeMove(Player enemyPlayer){return false;}

    protected int plantSeedsEnemyRow(int seedsTaken, ArrayList<House> enemyHouses){

        for(int houseIndex = 0; houseIndex < enemyHouses.size(); houseIndex++){
            if(seedsTaken == 0) {
                break;
            }else{
                enemyHouses.get(houseIndex).plantSeeds(1);
                seedsTaken--;
            }
        }

        return seedsTaken;

    }

    protected int plantSeedsOwnRow(int startHouseNo, int seedsTaken, ArrayList<House> enemyHouses){

        boolean isEnemyEmpty, isPlayerEmpty;
        int seedsNextHouse, enemyHouseNo, enemyHouseSeeds;

        for (int i = (startHouseNo - 1); i < _houses.size(); i++) {

            if(seedsTaken == 0) {
                break;
            }else{
                seedsNextHouse = _houses.get(i).getSeedCount();

                if((seedsNextHouse == 0) && (seedsTaken == 1)){

                    enemyHouseNo = enemyHouses.get(enemyHouses.size() - i - 1).gethouseNo();
                    isEnemyEmpty = enemyHouses.get(enemyHouseNo-1).isBuildingEmpty();
                    isPlayerEmpty = _houses.get(i).isBuildingEmpty();

                    if(!(isEnemyEmpty) && isPlayerEmpty){
                        enemyHouseSeeds = enemyHouses.get(enemyHouseNo-1).getSeedCount();
                        enemyHouses.get(enemyHouseNo-1).removeSeeds(enemyHouseSeeds);
                        _store.plantSeeds(seedsTaken + enemyHouseSeeds);
                    }else{
                        _houses.get(i).plantSeeds(1);
                    }

                }else{
                    _houses.get(i).plantSeeds(1);
                }

                seedsTaken--;
            }

        }

        return seedsTaken;

    }

    public void acquireHouses(int numHouses) {
        /*@assumption Each player has equal no of houses */

        for(int houseNo = 1; houseNo <= numHouses; houseNo++){
            House house = new House(houseNo);
            house.plantSeeds(4);
            _houses.add(house);
        }

    }

    public void acquireStore(){
        this._store = new Store();
    }

    public boolean areAllHousesEmpty(){
        boolean areEmpty = true;

        for(int i = 0; i < _houses.size(); i++){
            if(!(_houses.get(i).isBuildingEmpty())){
                areEmpty = false;
            }
        }

        return areEmpty;
    }

    public boolean isHouseEmpty(int houseNo){
        return _houses.get(houseNo-1).isBuildingEmpty();
    }

    public int getStoreSeedCount(){
        return _store.getSeedCount();
    }

    public int getAllHousesSeedCount(){
        int seedCount = 0;
        for(int i = 0 ; i < _houses.size(); i++){
            seedCount += _houses.get(i).getSeedCount();
        }

        return seedCount;
    }

    public int getHouseSeedCount(int houseNo){
        return _houses.get(houseNo - 1).getSeedCount();
    }

    public int getHousesCount(){
        _houses.trimToSize();
        return _houses.size();
    }

    public int getplayerID() {
        return _playerID;
    }

    public boolean getIsRobot(){
        return _isRobot;
    }

    protected void resetSpeechString(){
        _speechStr.setLength(0);
    }

    public String getSpeech(){
        if(_speechStr.length() > 0){
            return _speechStr.toString();
        }else{
            return "I(Player) have nothing to say";
        }
    }

    public void debugRemoveSeedsFromAllHouses(){
        for(House house: _houses){

            if(!house.isBuildingEmpty()){
                house.removeSeeds(house.getSeedCount());
            }

        }
    }

}
