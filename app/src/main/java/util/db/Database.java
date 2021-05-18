package util.db;

import util.db.DatabaseShopIst;
import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.db.queryInterfaces.PantryDAO;
import util.main.SharedClass;

public class Database {

    public static void fillDatabase(SharedClass sharedClass){
        PantryDAO pantryDAO = sharedClass.instanceDb().pantryDAO();

        long[] shopIds = new long[]{
                pantryDAO.insertShop(new Shop(38.73452478161277, -9.133309761369768, "Continente Sao Marcos", 1)),
                pantryDAO.insertShop(new Shop(38.72865628809242, -9.127079691360107, "Lidl Alameda", 2))
        };

        long[] pantryIds = new long[] {
                pantryDAO.insertPantry(new Pantry(38.73752054599338, -9.30328335632761, "Taguspark", 1)),
                pantryDAO.insertPantry(new Pantry(38.73700329289796, -9.138705001720002, "Alameda", 2))
        };

        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],1, 2, "pao", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],2, 1, "queijo", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],3 ,0, "fiambre", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],4 ,0, "tomates", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],2 , 0, "alface", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[0], (int)shopIds[0],3 , 0,  "gelado", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[1], (int)shopIds[1],1 , 0, "bolo", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[1], (int)shopIds[1],2 , 0, "peixe", null));
        pantryDAO.insertPantryItem( new PantryItem((int)pantryIds[1],(int)shopIds[1],4 , 0, "gelado", null));

    }

    public static void clearDatabase(SharedClass sharedClass) {
        PantryDAO pantryDAO = sharedClass.instanceDb().pantryDAO();

        pantryDAO.nukePantryItems();
        pantryDAO.nukeShops();
        pantryDAO.nukePantries();

    }

}
