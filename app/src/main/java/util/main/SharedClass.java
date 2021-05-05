package util.main;

import android.app.Application;

import androidx.room.Room;

import pt.ulisboa.tecnico.cmov.shopist.ServerInterface;
import util.db.DatabaseShopIst;


public class SharedClass extends Application {
    public static DatabaseShopIst dbShopIst = null;
    public static ServerInterface serverInterface = null;
    public SharedClass () {
    }

    public void instanceDb() {
        dbShopIst =  Room.databaseBuilder(this, DatabaseShopIst.class, "shopIstDb").fallbackToDestructiveMigration().build();
    }

    public void instanceServerCommunication() {

        ServerInterface.getInstance(this).updateShops();
    }
}
