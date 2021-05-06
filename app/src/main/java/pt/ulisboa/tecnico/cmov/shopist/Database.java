package pt.ulisboa.tecnico.cmov.shopist;

import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.main.SharedClass;

public class Database {

/*    public static void fillDatabase(){
        SharedClass sharedClass = new SharedClass();
        if (sharedClass.dbShopIst == null) sharedClass.instanceDb();
        //clearDatabase(sharedClass);

    }
*/
/*    private static void fillItems(SharedClass sharedClass){
        long[] itemIds= new long[]{
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ( "pao", 0.9f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("queijo",2.3f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("fiambre", 1.3f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("tomates", 3.3f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("alface", 1.4f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ( "gelado",5.6f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("bolo", 5.6f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("peixe", 5.6f)),
            sharedClass.dbShopIst.pantryDAO().insertItem(new Item ("gelado",5.6f))
        };


        long[] pantryIds = new long[] {
                sharedClass.dbShopIst.pantryDAO().insertPantry(new Pantry(38.73752054599338, -9.30328335632761, "Taguspark")),
                sharedClass.dbShopIst.pantryDAO().insertPantry(new Pantry(38.737020040649575, 38.737020040649575, "Alameda"))
        };

        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[0],1, 2));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[1],2, 1));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[2],3 ,0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[3],4 ,0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[4],2 , 0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[0], (int)itemIds[5],3 , 0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[1], (int)itemIds[6],1 , 0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[1], (int)itemIds[8],2 , 0));
        sharedClass.dbShopIst.pantryDAO().insertPantryList( new PantryItem((int)pantryIds[1],(int)itemIds[1],4 , 0));


        long[] shopIds = new long[]{
                sharedClass.dbShopIst.shopDAO().insertShop(new Shop(00004,00003, "Continente Sao Marcos")),
                sharedClass.dbShopIst.shopDAO().insertShop(new Shop(00003,00004, "Continente Alameda"))
        };



    }

    private static void clearDatabase(SharedClass sharedClass) {
        sharedClass.dbShopIst.pantryDAO().nukePantryItems();
        sharedClass.dbShopIst.shopDAO().nukeShops();
        sharedClass.dbShopIst.pantryDAO().nukeItems();
        sharedClass.dbShopIst.pantryDAO().nukePantries();

    }*/

}
