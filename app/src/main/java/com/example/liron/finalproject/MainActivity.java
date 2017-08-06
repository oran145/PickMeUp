package com.example.liron.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.liron.finalproject.Dialogs.DateDialogFragment;
import com.example.liron.finalproject.Dialogs.MyProgressBar;
import com.example.liron.finalproject.fragments.MainFragment;
import com.example.liron.finalproject.fragments.RegisterFragment;
import com.example.liron.finalproject.fragments.SignInFragment;
import com.example.liron.finalproject.model.Model;
import com.example.liron.finalproject.model.User;

public class MainActivity extends Activity {

    private FragmentTransaction ftr;
    int workingFragment;//tells which fragment is now in use
    MyProgressBar progressBar;
    Model.LoginListener loginListener;

    MainFragment mainFragment;
    SignInFragment signInFragment;
    RegisterFragment registerFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=new MyProgressBar(this);

        //constracting the fragments in this activity
        mainFragment=new MainFragment();
        signInFragment=new SignInFragment();
        registerFragment=new RegisterFragment();


        workingFragment= Constants.LOGIN_MAIN_FRAGMENT;

        //display the fragment using fragment manager
        ftr = getFragmentManager().beginTransaction();
        //add to the screen
        ftr.add(R.id.main_container,mainFragment);


        //show fragment
        ftr.show(mainFragment);
        ftr.commit();

        loginListener= new Model.LoginListener() {
            @Override
            public void showProgressBar() {
                progressBar.showProgressDialog();
            }

            @Override
            public void hideProgressBar() {
                progressBar.hideProgressDialog();
            }

            @Override
            public void makeToastAuthFailed() {
                Toast.makeText(MainActivity.this, R.string.auth_failed,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void makeToastVerifyEmail(String msg)
            {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean validateFormInRegister() {
                return registerFragment.validateForm();
            }

            @Override
            public boolean validateFormInSignIn() {
                return signInFragment.validateForm();
            }


            @Override
            public Activity getActivity() {
                return MainActivity.this;
            }

            @Override
            public void printToLogWarning(String tag, String msg, Throwable tr) {
                Log.w(tag, msg,tr);
            }

            @Override
            public void printToLogMessage(String tag, String msg) {
                Log.d(tag,msg);
            }

            @Override
            public void printToLogException(String tag, String msg, Throwable tr) {
                Log.e(tag,msg,tr);
            }

            @Override
            public void goToChatActivity() {
                Intent intent = new Intent(MainActivity.this, PickMeAppActivity.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void updateRegisterActivityIfSuccess() {
                registerFragment.enableOrDisableButtons(true,true);
                registerFragment.enableAllTextFields(false);
                registerFragment.changeRegisterButtonText();
            }

        };


        mainFragment.setDelegate(new MainFragment.Delegate() {
            @Override
            public void onSignInPressed() {
                ftr = getFragmentManager().beginTransaction();

                workingFragment=Constants.LOGIN_SIGN_IN_FRAGMENT;
                //add to the screen
                ftr.add(R.id.main_container,signInFragment);
                ftr.hide(mainFragment);
                ftr.show(signInFragment);
                ftr.addToBackStack("main");
                ftr.commit();
            }

            @Override
            public void onRegisterPressed() {
                ftr = getFragmentManager().beginTransaction();

                workingFragment=Constants.LOGIN_REGISTER_FRAGMENT;
                //add to the screen
                ftr.add(R.id.main_container,registerFragment);
                ftr.hide(mainFragment);
                ftr.show(registerFragment);
                ftr.addToBackStack("main");
                ftr.commit();
            }
        });

        registerFragment.setDelegate(new RegisterFragment.Delegate() {
            @Override
            public void onDateSet() {
                DateDialogFragment dialog = new DateDialogFragment();
                dialog.setOnDateSetListener(new DateDialogFragment.OnDateSetLisetener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {
                        registerFragment.setTextInBirthday("" + dayOfMonth + "/" + monthOfYear + "/" + year);
                    }
                });
                dialog.show(getFragmentManager(),"TAG");
            }

            @Override
            public void onRegisterButtonClick(User user) {
                if(registerFragment.getRegisterBtnText().equals("Register"))
                {
                    Model.getInstance().addUser(user,loginListener);
                }
                else
                {
                    Model.getInstance().signInAfterRegister(user.getEmail(),user.getPassword(),loginListener);
                }


            }

            @Override
            public void onVerifyEmailClick(final User user) {
                //problem that if verify not seccessed!!!!!!
                Model.getInstance().verifyEmail(loginListener);

            }
        });

        signInFragment.setDelegate(new SignInFragment.Delegate() {
            @Override
            public void onSignInPressed(String email, String password) {
                Model.getInstance().signIn(email,password,loginListener);
            }
        });

    }
}
