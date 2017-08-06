package com.example.liron.finalproject.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.liron.finalproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {


    private EditText emailET;
    private EditText passwordET;
    private Button signInBtn;


    public interface Delegate{
        void onSignInPressed(String email,String password);
    }

    Delegate delegate;
    public void setDelegate(Delegate dlg){
        this.delegate = dlg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //making the layout match the fragment activity
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailET=(EditText)view.findViewById(R.id.fragment_signIn_email_editText);
        passwordET=(EditText)view.findViewById(R.id.fragment_signIn_password_editText);

        signInBtn=(Button)view.findViewById(R.id.fragment_signIn_btn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                {
                    return;
                }
                delegate.onSignInPressed(emailET.getText().toString(),passwordET.getText().toString());
            }
        });


        return view;
    }
    public boolean validateForm()
    {
        boolean valid = true;

        String email = emailET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        } else {
            emailET.setError(null);
        }

        String password = passwordET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required.");
            valid = false;
        }
        else if(password.length()<6)
        {
            passwordET.setError("Mininum 6 Characters.");
            valid = false;
        }
        else {
            passwordET.setError(null);
        }

        return valid;
    }

}
