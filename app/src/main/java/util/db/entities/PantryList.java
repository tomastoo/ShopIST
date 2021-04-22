package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import util.db.entities.Item;
import util.db.entities.Pantry;

@Entity(tableName = "pantry_list",
        foreignKeys = {
        @ForeignKey(entity = Pantry.class, parentColumns = "id" , childColumns = "pantryId"),
        @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "itemId")
})
public class PantryList {
    @PrimaryKey
    public int id;

    @ColumnInfo(index = true)
    public int pantryId;

    @ColumnInfo(index = true)
    public int itemId;

    public int quantity;
    public int stock;
}
