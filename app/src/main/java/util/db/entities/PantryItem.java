package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import util.db.entities.Item;
import util.db.entities.Pantry;

@Entity(
        foreignKeys = {
        @ForeignKey(entity = Pantry.class, parentColumns = "id" , childColumns = "pantryId"),
        @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "itemId")
})
public class PantryItem {
    public PantryItem (int pantryId,int itemId, int quantity, int stock) {
        this.itemId = itemId;
        this.pantryId = pantryId;
        this.quantity = quantity;
        this.stock = stock;
    }
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(index = true)
    public int pantryId;

    @ColumnInfo(index = true)
    public int itemId;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "stock")
    public int stock;

}
