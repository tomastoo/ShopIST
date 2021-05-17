package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "name", unique = true)})
public class Shop {

    public Shop (double latitude, double longitude, String name, long server_id){
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.server_id = server_id;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public long server_id;
    public double latitude;
    public double longitude;
    public String name;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public String time_stamp;
}
