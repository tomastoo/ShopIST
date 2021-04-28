package util.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {
    public Item (String name, float price) {
        this.name = name;
        this.price = price;
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public float price;
}
