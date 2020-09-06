package player;

import kalah.House;
import kalah.Store;

import java.util.ArrayList;

public class Robot extends Player{

    public Robot(int playerID){
        super(playerID);
        this._isRobot = true;
    }

    public boolean makeMove(Player enemyPlayer) {

        int seedsTaken, houseNo;
        boolean isExtraTurn = false;
        boolean isWrap = false;
        ArrayList<House> enemyHouses = enemyPlayer._houses;

        if(_dummyHouses == null && _dummyEnemyHouses == null && _dummyStore == null){
            acquireDummyBoard();
        }

        resetSpeechString();

        houseNo = checkExtraMove(enemyHouses);

        if(houseNo == -1){
            houseNo = checkCapture(enemyHouses);

            if(houseNo == -1){
                houseNo = checkLowestHouse();
                _speechStr.append("Player P"+_playerID+" (Robot) chooses house #"+houseNo+" because it is the first legal move");
            }else{
                _speechStr.append("Player P"+_playerID+" (Robot) chooses house #"+houseNo+" because it leads to a capture");
            }

        }else{
            _speechStr.append("Player P"+_playerID+" (Robot) chooses house #"+houseNo+" because it leads to an extra move");
        }

        seedsTaken = _houses.get(houseNo - 1).getSeedCount();
        _houses.get(houseNo - 1).removeSeeds(seedsTaken);

        while(seedsTaken > 0){

            if (houseNo == _houses.size()) {

                _store.plantSeeds(1);
                seedsTaken--;

                if(seedsTaken == 0){
                    isExtraTurn = true;
                }else{
                    seedsTaken = plantSeedsEnemyRow(seedsTaken, enemyHouses);
                    houseNo = _houses.get(0).gethouseNo();
                    isWrap = true;
                }

            } else {

                if(!isWrap) houseNo++;

                seedsTaken = plantSeedsOwnRow(houseNo, seedsTaken, enemyHouses);
                houseNo = _houses.size();

            }

        }

        return isExtraTurn;
    }

    private int checkExtraMove(ArrayList<House> enemyHouses){

        int seedsTaken;
        int currentHouseNo;
        int lastHouseNo;
        boolean isExtraMove = false;

        lastHouseNo = _houses.get(_houses.size()-1).gethouseNo();
        currentHouseNo = -1;

        for(House house: _houses){

            currentHouseNo = house.gethouseNo();
            seedsTaken = house.getSeedCount();

            if (seedsTaken > (_houses.size() + enemyHouses.size() + 1)){

                seedsTaken -= _houses.size() + enemyHouses.size() + 1;

            }

            if(seedsTaken == ((lastHouseNo - currentHouseNo) + 1)){
                isExtraMove = true;
                break;
            }

        }

        if(!isExtraMove) currentHouseNo = -1;

        return currentHouseNo;
    }

    private int checkCapture(ArrayList<House> enemyHouses){

        int seedsTaken;
        int currentHouseNo, nextHouseNo;
        int isCapture;
        int status[];
        boolean isWrap;

        currentHouseNo = -1;
        isCapture = 0;

        for(House house: _dummyHouses) {

            if(isCapture == 1){
                break;
            }else{
                isCapture = 0;
            }

            isWrap  = false;

            updateDummyBoard(enemyHouses);

            if(house.isBuildingEmpty()) continue;

            currentHouseNo = house.gethouseNo();
            nextHouseNo = currentHouseNo;
            seedsTaken = house.getSeedCount();

            house.removeSeeds(seedsTaken);

            while(seedsTaken > 0){

                if (nextHouseNo == _dummyHouses.size()) {

                    _dummyStore.plantSeeds(1);
                    seedsTaken--;

                    if(seedsTaken == 0){
                        continue;
                    }else{
                        seedsTaken = plantSeedsEnemyDummyRow(seedsTaken);
                        nextHouseNo = _dummyHouses.get(0).gethouseNo();
                        isWrap = true;
                    }

                } else {

                    if(!isWrap) nextHouseNo = currentHouseNo + 1;

                    status = plantSeedsOwnDummyRow(nextHouseNo, seedsTaken);

                    seedsTaken = status[0];
                    isCapture = status[1];

                    if(seedsTaken > 0) {
                        nextHouseNo = _dummyHouses.size();
                    }

                }
            }
        }

        if(isCapture == 0) currentHouseNo = -1;

        return currentHouseNo;
    }

    private int checkLowestHouse(){

        int currentHouseNo = 1;

        for(House house:_houses){
            if(!house.isBuildingEmpty()) {
                currentHouseNo = house.gethouseNo();
                break;
            }
        }

        return currentHouseNo;
    }

    private void updateDummyBoard(ArrayList<House> enemyHouses){
        House dummyHouse, dummyEnemyHouse, originalHouse, originalEnemyHouse;

        for(int i = 0; i < _houses.size(); i++){

            dummyHouse = _dummyHouses.get(i);
            dummyEnemyHouse = _dummyEnemyHouses.get(i);
            originalHouse = _houses.get(i);
            originalEnemyHouse = enemyHouses.get(i);

            if(!dummyHouse.isBuildingEmpty()){
                dummyHouse.removeSeeds(dummyHouse.getSeedCount());
            }

            if(!dummyEnemyHouse.isBuildingEmpty()){
                dummyEnemyHouse.removeSeeds(dummyEnemyHouse.getSeedCount());
            }

            if(!_dummyStore.isBuildingEmpty()){
                _dummyStore.removeSeeds(_dummyStore.getSeedCount());
            }

            if(!originalHouse.isBuildingEmpty()){
                dummyHouse.plantSeeds(originalHouse.getSeedCount());
            }

            if(!originalEnemyHouse.isBuildingEmpty()){
                dummyEnemyHouse.plantSeeds(originalEnemyHouse.getSeedCount());
            }

            if(!_store.isBuildingEmpty()){
                _dummyStore.plantSeeds(_store.getSeedCount());
            }

        }
    }

    private int[] plantSeedsOwnDummyRow(int startHouseNo, int seedsTaken){

        boolean isEnemyEmpty, isPlayerEmpty;
        int seedsNextHouse, enemyHouseNo, enemyHouseSeeds, isCapture;
        int status[] = new int[2];

        isCapture = 0;

        for (int i = (startHouseNo - 1); i < _dummyHouses.size(); i++) {

            if(seedsTaken == 0) {
                break;
            }else{
                seedsNextHouse = _dummyHouses.get(i).getSeedCount();

                if((seedsNextHouse == 0) && (seedsTaken == 1)){

                    enemyHouseNo = _dummyEnemyHouses.get(_dummyEnemyHouses.size() - i - 1).gethouseNo();
                    isEnemyEmpty = _dummyEnemyHouses.get(enemyHouseNo-1).isBuildingEmpty();
                    isPlayerEmpty = _dummyHouses.get(i).isBuildingEmpty();

                    if(!(isEnemyEmpty) && isPlayerEmpty){
                        enemyHouseSeeds = _dummyEnemyHouses.get(enemyHouseNo-1).getSeedCount();
                        _dummyEnemyHouses.get(enemyHouseNo-1).removeSeeds(enemyHouseSeeds);
                        _dummyStore.plantSeeds(seedsTaken+enemyHouseSeeds);
                        isCapture = 1;
                    }else{
                        _dummyHouses.get(i).plantSeeds(1);
                    }

                }else{
                    _dummyHouses.get(i).plantSeeds(1);
                }

                seedsTaken--;
            }

        }

        status[0] = seedsTaken;
        status[1] = isCapture;

        return status;
    }

    private int plantSeedsEnemyDummyRow(int seedsTaken){

        for(int houseIndex = 0; houseIndex < _dummyEnemyHouses.size(); houseIndex++){

            if(seedsTaken == 0) {
                break;
            }else{
                _dummyEnemyHouses.get(houseIndex).plantSeeds(1);
                seedsTaken--;
            }
        }

        return seedsTaken;
    }

    private void acquireDummyBoard(){

        this._dummyHouses = new ArrayList<House>();
        this._dummyEnemyHouses = new ArrayList<House>();

        House dummyHouse, enemyDummyHouse;

        for(int i = 1; i <= _houses.size(); i++){
            dummyHouse = new House(i);
            enemyDummyHouse = new House(i);
            _dummyHouses.add(dummyHouse);
            _dummyEnemyHouses.add(enemyDummyHouse);
        }

        _dummyStore = new Store();

    }

}
