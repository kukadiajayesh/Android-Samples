package app.com.appfirebase;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by Reena on 8/4/2017.
 */

public class MyApplication extends Application {

    private static FirebaseDatabase firebaseDatabase;
    private static FirebaseStorage firebaseStorage;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static FirebaseDatabase getFirebaseDatabase() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }
        return firebaseDatabase;
    }

    public static FirebaseStorage getFirebaseStorage() {
        if (firebaseStorage == null) {
            firebaseStorage = FirebaseStorage.getInstance();
        }
        return firebaseStorage;
    }


}
