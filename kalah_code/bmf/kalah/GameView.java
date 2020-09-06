package kalah;

import com.qualitascorpus.testsupport.IO;
import player.Player;

import java.util.ArrayList;

public class GameView {
    private ArrayList <StringBuilder> _strBuilders;
    private StringBuilder _wallStr, _borderStr, _player1Str, _player2Str, _seedsTens, _seedsOnes;
    private int _keyBoardInput;
    private IO _io;
    private Modes viewMode;
    private String _fencePiece, _fencePieceRev, _plus, _stick, _minus, _blank, _bracket, _bracketRev;

    public GameView(IO io, Modes viewMode) {
        this._io = io;
        this._strBuilders = new ArrayList<StringBuilder>();
        this.viewMode = viewMode;
    }

    public enum Modes {
        H_VIEW, V_VIEW
    }

    private void resetStringBuilders(){
        _wallStr.setLength(0);
        _borderStr.setLength(0);
        _player1Str.setLength(0);
        _player2Str.setLength(0);
    }

    private void resetSeedStringBuilders() {
        _seedsTens.setLength(0);
        _seedsOnes.setLength(0);
    }

    private void resetPlayerStringBuilders(){
        _player1Str.setLength(0);
        _player2Str.setLength(0);
    }

    private void generateP2Row(Player player1, Player player2){
        int houseSeeds, storeSeeds, tens, ones;

        _player2Str.append("| P2 ");
        for(int houseNo = player2.getHousesCount(); houseNo >= 1; houseNo--){

            resetSeedStringBuilders();
            houseSeeds = player2.getHouseSeedCount(houseNo);

            if(houseSeeds >= 0 && houseSeeds < 10){

                _seedsOnes.append(String.valueOf(houseSeeds));

                _player2Str.append("| " + houseNo +

                        "[ "+ _seedsOnes +"] ");

            }else if(houseSeeds >= 10 && houseSeeds < 100){

                tens = houseSeeds / 10;
                _seedsTens.append(String.valueOf(tens));
                ones = houseSeeds % 10;
                _seedsOnes.append(String.valueOf(ones));

                _player2Str.append("| " + houseNo +

                        "["+ _seedsTens +""+ _seedsOnes +"] ");
            }
        }

        storeSeeds = player1.getStoreSeedCount();
        if(storeSeeds < 10){
            _player2Str.append("|  "+storeSeeds+" |");
        }else{
            _player2Str.append("| "+storeSeeds+" |");
        }

        _io.println(_player2Str.toString());
    }

    private void generateP1Row(Player player1, Player player2){
        int houseSeeds, tens, ones, storeSeeds;

        storeSeeds = player2.getStoreSeedCount();

        if(storeSeeds < 10){
            _player1Str.append("|  "+storeSeeds+" ");
        }else{
            _player1Str.append("| "+storeSeeds+" ");
        }

        for(int houseNo = 1; houseNo <= player1.getHousesCount(); houseNo++){

            resetSeedStringBuilders();
            houseSeeds = player1.getHouseSeedCount(houseNo);

            if(houseSeeds >= 0 && houseSeeds < 10){

                _seedsOnes.append(String.valueOf(houseSeeds));

                _player1Str.append("| " + houseNo +

                        "[ "+ _seedsOnes +"] ");

            }else if(houseSeeds >= 10 && houseSeeds < 100){

                tens = houseSeeds / 10;
                _seedsTens.append(String.valueOf(tens));
                ones = houseSeeds % 10;
                _seedsOnes.append(String.valueOf(ones));

                _player1Str.append("| " + houseNo +

                        "["+ _seedsTens +""+ _seedsOnes +"] ");

            }
        }
        _player1Str.append("| P1 |");

        _io.println(_player1Str.toString());

    }

    private void generateBorder(int numHouses) {

        if (_borderStr.length() == 0) {
            _borderStr.append("+----");
            for (int i = 0; i < numHouses; i++) {
                _borderStr.append("+-------");
            }
            _borderStr.append("+----+");
        }

        _io.println(_borderStr.toString());

    }

    private void generateWall(int numHouses){

        if(_wallStr.length() == 0){
            _wallStr.append("|    |");
            for(int i = 0; i < numHouses; i++){
                _wallStr.append("-------+");
            }
            _wallStr.deleteCharAt(_wallStr.length()-1)
                    .insert(_wallStr.length(), "|    |");
        }

        _io.println(_wallStr.toString());
    }

    public void initStringBuilders(){

        _wallStr = new StringBuilder();
        _borderStr = new StringBuilder();
        _player1Str = new StringBuilder();
        _player2Str = new StringBuilder();
        _seedsOnes = new StringBuilder();
        _seedsTens = new StringBuilder();
        _fencePiece = "+-------";
        _fencePieceRev = "-------+";
        _plus = "+";
        _stick = "|";
        _minus = "-";
        _blank = " ";
        _bracket = "[";
        _bracketRev = "]";

        _strBuilders.add(_wallStr);
        _strBuilders.add(_borderStr);
        _strBuilders.add(_player1Str);
        _strBuilders.add(_player2Str);
        _strBuilders.add(_seedsOnes);
        _strBuilders.add(_seedsTens);
    }

    public int getUserInput(int playerID, int lastHouseNo){
        _keyBoardInput = _io.readInteger("Player P"+playerID+"'s turn - Specify house number or 'q' to quit: ",1, lastHouseNo, -1, "q");
        return _keyBoardInput;
    }

    public void printBoard(Player player1, Player player2){

        resetStringBuilders();

        switch (viewMode){

            case H_VIEW:
                generateBorder(player1.getHousesCount());
                generateP2Row(player1, player2);
                generateWall(player1.getHousesCount());
                generateP1Row(player1, player2);
                generateBorder(player1.getHousesCount());

                break;

            case V_VIEW:
                generateVBorder();
                generateVStore(player2);
                generateVWall();
                generateVHouses(player1, player2);
                generateVWall();
                generateVStore(player1);
                generateVBorder();

                break;

        }

    }

    public void printEndGame(Player player1, Player player2){
        int seedsP1 = 0, seedsP2 = 0;

        seedsP1 += player1.getStoreSeedCount() + player1.getAllHousesSeedCount();
        seedsP2 += player2.getStoreSeedCount() + player2.getAllHousesSeedCount();

        _io.println("\tplayer 1:" + seedsP1);
        _io.println("\tplayer 2:" + seedsP2);

        if(seedsP1 > seedsP2){
            _io.println("Player "+player1.getplayerID()+" wins!");
        }else if (seedsP1 == seedsP2){
            _io.println("A tie!");
        }else{
            _io.println("Player "+player2.getplayerID()+" wins!");
        }
    }

    public void printGameOver(){
        _io.println("Game over");
    }

    public void printHouseEmpty(){
        _io.println("House is empty. Move again.");
    }

    public void printDebugMode(){
        _io.println("######### DEBUG MODE #########");
    }

    private void generateVStore(Player player){

        int pID = player.getplayerID();
        int seedCnt = player.getStoreSeedCount();
        _io.print(_stick);

        if (pID == 1){

            _io.print(_blank);
            _io.print("P"+ pID);

            if(seedCnt < 10){
                _io.print(_blank + _blank);
            }else{
                _io.print(_blank);
            }

            _io.print(String.valueOf(seedCnt));
            _io.print(_blank);
            _io.print(_stick);

            for(int i = 0; i < _fencePiece.length() - 1; i++){
                _io.print(_blank);
            }

            _io.println(_stick);

        }else if(pID == 2){

            for(int i = 0; i < _fencePiece.length() - 1; i++){
                _io.print(_blank);
            }

            _io.print(_stick);
            _io.print(_blank);
            _io.print("P"+ pID);

            if(seedCnt < 10){
                _io.print(_blank + _blank);
            }else{
                _io.print(_blank);
            }

            _io.print(String.valueOf(seedCnt));
            _io.print(_blank);
            _io.println(_stick);
        }
    }

    private void generateVBorder(){

        if (_borderStr.length() == 0) {
            _borderStr.append(_fencePiece);
            _borderStr.append(_minus);
            _borderStr.append(_fencePieceRev);
        }

        _io.println(_borderStr.toString());
    }

    private void generateVWall(){
        if (_wallStr.length() == 0) {
            _wallStr.append(_fencePiece);
            _wallStr.append(_plus);
            _wallStr.append(_fencePieceRev);
        }

        _io.println(_wallStr.toString());
    }

    private void generateVHouses(Player player1, Player player2){

        int p1Houses = player1.getHousesCount();
        int p2Houses = player2.getHousesCount();
        int seedsP1, seedsP2;

        for(int i = 0; i < p1Houses ; i++){

            resetPlayerStringBuilders();

            seedsP1 = player1.getHouseSeedCount(i + 1);
            seedsP2 = player2.getHouseSeedCount(p2Houses - i);

            _player1Str.append(_stick + _blank + (i+1) + _bracket);

            if(seedsP1 < 10){
                _player1Str.append(_blank);
            }

            _player1Str.append(seedsP1 + _bracketRev + _blank + _stick);

            _player2Str.append(_blank + (p2Houses - i) + _bracket);

            if(seedsP2 < 10){
                _player2Str.append(_blank);
            }

            _player2Str.append(seedsP2 + _bracketRev + _blank + _stick);

            _io.print(_player1Str.toString());
            _io.println(_player2Str.toString());
        }

    }

    public Modes getViewMode() {
        return viewMode;
    }

    public void setViewMode(Modes viewMode) {
        this.viewMode = viewMode;
    }

    public void printPlayerSpeech(String speech){
        _io.println(speech);
    }
}
