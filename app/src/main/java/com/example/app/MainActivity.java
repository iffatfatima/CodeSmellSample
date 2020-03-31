package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//NLMR
public class MainActivity extends AppCompatActivity implements EditorFragment.OnFragmentInteractionListener {

    private RecyclerView rv;
    private TextView welcomeTv;
    private ImageView arrowIv;
    private RVAdapter rvAdapter;
    private boolean imageSaved = false;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //todo:
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        Log.d("TOKEN", Objects.requireNonNull(task.getResult()).getToken());
                    }
                });
        rv = findViewById(R.id.rv);
        welcomeTv = findViewById(R.id.tv_welcome_text);
        arrowIv = findViewById(R.id.iv_arrow);

    }



    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void fetchImages() {
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        File [] fileArr = Utils.loadImages();
        if (fileArr != null && fileArr.length >0) {
            welcomeTv.setVisibility(View.GONE);
            arrowIv.setVisibility(View.GONE);
            List<File> fileList = new ArrayList<>(Arrays.asList(fileArr));
            rvAdapter = new RVAdapter(fileList);
            rv.setAdapter(rvAdapter);
        }
        else {
            welcomeTv.setVisibility(View.VISIBLE);
            arrowIv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions(MainActivity.this, PERMISSIONS)) {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.container, CameraFragment.newInstance(), CameraFragment.TAG);
                    transaction.addToBackStack(CameraFragment.TAG);
                    transaction.commit();
                }
                else {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, PERMISSION_ALL);
                    Snackbar.make(findViewById(R.id.container), "Please grant permissions to proceed",
                            Snackbar.LENGTH_LONG).show();
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction("Location");
        registerReceiver(receiver, filter);
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            Snackbar.make(findViewById(R.id.container), "Please grant permissions to proceed",
                    Snackbar.LENGTH_LONG).show();
        }
        else {
            fetchImages();
            startService(new Intent(this, LocationService.class));//VBS
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        fetchImages();
    }

    @Override
    public void onBackPressed(){
        if (imageSaved){
            fetchImages();
            imageSaved = false;
        }
        super.onBackPressed();
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
    }

    @Override
    public void onImageSaved(File file) {
        imageSaved = true;
    }
}
