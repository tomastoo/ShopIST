package util.main;

import android.app.Application;

import androidx.room.Room;

import util.db.DatabaseShopIst;


public class SharedClass extends Application {
    public int teste=1;
    public DatabaseShopIst dbShopIst;

    public SharedClass () {
    }

    public void instanceDb() {
        dbShopIst =  Room.databaseBuilder(this, DatabaseShopIst.class, "shopIstDb").build();
    }
}
