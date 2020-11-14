package com.dominicsilveira.one_q_shop.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dominicsilveira.one_q_shop.MainActivity;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.one_q_shop.utils.CallbackUtils;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.gson.Gson;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.User.User;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements CallbackUtils.AsyncResponse{
    LinearLayout personalDetailsBtn,changePasswordBtn,aboutMeBtn,logoutBtn,upiDetailsBtn;
    TextView nameText;
    User userObj;
    AppConstants globalClass;
    CircularImageView userAvatar;
    RestMethods restMethods;
    private Uri mCropImageUri;
    String token;
    CallbackUtils callbackUtils;
    BasicUtils basicUtils=new BasicUtils();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        initComponents(root);
        attachListeners();

        return root;
    }

    private void initComponents(View root) {
        globalClass=(AppConstants)getActivity().getApplicationContext();
        userObj=globalClass.getUserObj();
        callbackUtils =new CallbackUtils(getActivity().getApplicationContext(), ProfileFragment.this);


        logoutBtn = root.findViewById(R.id.logoutBtn);
        nameText = root.findViewById(R.id.nameText);
        personalDetailsBtn = root.findViewById(R.id.personalDetailsBtn);
        changePasswordBtn = root.findViewById(R.id.changePasswordBtn);
        aboutMeBtn = root.findViewById(R.id.aboutMeBtn);
        upiDetailsBtn = root.findViewById(R.id.upiDetailsBtn);
        nameText.setText(userObj.getUsername());
        userAvatar = root.findViewById(R.id.userAvatar);

        SharedPreferences sh = getActivity().getSharedPreferences("TokenAuth", Context.MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        token=sh.getString("token", "0");// We can then use the data

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

//        callbackUtils.setBitmapFromURL(userObj.getPicturePath());
        if(userObj.getPicturePath()!=null)
            Picasso.get().load(AppConstants.BACKEND_URL.concat(userObj.getPicturePath())).into(userAvatar);

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromGallery();
            }
        });

    }

    private void attachListeners() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                getActivity().stopService(new Intent(getActivity(), MyParkingService.class));
//                AlarmUtils.cancelAllAlarms(getActivity(),new Intent(getActivity(), NotificationReceiver.class));
                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                getActivity().finish();
            }
        });

        personalDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PersonalDetailsActivity.class));
//                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out); //push from top to bottom
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

//        upiDetailsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), UpiDetailsActivity.class));
//            }
//        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        aboutMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }


    private void pickImageFromGallery() {
        CropImage.startPickImageActivity(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("ProfileFragment","CROP_IMAGE");
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {// handle result of pick image chooser
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {// For API >= 23 we need to check specifically that we have permissions to read external storage.
                mCropImageUri = imageUri;// request permissions and handle the result in onRequestPermissionsResult()
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCropImageActivity(imageUri);// no permissions required or already grunted, can start crop image activity
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {// handle result of CropImageActivity
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri newImg=result.getUri();
                userAvatar.setImageURI(newImg);
                Bitmap bitmap=basicUtils.uriToBitmap(getActivity(),newImg);
                globalClass.setUserProfilePic(bitmap);
                ((MainActivity)getActivity()).callbackMethod(bitmap);
                uploadProfilePic(newImg);
//                Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadProfilePic(Uri uri) {
        File file=new File(uri.getPath());
        RequestBody reqFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture",
                file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "picture");
        Call<User> req = restMethods.postProfileImage(userObj.getId(),token,body, name);
        req.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if(response.code()==200){
                        Toast.makeText(getActivity(), "Updated Details!", Toast.LENGTH_SHORT).show();
                        globalClass.setUserObj(response.body());
                    }else{
                        Toast.makeText(getActivity(), "Request failed!", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                        Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(error.getMessage()));
                    }
                }else {
                    try {
                        Toast.makeText(getActivity(), "Request failed!", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                        Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(error.getMessage()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropImageUri);// required permissions granted, start crop image activity
        } else {
            Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }

    @Override
    public void callbackMethod(Bitmap output) {
        Log.e("ProfileFragment","Callback from utils");
        if(output==null){
            userAvatar.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_account_circle_000000_24));
        }else{
            userAvatar.setImageBitmap(output);
        }
    }
}