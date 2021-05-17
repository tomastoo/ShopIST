package util.main;

import android.app.Application;
import android.provider.Settings;

import androidx.room.Room;

import util.ServerSync.ServerInterface;
import util.db.DatabaseShopIst;


public class SharedClass extends Application {

    private static DatabaseShopIst dbShopIst = null;

    public DatabaseShopIst instanceDb() {
        if(dbShopIst == null) {
            dbShopIst = Room.databaseBuilder(this, DatabaseShopIst.class, "shopIstDb").fallbackToDestructiveMigration().build();
            //this.updateLocalDB();
        }
        return dbShopIst;
    }

    public void updateLocalDB() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ServerInterface.getInstance(this).getPantries();
        ServerInterface.getInstance(this).getShops();
    }

}
