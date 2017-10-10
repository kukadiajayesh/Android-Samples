package app.com.appfirebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Reena on 8/4/2017.
 */

@IgnoreExtraProperties
public class User {

    public String name, profile,mobile;
    public long birthdate;

    public User() {
    }


    public User(String name, String profile, String mobile, long birthdate) {
        this.name = name;
        this.profile = profile;
        this.mobile = mobile;
        this.birthdate = birthdate;
    }
}
