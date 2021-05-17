package util.db.queryInterfaces;

public class ShopItem {
    public String name;
    public String quantity;
    public Boolean inManyPantryLists;

    public ShopItem(String name, String quantity , Boolean inManyPantryLists) {
        this.name = name;
        this.quantity = quantity;
        this.inManyPantryLists = inManyPantryLists;

    }


    public ShopItem(String name, String quantity ) {
        this.name = name;
        this.quantity = quantity;
        inManyPantryLists = false;
    }
}