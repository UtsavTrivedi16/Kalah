package kalah;

public class GameObject {

    private static Integer _ID = 0;
    protected Integer gameObjectID = 0;

    public GameObject(){
        _ID++;
        gameObjectID = _ID;
    }

    public Integer getObjectID(){
        return new Integer(gameObjectID);
    }

}
