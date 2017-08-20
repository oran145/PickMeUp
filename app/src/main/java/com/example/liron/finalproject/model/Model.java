package com.example.liron.finalproject.model;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by liron on 25-Jul-17.
 */

public class Model {
    ModelFirebase modelFirebase=new ModelFirebase();
    ModelSql modelSql = new ModelSql();

    final private static Model instance = new Model();
    private Model() {}

    public static Model getInstance()
    {
        return instance;
    }

    public void addHitchhiker(String rideID)
    {
        modelFirebase.addHitchhiker(rideID);
    }

    public String getCurrentUserId() {
        return modelFirebase.getCurrentUserId();
    }

    public void removeHitchhiker(String rideID)
    {
        modelFirebase.removeHitchhiker( rideID);
    }

    public void removeRide(String rideID)
    {
        modelSql.removeRide(rideID);
        modelFirebase.removeRide(rideID);
    }

    public interface LoginListener{
        /**
         * showing progress bar
         */
        void showProgressBar();

        /**
         * hide progress bar
         */
        void hideProgressBar();

        /**
         * make toast with the text:Authentication failed
         * (the message have to be declared inside activity,that why there are no parameters declared inside firebase class)
         */
        void makeToastAuthFailed();

        /**
         * make toast for verify email in firebase with message declared in firebase class
         * @param msg message to show in the toast
         */
        void makeToastVerifyEmail(String msg);

        /**
         * valide the form of register activity
         * @return true if the form is legit false otherwise
         */
        boolean validateFormInRegister();

        /**
         * valide the form of sighIn activity
         * @return true if the form is legit false otherwise
         */
        boolean validateFormInSignIn();

        /**
         * getting the activity that all the things running inside it
         * @return ActivityMain
         */
        Activity getActivity();

        /**
         * printing to log warning
         * @param tag tag to print
         * @param msg message message to print
         * @param tr the stack trace of warning
         */
        void printToLogWarning(String tag,String msg,Throwable tr);

        /**
         * rinting message to log
         * @param tag tag to print
         * @param msg message to print
         */
        void printToLogMessage(String tag,String msg);

        /**
         * printing to log Exception
         * @param tag tag to print
         * @param msg message message to print
         * @param tr the stack trace of warning
         */
        void printToLogException(String tag,String msg,Throwable tr);

        /**
         * update UI and go to the next activity (Chat Activity)
         */
        void goToChatActivity();

        /**
         * if the registration worked,update the buttons(register button disabled and verify email enabled)
         */
        void updateRegisterActivityIfSuccess();
    }

    public interface saveUserLocalAndRemote
    {
        void saveUserToLocal(User user);
        void saveUserToRemote(User user);
    }

    public interface GetAllRidesListener
    {
        void onComplete(ArrayList<Ride> ridesList);
        Context getAppContext();
        void showProgressBar();
        void hideProgressBar();
    }

    public interface GetAllUsersListener
    {
        void onComplete(ArrayList<User> usersList);
        Context getAppContext();
        void showProgressBar();
    }

    public interface SaveRideListener
    {
        void showProgressBar();
        void hideProgressBar();
    }

    public void getAllRidesRemote(final GetAllRidesListener listener)
    {
        modelFirebase.getAllUsers(new GetAllUsersListener() {
            @Override
            public void onComplete(ArrayList<User> usersList)
            {
                modelSql.writeUsersFromFireBase(usersList);
                modelFirebase.getAllRides(new GetAllRidesListener() {
                    @Override
                    public void onComplete(ArrayList<Ride> ridesList)
                    {
                        modelSql.writeDataFromFireBase(ridesList,listener);
                    }
                    @Override
                    public Context getAppContext() {
                        return listener.getAppContext();
                    }

                    @Override
                    public void showProgressBar() {
                        listener.showProgressBar();
                    }

                    @Override
                    public void hideProgressBar() {
                        listener.hideProgressBar();
                    }
                });

            }

            @Override
            public Context getAppContext() {
                return listener.getAppContext();
            }

            @Override
            public void showProgressBar() {
                listener.showProgressBar();
            }

        });
    }

    public void addUser(User user, final LoginListener listener)
    {
        Model.saveUserLocalAndRemote sular=new saveUserLocalAndRemote() {
            @Override
            public void saveUserToLocal(User user) {
                //modelSql.addCurrentUser(user);
            }

            @Override
            public void saveUserToRemote(User user) {
                modelFirebase.addUser(user,listener);
            }
        };
        modelFirebase.createAccount(user,listener,sular);

    }

    public void verifyEmail(LoginListener listener)
    {
        modelFirebase.sendEmailVerification(listener);
    }

    public interface SignInListener
    {
        void changeIsSignedInRemote(String userId,boolean isSignedIn);
        void changeIsSignedInLocal(String userId,int isSignedIn);
    }
    public void signIn(String email,String password,LoginListener listener)
    {
        Model.SignInListener dataBaseListener=new SignInListener() {
            @Override
            public void changeIsSignedInRemote(String userId,boolean isSignedIn) {
                modelFirebase.changeUserSignInStatus(userId,isSignedIn);
            }

            @Override
            public void changeIsSignedInLocal(String userId,int isSignedIn) {
                //modelSql.changeUserSignInStatus(userId,isSignedIn);
            }
        };
        modelFirebase.signIn(email,password,listener,dataBaseListener);

    }
    public void signInAfterRegister(String email,String password,LoginListener listener)
    {
        Model.SignInListener dataBaseListener=new SignInListener() {
            @Override
            public void changeIsSignedInRemote(String userId,boolean isSignedIn) {
                modelFirebase.changeUserSignInStatus(userId,isSignedIn);
            }

            @Override
            public void changeIsSignedInLocal(String userId,int isSignedIn) {
                //modelSql.changeUserSignInStatus(userId,isSignedIn);
            }
        };
        modelFirebase.signInAfterRegister(email,password,listener,dataBaseListener);
    }

    public void addRide(Ride r, SaveRideListener saveRideListener)
    {
        modelFirebase.addRide(r,saveRideListener);
    }


}