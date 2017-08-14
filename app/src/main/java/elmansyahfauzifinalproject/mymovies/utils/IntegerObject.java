package elmansyahfauzifinalproject.mymovies.utils;


import io.realm.RealmObject;

/**
 * Created by ALLIANCES on 8/14/2017.
 */

public class IntegerObject extends RealmObject {
    public Integer integer;

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }
}
