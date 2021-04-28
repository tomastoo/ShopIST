package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "shop_items",
        foreignKeys = {
        @ForeignKey(entity = Shop.class, parentColumns = "id" , childColumns = "shopId"),
        @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "itemId")
})
public class ShopItems {
    public ShopItems (int shopId,int itemId, int quantity) {
        this.itemId = itemId;
        this.shopId = shopId;
        this.quantity = quantity;
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(index = true)
    public int shopId;

    @ColumnInfo(index = true)
    public int itemId;

    public int quantity;
}
