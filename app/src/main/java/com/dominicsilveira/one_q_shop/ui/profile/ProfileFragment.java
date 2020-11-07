package com.dominicsilveira.one_q_shop.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.classes.Users;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    LinearLayout personalDetailsBtn,changePasswordBtn,aboutMeBtn,logoutBtn,upiDetailsBtn;
    TextView nameText;
    Users userObj;
    AppConstants globalClass;
    CircularImageView profileImage;
    RestMethods restMethods;
    private Uri mCropImageUri;

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
        logoutBtn = root.findViewById(R.id.logoutBtn);
        nameText = root.findViewById(R.id.nameText);
        personalDetailsBtn = root.findViewById(R.id.personalDetailsBtn);
        changePasswordBtn = root.findViewById(R.id.changePasswordBtn);
        aboutMeBtn = root.findViewById(R.id.aboutMeBtn);
        upiDetailsBtn = root.findViewById(R.id.upiDetailsBtn);
        nameText.setText(userObj.getUsername());
        profileImage = root.findViewById(R.id.profileImage);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        String imageURL=AppConstants.BACKEND_URL.concat(userObj.getPicturePath());
        Log.i("IMAGE_URL",imageURL);
        setBitmapFromURL(imageURL);

        profileImage.setOnClickListener(new View.OnClickListener() {
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
    
    public void setBitmapFromURL(String src) {
        Call<ResponseBody> req = restMethods.getImageFile(src);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // display the image data in a ImageView or save it
                        Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                        profileImage.setImageBitmap(bmp);
                        // profileImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_baseline_account_circle_000000_24));
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO
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
                profileImage.setImageURI(result.getUri());
                Toast.makeText(getActivity(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
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
}