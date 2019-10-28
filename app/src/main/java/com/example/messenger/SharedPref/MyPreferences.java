package com.example.messenger.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {

    private static MyPreferences myPreferences;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * constructor to create a instance
     * @param context in where it is needed
     */
    private MyPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    /**
     * as it is a static method then it do not need a object to call and it will create a object of MyPreference
     * And if there is a object then that will not create another one return previous one
     * This type of class is called singleton class
     * @param context in where this object need to be created
     * @return the objected of shared preference
     */
    public static MyPreferences getPreferences(Context context) {
        if (myPreferences == null) myPreferences = new MyPreferences(context);
        return myPreferences;
    }

    /**
     * Initialize the titleTv of shared preference
     * @param userName titleTv of shared preference as String
     */
    public void setUserName(String userName){
        editor.putString(Config.USER_NAME, userName);
        editor.apply();
    }

    /**
     * get shared preference if it is already created
     * @return get titleTv of the existing shared preference
     */
    public String getUserName(){
        //if no data is available for Config.USER_NAME then this getString() method returns
        //a default value that is mentioned in second parameter
        return sharedPreferences.getString(Config.USER_NAME, "Name not found");
    }

    /**
     * it is only for one time use
     * initialize it
     * @param isStudent as boolean to handle one time use
     */
    public void setOneTimeUse(boolean isStudent){
        editor.putBoolean(Config.ONE_TIME_USE, isStudent);
        editor.apply();
    }

    /**
     * return the status
     * @return get the status true or false
     */
    public boolean getOneTimeUse(){
        return sharedPreferences.getBoolean(Config.ONE_TIME_USE, false); //assume the default value is false
    }

    public void setId(int id){
        editor.putInt(Config.ID, id);
        editor.apply();
    }
    public int getId(){
        return sharedPreferences.getInt(Config.ID, -1); //assume the default value is false
    }

}