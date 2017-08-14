package com.example.liron.finalproject.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liron on 25-Jul-17.
 */

public class ModelFirebase {

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private User currentuser;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    //----firebase storage and realtime databse-------
    private FirebaseDatabase database;//firebase databse reference

    private StorageReference mStorageRef;//firebase storage reference(read and write images)

    ChildEventListener childEventListener;


    public ModelFirebase() {
        //------------Authentication------------
        mAuth = FirebaseAuth.getInstance();

        //initialize the FirebaseAuth instance and the AuthStateListener method so you can track whenever the user signs in or out.
        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
//                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
        //-----------End Authentication------------
        //--------------firebase database---------------
        database= FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    /**
     * sets a listenr wich indicates if the user is signed in
     */
    public void addAuthStateListener(final Model.LoginListener listener)
    {
        //add a listener which indicates weather the usr is authonticated or not.
        //if the user was authonticated previously,Firebase will try to renew its session with the roundtrip to the server
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void removeAuthStateListener()
    {
        if (mAuthListener != null) {
            //add a listener which indicates weather the usr is authonticated or not.
            //if the user was authonticated previously,Firebase will try to renew its session with the roundtrip to the server
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    /**
     * creates account in Firebase Authentication with email and password
     * @param user the user we take from the password and email,and insert the new token
     * @param listener the listener that gives us the functionality of the view in the firebase class
     */
    public void createAccount(final User user, final Model.LoginListener listener,final Model.saveUserLocalAndRemote sular) {

        listener.printToLogMessage(TAG, "createAccount:" + user.getEmail());
        if (!listener.validateFormInRegister()) {
            return;
        }

        listener.showProgressBar();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(listener.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.printToLogMessage(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            listener.makeToastAuthFailed();
                            listener.hideProgressBar();
                        }
                        else
                        {
                            listener.updateRegisterActivityIfSuccess();

                            //now adding user to loacl and remote database

                            //enter the token that we got from firebase into userID
                            user.setUserID(mAuth.getCurrentUser().getUid());

                            //enter the image name(not absolute path)
                            String imageName=user.getUserID()+"_"+System.currentTimeMillis();
                            user.setImageUrl(imageName);

                            sular.saveUserToLocal(user);
                            sular.saveUserToRemote(user);
                        }


                    }
                });
        // [END create_user_with_email]


    }

    /**
     * send email verification to verify user email
     * @param listener listener which generates functions from the Activity
     */
    public void sendEmailVerification(final Model.LoginListener listener) {

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser fbUser = mAuth.getCurrentUser();
        //first checking that we make account
        if (fbUser != null) {
            fbUser.sendEmailVerification()
                    .addOnCompleteListener(listener.getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // [START_EXCLUDE]
                            // Re-enable button

                            if (task.isSuccessful()) {
                                listener.makeToastVerifyEmail("Verification email sent to " + fbUser.getEmail());
                            } else {
                                listener.printToLogException(TAG, "sendEmailVerification", task.getException());
                                listener.makeToastVerifyEmail("Failed to send verification email.");
                            }
                            // [END_EXCLUDE]
                        }
                    });
        }
        else
        {
            listener.makeToastVerifyEmail("Can't verify Email before making account");
        }
        // [END send_email_verification]
    }

    public void signIn(String email, String password, final Model.LoginListener listener, final Model.SignInListener databseListener) {
        listener.printToLogMessage(TAG, "signIn:" + email);
        if (!listener.validateFormInSignIn()) {
            return;
        }

        listener.showProgressBar();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.printToLogMessage(TAG,"signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            listener.printToLogWarning(TAG, "signInWithEmail:failed", task.getException());
                            listener.makeToastAuthFailed();

                        }
                        else
                        {
                            databseListener.changeIsSignedInLocal(mAuth.getCurrentUser().getUid(),1);
                            databseListener.changeIsSignedInRemote(mAuth.getCurrentUser().getUid(),true);
                            listener.goToChatActivity();
                        }
                        listener.hideProgressBar();

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

//    public void signOut(final Model.SignInListener databseListener,Model.ChatListener chatListener) {
//
//        chatListener.showProgressBar();
//        databseListener.changeIsSignedInLocal(mAuth.getCurrentUser().getUid(),0);
//        databseListener.changeIsSignedInRemote(mAuth.getCurrentUser().getUid(),false);
//        mAuth.signOut();
//        chatListener.hideProgressBar();
//        chatListener.goToMainActivity();
//
//    }

    public void signInAfterRegister(String email, String password,final Model.LoginListener listener, final Model.SignInListener databseListener)
    {
        //you have to sign out  and then sign in,in order to get the new email verified status
        mAuth.signOut();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        listener.printToLogMessage(TAG,"signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            listener.printToLogWarning(TAG, "signInWithEmail:failed", task.getException());
                            listener.makeToastAuthFailed();

                        }
                        FirebaseUser fbUser=mAuth.getCurrentUser();
                        if(fbUser!=null)
                        {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                databseListener.changeIsSignedInLocal(mAuth.getCurrentUser().getUid(),1);
                                databseListener.changeIsSignedInRemote(mAuth.getCurrentUser().getUid(),true);
                                listener.goToChatActivity();//moved to changeStatus in firebase
                            }
                            else
                            {

                                listener.makeToastVerifyEmail("Please verify email first before signing in");
                                //signOut();

                            }

                        }
                        listener.hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]

    }



    public void addUser(User user,final Model.LoginListener viewlistener)
    {
        //saves the image of the user and the other details
        saveImage(user,viewlistener);
    }

    /**
     * save the image of the current user and then (if succeded)save the other details of the user with the image path(got from storege)
     * @param user the user to store in databse
     * @param viewlistener the listener that gives you functions activated from activity
     */
    private void saveImage(final User user,final Model.LoginListener viewlistener){

        Bitmap imageBmp=user.getUserImage();
        String imageName=user.getImageUrl();

        StorageReference imagesRef = mStorageRef.child("images").child(imageName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                viewlistener.printToLogMessage("Tag","Fail to save image to firebase storage");
                viewlistener.hideProgressBar();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") String imagePath = taskSnapshot.getDownloadUrl().toString();

                viewlistener.printToLogMessage("TAG","Image was saved to Firebase storage");

                //saving user deatails to storage
                HashMap<String, Object> result = new HashMap<>();
                result.put("id",user.getUserID());
                result.put("imageUrl",imagePath);
                result.put("firstName",user.getFirstName());
                result.put("lastName",user.getLastName());
                result.put("birthday",user.getBirthday());
                result.put("isSignedIn",user.translateIsSignedInToBool());
                result.put("lastUpdated",user.getLastUpdated());


                DatabaseReference myRef = database.getReference("users").child(user.getUserID());
                myRef.setValue(result);

                viewlistener.hideProgressBar();

            }
        });
    }

    public void changeUserSignInStatus(String userId,boolean isSignedIn)
    {//put in the user row id...the isSignedIn=true
        database.getReference("users").child(userId).child("isSignedIn").setValue(isSignedIn);
    }

    public void addRide(final Ride ride, final Model.SaveRideListener saveListener)
    {

        String ownerId = mAuth.getCurrentUser().getUid();

        database.getReference("users").child(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User currentuser = new User();
                currentuser.setUserID(dataSnapshot.child("id").getValue().toString());
                currentuser.setImageUrl(dataSnapshot.child("imageUrl").getValue().toString());
                currentuser.setFirstName(dataSnapshot.child("firstName").getValue().toString());
                currentuser.setLastName(dataSnapshot.child("lastName").getValue().toString());
                currentuser.setBirthday((long)dataSnapshot.child("birthday").getValue());

                insertRideToDb(currentuser,ride);
                saveListener.hideProgressBar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void insertRideToDb(User currentuser , Ride ride)
    {
        HashMap<String, Object> result = new HashMap<>();
        if(currentuser != null)
        {
            result.put("rideOwner", currentuser);
        }

        ArrayList<String> hitchhikers = new ArrayList<String>();

        hitchhikers.add("");

        result.put("rideDate",ride.getDate());
        result.put("rideTime",ride.getTime());
        result.put("from",ride.getFrom());
        result.put("to",ride.getTo());
        result.put("freeSeats",ride.getFreeSeats());
        result.put("hitchhikers",hitchhikers);


        DatabaseReference myRef = database.getReference("ride").push();
        result.put("rideID",myRef.getKey());
        myRef.setValue(result);

    }


    public void getAllRides(final Model.GetAllRidesListener listener) {
//        listener.showProgressBar();

        DatabaseReference myRef = database.getReference("ride");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    //getting one ride
                    final Ride ride = new Ride();

                    User currentuser = new User();
                    currentuser.setUserID(dataSnapshot.child("rideOwner").child("userID").getValue().toString());
                    currentuser.setImageUrl(dataSnapshot.child("rideOwner").child("imageUrl").getValue().toString());
                    currentuser.setFirstName(dataSnapshot.child("rideOwner").child("firstName").getValue().toString());
                    currentuser.setLastName(dataSnapshot.child("rideOwner").child("lastName").getValue().toString());
                    currentuser.setBirthday((long)dataSnapshot.child("rideOwner").child("birthday").getValue());

                    ride.setRideOwner(currentuser);
                    ride.setDate((long)dataSnapshot.child("rideDate").getValue());
                    ride.setTime((long)dataSnapshot.child("rideTime").getValue());
                    ride.setFrom(dataSnapshot.child("from").getValue().toString());
                    ride.setTo(dataSnapshot.child("to").getValue().toString());
                    ride.setFreeSeats((long)dataSnapshot.child("freeSeats").getValue());
                    ride.setRideID(dataSnapshot.child("rideID").getValue().toString());
                    ride.setHitchhikers((ArrayList<String>)dataSnapshot.child("hitchhikers").getValue());

                    String absoluteImageName = dataSnapshot.child("rideOwner").child("imageUrl").getValue().toString();

                    String imageName = absoluteImageName.substring(absoluteImageName.indexOf(ride.getRideOwner().getUserID()), absoluteImageName.indexOf("?"));
                    ride.getRideOwner().setImageUrl(imageName);

                    Glide.with(listener.getAppContext())
                            .load(absoluteImageName)
                            .asBitmap()
                            .toBytes()
                            .centerCrop()
                            .into(new SimpleTarget<byte[]>(60, 60) {
                                @Override
                                public void onResourceReady(byte[] data, GlideAnimation anim) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ride.getRideOwner().setUserImage(bitmap);

                                    listener.onComplete(ride);
                                    listener.hideProgressBar();
                                }
                            });
                }
                else
                {
                    Log.d("bla", "doesn't found");
//                    listener.hideProgressBar();
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addHitchhiker(String rideID ,final Model.updateListener listener) {
        DatabaseReference myRef = database.getReference("ride").child(rideID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                long freeSteats = (long) dataSnapshot.child("freeSeats").getValue();
                freeSteats--;
                dataSnapshot.child("freeSeats").getRef().setValue(freeSteats);
                ArrayList<String> hitchhikers = (ArrayList<String>) dataSnapshot.child("hitchhikers").getValue();
                hitchhikers.add(mAuth.getCurrentUser().getUid());
                dataSnapshot.child("hitchhikers").getRef().setValue(hitchhikers);
                listener.onUpdate();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public String getCurrentUserId() {
        return mAuth.getCurrentUser().getUid();
    }

    public void removeHitchhiker(String rideID, final Model.updateListener updateListener)
    {
        DatabaseReference myRef = database.getReference("ride").child(rideID);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                long freeSteats = (long) dataSnapshot.child("freeSeats").getValue();
                freeSteats++;
                dataSnapshot.child("freeSeats").getRef().setValue(freeSteats);
                ArrayList<String> hitchhikers = (ArrayList<String>) dataSnapshot.child("hitchhikers").getValue();
                hitchhikers.remove(mAuth.getCurrentUser().getUid());
                dataSnapshot.child("hitchhikers").getRef().setValue(hitchhikers);
                updateListener.onUpdate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeRide(String rideID, final Model.updateListener updateListener)
    {
        database.getReference("ride").child(rideID).removeValue();
        updateListener.onUpdate();
    }
}