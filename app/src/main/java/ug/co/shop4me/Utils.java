package ug.co.shop4me;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kaelyn on 23/11/2016.
 */

public class Utils {
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

}