package util.main;

import android.app.Application;

import androidx.room.Room;

import pt.ulisboa.tecnico.cmov.shopist.ServerInterface;
import util.db.DatabaseShopIst;


public class SharedClass extends Application {
    private static SharedClass sharedClass;
    private static DatabaseShopIst dbShopIst = null;

    public SharedClass () {
    }

    public DatabaseShopIst instanceDb() {
        if(dbShopIst == null) {
            dbShopIst = Room.databaseBuilder(this, DatabaseShopIst.class, "shopIstDb").fallbackToDestructiveMigration().build();
            //this.updateLocalDB();
        }
        return dbShopIst;
    }
    public void updateLocalDB() {
        ServerInterface.getInstance(this).getPantries();
    }
}
