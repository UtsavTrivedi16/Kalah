package kalah;

public class House extends KalahBuilding {
    private final int _houseNo;

    public House(int _houseNo){
        super();
        this._houseNo = _houseNo;
    }

    public int gethouseNo() {
        return _houseNo;
    }

}
