package com.dominicsilveira.one_q_shop.ui.profile;

import android.Manifest;
import android.app.Activity;
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
import androidx.fragment.app.Fragment;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.RegisterLogin.LoginActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.User.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment implements ApiListener {
    static String TAG = ProfileFragment.class.getSimpleName();
    LinearLayout personalDetailsBtn,changePasswordBtn,aboutMeBtn,logoutBtn,upiDetailsBtn;
    TextView nameText;
    User userObj;
    AppConstants globalClass;
    CircularImageView userAvatar;
    RestApiMethods restMethods;
    Uri mCropImageUri;
    String token;
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
        token=BasicUtils.getSharedPreferencesString(getActivity(),"TokenAuth","token","0");
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls
        userObj=globalClass.getUserObj();

        logoutBtn = root.findViewById(R.id.logoutBtn);
        nameText = root.findViewById(R.id.nameText);
        personalDetailsBtn = root.findViewById(R.id.personalDetailsBtn);
        changePasswordBtn = root.findViewById(R.id.changePasswordBtn);
        aboutMeBtn = root.findViewById(R.id.aboutMeBtn);
        nameText.setText(userObj.getUsername());
        userAvatar = root.findViewById(R.id.userAvatar);

        if(userObj.getPicturePath()!=null)
            Picasso.get().load(AppConstants.BACKEND_URL.concat(userObj.getPicturePath())).into(userAvatar);

    }

    private void attachListeners() {
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(getContext(), ProfileFragment.this);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("TokenAuth", MODE_PRIVATE);
                preferences.edit().remove("token").apply();
                Toast.makeText(getActivity(), "Logout Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"CROP_IMAGE");
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
        ApiResponse.callRetrofitApi(req, RestApiMethods.postProfileImageRequest, ProfileFragment.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.postProfileImageRequest)) {
            if(status==200){
                User user = (User) data;
                globalClass.setUserObj(user);
                Toast.makeText(getActivity(), "Updated Profile Pic!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
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