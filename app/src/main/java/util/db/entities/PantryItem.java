package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        foreignKeys = {
        @ForeignKey(entity = Pantry.class, parentColumns = "id" , childColumns = "pantryId"),
        @ForeignKey(entity = Shop.class, parentColumns = "id", childColumns = "shopId")
})
public class PantryItem {
    public PantryItem (int pantryId, int shopId, int quantity, int stock , String name) {
        this.shopId = shopId;
        this.pantryId = pantryId;
        this.quantity = quantity;
        this.stock = stock;
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;

    public float price;

    public String barcode;

    @ColumnInfo(index = true)
    public int pantryId;

    @ColumnInfo(index = true)
    public int shopId;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "stock")
    public int stock;

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    public String time_stamp;
}
