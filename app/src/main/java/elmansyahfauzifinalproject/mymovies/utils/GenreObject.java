package elmansyahfauzifinalproject.mymovies.utils;

import io.realm.RealmObject;

/**
 * Created by ALLIANCES on 8/28/2017.
 */

public class GenreObject extends RealmObject {


    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
