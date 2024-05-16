package com.example.project2;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

public class UserClass implements Parcelable {
    private String FirstName;
    private String LastName;
    private String UserName;
    private String Email;
    private String Password;

    private int MaxWeightSquant;
    private int MaxWeightBench;
    private int MaxWeightDeadlift;
    private int BestTimeMile;





    public UserClass(String firstName, String lastName, String userName, String email, String password, int maxWeightSquant, int maxWeightBench, int maxWeightDeadlift, int bestTimeMile) {
        FirstName = firstName;
        LastName = lastName;
        UserName = userName;
        setEmail(email);
        setPassword(password);
        setMaxWeightSquant(maxWeightSquant);
        setMaxWeightBench(maxWeightBench);
        setMaxWeightDeadlift(maxWeightDeadlift);
        setBestTimeMile(bestTimeMile);

    }

    public UserClass(String firstName, String lastName, String userName, String email, String password) {
        FirstName = firstName;
        LastName = lastName;
        UserName = userName;
        setEmail(email);
        setPassword(password);
    }

    public void SetUserClass(String firstName, String lastName, String userName, String email, String password) {
        FirstName = firstName;
        LastName = lastName;
        UserName = userName;
        setEmail(email);
        setPassword(password);

    }

    public UserClass() {

    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        final Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid Email");
        } else {
            Email = email;
        }
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {

        final Pattern pattern = Pattern.compile("^[a-z]+[A-Z]+[0-9]+\\w{5,}$");
        if (!pattern.matcher(password).matches() && !password.equals("admin")) {
            throw new IllegalArgumentException("Invalid Password");
        } else {
            Password = password;
        }

    }


    public int getMaxWeightSquant() {
        return MaxWeightSquant;
    }

    public void setMaxWeightSquant(int maxWeightSquant) {

        if (maxWeightSquant < 0) {
            throw new IllegalArgumentException("Can not be negative");
        } else {
            MaxWeightSquant = maxWeightSquant;
        }

    }

    public int getMaxWeightBench() {
        return MaxWeightBench;
    }

    public void setMaxWeightBench(int maxWeightBench) {

        if (maxWeightBench < 0) {
            throw new IllegalArgumentException("Can not be negative");
        } else {
            MaxWeightBench = maxWeightBench;
        }

    }

    public int getMaxWeightDeadlift() {
        return MaxWeightDeadlift;
    }

    public void setMaxWeightDeadlift(int maxWeightDeadlift) {
        if (maxWeightDeadlift < 0) {
            throw new IllegalArgumentException("Can not be negative");
        } else {
            MaxWeightDeadlift = maxWeightDeadlift;
        }

    }

    public int getBestTimeMile() {
        return BestTimeMile;
    }

    public void setBestTimeMile(int bestTimeMile) {

        if (bestTimeMile < 0) {
            throw new IllegalArgumentException("Can not be negative");
        } else {
            BestTimeMile = bestTimeMile;
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(UserName);
        dest.writeString(Email);
        dest.writeString(Password);
        dest.writeInt(MaxWeightSquant);
        dest.writeInt(MaxWeightBench);
        dest.writeInt(MaxWeightDeadlift);
        dest.writeInt(BestTimeMile);

    }

    public static final Parcelable.Creator<UserClass> CREATOR
            = new Parcelable.Creator<UserClass>() {
        public UserClass createFromParcel(Parcel in) {
            return new UserClass(in);
        }

        public UserClass[] newArray(int size) {
            return new UserClass[size];
        }
    };

    private UserClass(Parcel in) {
        FirstName = in.readString();
        LastName = in.readString();
        UserName = in.readString();
        Email = in.readString();
        Password = in.readString();
        MaxWeightSquant = in.readInt();
        MaxWeightBench = in.readInt();
        MaxWeightDeadlift = in.readInt();
        BestTimeMile = in.readInt();

    }
}
