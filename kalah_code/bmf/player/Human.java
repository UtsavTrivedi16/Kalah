package player;

import kalah.House;

import java.util.ArrayList;

public class Human extends Player{

    public Human(int playerID){
        super(playerID);
        this._isRobot = false;
    }

    public boolean makeMove(int houseNo, Player enemyPlayer) {
        int seedsTaken;
        boolean isExtraTurn = false;
        boolean isWrap = false;
        ArrayList<House> enemyHouses = enemyPlayer._houses;

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

}
