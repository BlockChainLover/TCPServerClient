import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Mounica on 12/25/15.
 */
public class storeManager {
    private ConcurrentHashMap<Integer, Integer> store;
    private LogManager logger = LogManager.getInstance();

    public storeManager(){
        this.store = new ConcurrentHashMap<Integer, Integer>();
    }

    public void put(Integer key, Integer val){
        store.put(key, val);
    }

    public Integer get(Integer key){
        return store.get(key);
    }

    public void delete(Integer key){

            store.remove(key);

    }

}
