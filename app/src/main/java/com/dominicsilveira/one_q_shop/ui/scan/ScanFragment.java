package com.dominicsilveira.one_q_shop.ui.scan;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
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
import com.dominicsilveira.one_q_shop.utils.AppConstants;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.security.Permission;

public class ScanFragment extends Fragment {

    SurfaceView image;
    RelativeLayout cameraOn,cameraOff;
    MaterialButton turnOnCameraBtn;

    String[] PERMISSIONS = {
            android.Manifest.permission.CAMERA,
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Scan");

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
        cameraOn.setVisibility(View.GONE);
        cameraOff.setVisibility(View.GONE);
    }

    private void addListeners() {
        turnOnCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
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
                if(barcodeSparseArray.size()>0){
                    Log.i("Barcode/QR-code value:",barcodeSparseArray.valueAt(0).displayValue);
//                    Intent intent=new Intent();
//                    intent.putExtra("barcode",barcodeSparseArray.valueAt(0));
//                    setResult(CommonStatusCodes.SUCCESS,intent);
//                    finish();
                }
            }
        });
    }
}