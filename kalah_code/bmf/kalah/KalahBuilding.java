package kalah;

import java.util.ArrayList;

public class KalahBuilding extends GameObject{

    protected ArrayList<Seed> seeds;

    public KalahBuilding(){
        super();
        this.seeds = new ArrayList<Seed>();
    }

    public void plantSeeds(int seeds){

        if(seeds <= 0){
            throw new IndexOutOfBoundsException("Failed! Cannot Plant Zero or Negative Seeds");
        } else{
            for(int i = 0; i < seeds; i++){
                this.seeds.add(new Seed());
            }
        }
    }

    public void removeSeeds(int seeds){

        if(seeds <= 0){
            throw new IndexOutOfBoundsException("Failed! Cannot remove Zero or Negative Seeds");
        }else{
            for(int i = (this.seeds.size() - 1); i >= 0; i--){
                this.seeds.remove(i);
            }
        }
    }

    public int getSeedCount(){
        seeds.trimToSize();
        return seeds.size();
    }

    public boolean isBuildingEmpty(){
        return seeds.isEmpty();
    }

}
