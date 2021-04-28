package util.db.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "name", unique = true)})
public class Shop {

    public Shop (float latitude, float longitude, String name){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public float latitude;
    public float longitude;
    public String name;
}
