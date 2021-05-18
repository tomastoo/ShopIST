package util.db.queryInterfaces;

public class ShopItem {
    public long id;
    public String name;
    public String quantity;
    public String barcode;
    public Boolean inManyPantryLists;

    public ShopItem(String name, String quantity ,String barcode, Boolean inManyPantryLists) {
        this.name = name;
        this.quantity = quantity;
        this.barcode = barcode;
        this.inManyPantryLists = inManyPantryLists;

    }


    public ShopItem(long id,String name, String quantity ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        inManyPantryLists = false;
    }
}