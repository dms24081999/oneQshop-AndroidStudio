package com.dominicsilveira.one_q_shop.ui.scan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.RegisterLogin.SplashScreen;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.ErrorMessage;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductBarCodes;
import com.dominicsilveira.one_q_shop.jsonschema2pojo_classes.Product.ProductDetails;
import com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity;
import com.dominicsilveira.one_q_shop.ui.product.ProductDetailsActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.api.RestClient;
import com.dominicsilveira.one_q_shop.utils.api.RestMethods;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class ScanFragment extends Fragment {

    Boolean isProductDialogOpen=false;
    SurfaceView image;
    RelativeLayout cameraOn,cameraOff;
    Button turnOnCameraBtn,btn_camera;
    String barCodeValue="";
    ProductBarCodes productBarCodes;
    RestMethods restMethods;

    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        initComponents(root);
        updateUI();
        addListeners();

        return root;
    }


    private void initComponents(View root) {
        image=root.findViewById(R.id.image);
        cameraOn=root.findViewById(R.id.cameraOn);
        cameraOff=root.findViewById(R.id.cameraOff);
        turnOnCameraBtn=root.findViewById(R.id.turnOnCameraBtn);
        btn_camera=root.findViewById(R.id.btn_camera);
        cameraOn.setVisibility(View.GONE);
        cameraOff.setVisibility(View.GONE);

        //Builds HTTP Client for API Calls
        restMethods = RestClient.buildHTTPClient();

        SharedPreferences sh = getActivity().getSharedPreferences("ProductBarCodes", MODE_PRIVATE);// The value will be default as empty string because for the very first time when the app is opened, there is nothing to show
        Gson gson = new Gson();
        String json = sh.getString("barcodesObj", "");// We can then use the data
        productBarCodes = gson.fromJson(json, ProductBarCodes.class);
        Log.i("ScanFragment", String.valueOf(productBarCodes.getResults().size()));
    }

    private void addListeners() {
        turnOnCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updateUI() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            cameraOn.setVisibility(View.VISIBLE);
            cameraOff.setVisibility(View.GONE);
            createCameraSource();
        }else{
            cameraOn.setVisibility(View.GONE);
            cameraOff.setVisibility(View.VISIBLE);
        }
    }

    private void askCameraPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
        }else{
            requestPermissions(new String[]
                    {Manifest.permission.CAMERA},AppConstants.CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.CAMERA_REQUEST_CODE) {// If request is cancelled, the result arrays are empty.
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) { // permission was granted, yay! Do the contacts-related task you need to do.
                Log.e(String.valueOf(getActivity().getClass()),"Permission Granted!");
                Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                updateUI();
            } else { // permission denied, boo! Disable the functionality that depends on this permission.
                Log.e(String.valueOf(getActivity().getClass()),"Permission Denied!");
                Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                updateUI();
            }
        }
    }

    private void createCameraSource() {
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity()).build();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        Log.d("Metrics","Metrics Preview width and height="+screenWidth+" "+screenHeight);
        final CameraSource cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(screenWidth, screenHeight)
                .build(); //.setRequestedPreviewSize(1600, 1024)
        image.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(image.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodeSparseArray=detections.getDetectedItems();
                if(barcodeSparseArray.size()>0 && !isProductDialogOpen) {
                    barCodeValue = barcodeSparseArray.valueAt(0).displayValue;
                    Log.i("Barcode/QR-code value:", barCodeValue);
                    if (productBarCodes.getResults().containsKey(barCodeValue)) {
                        getProductDetails(productBarCodes.getResults().get(barCodeValue));
//                        Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
//                        intent.putExtra("BARCODE_VALUE",productBarCodes.getResults().get(barCodeValue));
//                        startActivity(intent);
                    }
                }else{
                    barCodeValue="";
                }
            }
        });
    }

    private void getProductDetails(final Integer product_id) {
//        https://stackoverflow.com/a/9815528
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "Product Found!", Toast.LENGTH_SHORT).show();
                if(!isProductDialogOpen){
                    isProductDialogOpen=true;
                    Map<String, String> data = new HashMap<>();
                    Call<ProductDetails> getProductBarCodes = restMethods.getProductDetails(product_id,data);
                    getProductBarCodes.enqueue(new Callback<ProductDetails>() {
                        @Override
                        public void onResponse(Call<ProductDetails> call, Response<ProductDetails> response) {
                            Toast.makeText(getActivity(), response.code() + " ", Toast.LENGTH_SHORT).show();
                            if (response.isSuccessful()) {
                                Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(response.code()));
                                showProductDialog(response.body());
                            } else {
                                isProductDialogOpen=false;
                                Toast.makeText(getActivity(), "Request failed!", Toast.LENGTH_SHORT).show();
                                Gson gson = new Gson();
                                ErrorMessage error=gson.fromJson(response.errorBody().charStream(),ErrorMessage.class);
                                Log.i(String.valueOf(getActivity().getComponentName().getClassName()), String.valueOf(error.getMessage()));
                            }
                        }
                        @Override
                        public void onFailure(Call<ProductDetails> call, Throwable t) {
                            isProductDialogOpen=false;
                            Toast.makeText(getActivity(), "Request failed", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void showProductDialog(ProductDetails productDetails) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_product);

        TextView productNamePop=dialog.findViewById(R.id.productNamePop);
        TextView brandNamePop=dialog.findViewById(R.id.brandNamePop);
        TextView pricePop=dialog.findViewById(R.id.pricePop);
        ImageView productImagePop=dialog.findViewById(R.id.productImagePop);
        LinearLayout categoryTagsPop=dialog.findViewById(R.id.categoryTagsPop);

        productNamePop.setText(productDetails.getName());
        pricePop.setText(productDetails.getPrice());
        Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(productImagePop);
        for(final CategoriesDetails categoriesDetails:productDetails.getCategoriesDetails()){
            View categoryView = getLayoutInflater().inflate(R.layout.include_round_chips, null);
            Button chipName=categoryView.findViewById(R.id.chipName);
            chipName.setText(categoriesDetails.getName());
            chipName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(), ProductCategoriesActivity.class);
                    intent.putExtra("CATEGORY_ID",categoriesDetails.getId());
                    intent.putExtra("CATEGORY_NAME",categoriesDetails.getName());
                    startActivity(intent);
                }
            });
            categoryTagsPop.addView(categoryView);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // dialog dismiss without button press
                isProductDialogOpen=false;
            }
        });
    }
}