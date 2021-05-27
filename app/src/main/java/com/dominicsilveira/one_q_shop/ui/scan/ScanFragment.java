package com.dominicsilveira.one_q_shop.ui.scan;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.dominicsilveira.one_q_shop.R;
import com.dominicsilveira.one_q_shop.ui.product.ProductCategoriesActivity;
import com.dominicsilveira.one_q_shop.ui.product.ProductDetailsActivity;
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.dominicsilveira.one_q_shop.utils.BasicUtils;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiListener;
import com.dominicsilveira.oneqshoprestapi.api_calls.ApiResponse;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Cart.CartDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.MiniCartDetails;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiClient;
import com.dominicsilveira.oneqshoprestapi.rest_api.RestApiMethods;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.CategoriesDetails;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductBarCodes;
import com.dominicsilveira.oneqshoprestapi.pojo_classes.Product.ProductDetails;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;

public class ScanFragment extends Fragment implements ApiListener {
    static String TAG = ScanFragment.class.getSimpleName();
    Boolean isProductDialogOpen=false;
    SurfaceView image;
    RelativeLayout cameraOn,cameraOff;
    Button turnOnCameraBtn,btn_camera;
    String barCodeValue="",token;
    ProductBarCodes productBarCodes;
    RestApiMethods restMethods;
    Dialog productDialog;
    FloatingActionButton cart_decrease, cart_increase,open_in_new_tab;
    AppCompatButton add_to_cart, update_cart, remove_from_cart;
    LinearLayout in_cart, not_in_cart,categoryTagsPop;
    TextView productNamePop,brandNamePop,pricePop,cart_count;
    ImageView productImagePop;
    MiniCartDetails miniCartDetails;
    ProductDetails productDetails;
    AppConstants globalClass;
    Integer product_id;

    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void askPermissions() {
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            requestPermissions(PERMISSIONS, AppConstants.SCAN_PERMISSION_ALL);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);

        initComponents(root);
        initProductDialog();
        updateUI();
        addListeners();
        return root;
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        updateUI();
        Log.i(TAG,"on Scan fragment resume");
        super.onResume();
        //Refresh your stuff here
    }

    private void initComponents(View root) {
        globalClass=(AppConstants)getActivity().getApplicationContext();
        token=BasicUtils.getSharedPreferencesString(getActivity(),"TokenAuth","token","0");
        restMethods = RestApiClient.buildHTTPClient();//Builds HTTP Client for API Calls

        image=root.findViewById(R.id.image);
        cameraOn=root.findViewById(R.id.cameraOn);
        cameraOff=root.findViewById(R.id.cameraOff);
        turnOnCameraBtn=root.findViewById(R.id.turnOnCameraBtn);
        cameraOn.setVisibility(View.GONE);
        cameraOff.setVisibility(View.GONE);

        Gson gson = new Gson();
        productBarCodes = gson.fromJson(BasicUtils.getSharedPreferencesString(getActivity(),"ProductBarCodes","barcodesObj",""), ProductBarCodes.class);
//        Log.i(TAG, String.valueOf(productBarCodes.getResults().size()));
    }

    private void addListeners() {
        turnOnCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermissions();
            }
        });

        cart_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(cart_count.getText().toString());
                if (qty > 1) {
                    miniCartDetails.setCount(--qty);
                    cart_count.setText(qty+"");
                }
            }
        });

        cart_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(cart_count.getText().toString());
                if (qty < productDetails.getCount()) {
                    miniCartDetails.setCount(++qty);
                    cart_count.setText(qty+"");
                }
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUpdateCartDetailsAPI();
            }
        });
        update_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadUpdateCartDetailsAPI();
            }
        });
        remove_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<CartDetails> req = restMethods.deleteCartDetails(token,miniCartDetails.getId());
                ApiResponse.callRetrofitApi(req, RestApiMethods.deleteCartDetailsRequest, ScanFragment.this);
            }
        });
        open_in_new_tab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productDialog.dismiss();
                Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("PRODUCT_DETAILS", productDetails);
                intent.putExtra("BARCODE_VALUE", productDetails.getBarcode());
                startActivity(intent);
            }
        });
    }

    private void loadUpdateCartDetailsAPI() {
        Call<CartDetails> req = restMethods.updateCartDetails(token,globalClass.getUserObj().getId(),productDetails.getId(),miniCartDetails.getCount());
        ApiResponse.callRetrofitApi(req, RestApiMethods.updateCartDetailsRequest, ScanFragment.this);
    }

    private void initProductDialog() {
        productDialog = new Dialog(getActivity());
        productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        productDialog.setContentView(R.layout.include_product_card_dialog);
        productNamePop=productDialog.findViewById(R.id.productNamePop);
        brandNamePop=productDialog.findViewById(R.id.brandNamePop);
        pricePop=productDialog.findViewById(R.id.pricePop);
        productImagePop=productDialog.findViewById(R.id.productImagePop);
        categoryTagsPop=productDialog.findViewById(R.id.categoryTagsPop);
        cart_decrease=productDialog.findViewById(R.id.cart_decrease);
        cart_increase=productDialog.findViewById(R.id.cart_increase);
        cart_count=productDialog.findViewById(R.id.cart_count);
        open_in_new_tab=productDialog.findViewById(R.id.open_in_new_tab);
        add_to_cart=productDialog.findViewById(R.id.add_to_cart);
        update_cart=productDialog.findViewById(R.id.update_cart);
        remove_from_cart=productDialog.findViewById(R.id.remove_from_cart);
        not_in_cart=productDialog.findViewById(R.id.not_in_cart);
        in_cart=productDialog.findViewById(R.id.in_cart);
    }

    private void showProductDialog() {
        productDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        productDialog.setCancelable(true);
        productDialog.show();
        productDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isProductDialogOpen=false;// dialog dismiss without button press
            }
        });
    }

    private void populateProductDialog() {
        productNamePop.setText(productDetails.getName());
        brandNamePop.setText(productDetails.getBrandDetails().getName());
        pricePop.setText("₹ ".concat(productDetails.getPrice()));
        if(productDetails.getCartDetails()==null){
            updateNotInCartAndCartCount();
        }else{
            miniCartDetails=productDetails.getCartDetails();
            updateInCartAndCartCount(productDetails.getCartDetails().getCount());
        }

        if(AppConstants.IS_AWS) Picasso.get().load(productDetails.getImagesDetails().get(0).getImage()).into(productImagePop);
        else Picasso.get().load(AppConstants.BACKEND_URL.concat(productDetails.getImagesDetails().get(0).getImage())).into(productImagePop);

        categoryTagsPop.removeAllViews();
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
        showProductDialog();
    }

    private void updateNotInCartAndCartCount() {
        miniCartDetails=new MiniCartDetails();
        miniCartDetails.setCount(1);
        not_in_cart.setVisibility(View.VISIBLE);
        in_cart.setVisibility(View.GONE);
        cart_count.setText("1");
    }

    private void updateInCartAndCartCount(Integer count) {
        not_in_cart.setVisibility(View.GONE);
        in_cart.setVisibility(View.VISIBLE);
        cart_count.setText(count+"");
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
        if (requestCode == AppConstants.SCAN_PERMISSION_ALL) {// If request is cancelled, the result arrays are empty.
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED) { // permission was granted, yay! Do the contacts-related task you need to do.
                Log.e(TAG,"Permission Granted!");
                Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();
                updateUI();
            } else { // permission denied, boo! Disable the functionality that depends on this permission.
                Log.e(TAG,"Permission Denied!");
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
        Log.d(TAG,"Metrics Preview width and height="+screenWidth+" "+screenHeight);
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
                    Log.i(TAG,"Barcode/QR-code value:"+ barCodeValue);
                    if (productBarCodes.getResults().containsKey(barCodeValue)) {
                        getProductDetails(productBarCodes.getResults().get(barCodeValue));
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
                    ScanFragment.this.product_id=product_id;
                    loadData();
                }
            }
        });
    }

    private void loadData() {
        Map<String, String> data = new HashMap<>();
        Call<ProductDetails> getProductBarCodes = restMethods.getProductDetails(token,product_id,data);
        ApiResponse.callRetrofitApi(getProductBarCodes, RestApiMethods.getProductDetailsRequest, ScanFragment.this);
    }

    @Override
    public void onApiResponse(String strApiName, int status, Object data, int error) {
        if (strApiName.equals(RestApiMethods.getProductDetailsRequest)) {
            if(error!=1){
                productDetails = (ProductDetails) data;
                populateProductDialog();
            }else{
                isProductDialogOpen=false;
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
            }
        }
        if (strApiName.equals(RestApiMethods.updateCartDetailsRequest)) {
            CartDetails cartDetails = (CartDetails) data;
            miniCartDetails.convertCartDetails(cartDetails);
            productDetails.setCartDetails(miniCartDetails);
            updateInCartAndCartCount(cartDetails.getCount());
            Toast.makeText(getActivity(),"Updated!",Toast.LENGTH_SHORT).show();
        }
        if (strApiName.equals(RestApiMethods.deleteCartDetailsRequest)) {
            updateNotInCartAndCartCount();
            Toast.makeText(getActivity(),"Deleted!",Toast.LENGTH_SHORT).show();
        }
    }
}