package com.example.liron.finalproject.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.liron.finalproject.Constants;
import com.example.liron.finalproject.MyAppContext;
import com.example.liron.finalproject.R;
import com.example.liron.finalproject.Utilities.utilities;
import com.example.liron.finalproject.Utility;
import com.example.liron.finalproject.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    ImageView contactPhotoIV;
    EditText birthdayET;
    EditText fNameET;
    EditText lNameET;
    EditText emailET;
    EditText passwordET;

    //buttons
    Button verifyEmailBtn;
    Button registerBtn;

    //for dialog of camera or gallery open
    String userChoosenTask;

    public RegisterFragment() {}

    public interface Delegate{
        void onDateSet();
        void onRegisterButtonClick(User user);
        void onVerifyEmailClick(User user);
    }

    Delegate delegate;
    public void setDelegate(Delegate dlg){
        this.delegate = dlg;
    }

    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_register, container, false);
        contactPhotoIV=(ImageView)view.findViewById(R.id.fragment_register_image_imageView);
        birthdayET=(EditText)view.findViewById(R.id.fragment_register_birthday_editText);
        fNameET=(EditText)view.findViewById(R.id.fragment_register_fName_editText);
        lNameET=(EditText)view.findViewById(R.id.fragment_register_lName_editText);
        emailET=(EditText)view.findViewById(R.id.fragment_register_email_editText);
        passwordET=(EditText)view.findViewById(R.id.fragment_register_password_editText);

        verifyEmailBtn=(Button)view.findViewById(R.id.fragment_register_verifyEmail_btn);
        registerBtn=(Button)view.findViewById(R.id.fragment_register_btn);

        verifyEmailBtn.setEnabled(false);

        contactPhotoIV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    selectImage();
                }
                return true;
            }
        });


        birthdayET.setInputType(0);
        birthdayET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    delegate.onDateSet();
                }
                return true;
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                {
                    return;
                }
                delegate.onRegisterButtonClick(setUserDetails());



            }
        });
        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                {
                    return;
                }
                delegate.onVerifyEmailClick(setUserDetails());

            }
        });

        return view;
    }

    public void setTextInBirthday(String date)
    {
        birthdayET.setText(date);
    }
    public User setUserDetails()
    {
        Bitmap userImage=((BitmapDrawable)contactPhotoIV.getDrawable()).getBitmap();
        String fName=fNameET.getText().toString();
        String lName=lNameET.getText().toString();
        long birthday=utilities.convertDateStringToMillis(birthdayET.getText().toString());
        String email=emailET.getText().toString();
        String password=passwordET.getText().toString();

        return new User(null,userImage,null,fName,lName,birthday,email,password,0,0);
    }

    public boolean validateForm()
    {
        boolean valid = true;

        //checking first name field
        String fName = fNameET.getText().toString();
        if (TextUtils.isEmpty(fName)) {
            fNameET.setError("Required.");
            valid = false;
        } else {
            fNameET.setError(null);
        }

        //checking last name field
        String lName = lNameET.getText().toString();
        if (TextUtils.isEmpty(lName)) {
            lNameET.setError("Required.");
            valid = false;
        } else {
            lNameET.setError(null);
        }

        //checking birthday field
        String birthday = birthdayET.getText().toString();
        if (TextUtils.isEmpty(birthday)) {
            birthdayET.setError("Required.");
            valid = false;
        } else {
            birthdayET.setError(null);
        }


        //checking email field
        String email = emailET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required.");
            valid = false;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Invalid Email");
            valid = false;
        }
        else {
            emailET.setError(null);
        }

        //checking password field
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

    public void enableAllTextFields(boolean enable)
    {
        contactPhotoIV.setEnabled(enable);
        birthdayET.setEnabled(enable);
        fNameET.setEnabled(enable);
        lNameET.setEnabled(enable);
        emailET.setEnabled(enable);
        passwordET.setEnabled(enable);
    }
    public void enableOrDisableButtons(boolean enableRegiter,boolean enableVerify)
    {
        registerBtn.setEnabled(enableRegiter);
        verifyEmailBtn.setEnabled(enableVerify);

    }

    public void changeRegisterButtonText()
    {
        registerBtn.setText(getString(R.string.sign_in));
    }
    public String getRegisterBtnText()
    {
        return registerBtn.getText().toString();
    }


    //----------functions which handle selection of image from camera or gallery---------------

    /**
     * building dialog to chose image from camera shooting or from gallery
     */
    private void selectImage() {
        userChoosenTask=null;
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),Constants.SELECT_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == Constants.REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(MyAppContext.getAppContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        contactPhotoIV.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        Uri tempUri = getImageUri(MyAppContext.getAppContext(), thumbnail);
        Bitmap rotatedThumbnail=null;

        try {
            rotatedThumbnail=rotateImageIfRequired(thumbnail,tempUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        contactPhotoIV.setImageBitmap(rotatedThumbnail);

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //rotating the image if needed
    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

}
