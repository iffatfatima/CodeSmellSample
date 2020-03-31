package com.example.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class EditorFragment extends Fragment implements View.OnClickListener {

    static final String TAG = "EditorFragment";
    private ImageView imageView;
    private ImageView sizePickerBtn;
    private ImageView colorPickerBtn;
    private ImageView clearBtn;
    private ImageView fillBtn;
    private ImageView strokeBtn;
    private CanvasView canvas;
    private SeekBar seekbar;
    private TextView saveBtn;
    private AlertDialog colorPickerDialog;
    private OnFragmentInteractionListener mListener;//LC
    private Bitmap bitmap;
    private Handler handler = new Handler();//LT

    public static EditorFragment newInstance() {
        return new EditorFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app, container, false);
        imageView = view.findViewById(R.id.iv_pic);
        fillBtn = view.findViewById(R.id.fill);
        strokeBtn = view.findViewById(R.id.stroke);
        clearBtn = view.findViewById(R.id.clear);
        sizePickerBtn = view.findViewById(R.id.size_picker);
        colorPickerBtn = view.findViewById(R.id.color_picker);
        seekbar = view.findViewById(R.id.seekbar);
        canvas = view.findViewById(R.id.canvas);
        saveBtn = view.findViewById(R.id.save);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        initUI();
        colorPickerDialog = ColorPickerDialogBuilder
                .with(getActivity())
                .setTitle("Choose color")
                .initialColor(canvas.getColor())
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        canvas.setColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build();
        mListener = (OnFragmentInteractionListener) getActivity();
    }

    private void initUI() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            byte[] byteArray = bundle.getByteArray("image");
            if (byteArray != null) {
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);//DTWC
                int sensorOrientation = bundle.getInt("orientation");
                int bitmapOrientation = (sensorOrientation + 90 + 360) % 360;
                Matrix matrix = new Matrix();
                switch (sensorOrientation){
                    case 90:
                        //cam portrait, image landscape
                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            matrix.postRotate(90);
                        }
                        break;
                    case 180:
                        //landscape
                        matrix.postRotate(90);
                        break;
                    default:
                        //landscape
                        matrix.postRotate(180);
                        break;
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);//BFU
                imageView.setImageBitmap(bitmap);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = canvas.getLayoutParams();
                    params.width = imageView.getWidth();
                    params.height = imageView.getHeight();
                    canvas.setLayoutParams(params);
                    canvas.invalidate();
                }
            }, 2000);

        }

        sizePickerBtn.setOnClickListener(this);
        colorPickerBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        fillBtn.setOnClickListener(this);
        strokeBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        seekbar.setMax(50);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                canvas.setSize(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.click);//ERB, RL
        mediaPlayer.start();
        switch (view.getId()){
            case R.id.size_picker:
                break;
            case R.id.color_picker:
                toggleColorPicker();
                break;
            case R.id.clear:
                canvas.clear();
                break;
            case R.id.fill:
                canvas.setStyle(Paint.Style.FILL);
                break;
            case R.id.stroke:
                canvas.setStyle(Paint.Style.STROKE);
                break;
            case R.id.save:
                seekbar.setVisibility(View.GONE);
                imageView.destroyDrawingCache();
                imageView.buildDrawingCache();
                canvas.destroyDrawingCache();
                canvas.buildDrawingCache();
                final Bitmap canvasBitmap = canvas.getDrawingCache();
                Canvas canvas = new Canvas(canvasBitmap);
                getView().findViewById(R.id.parent).draw(canvas);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        File file = Utils.saveImage(canvasBitmap);
                        Snackbar.make(getView().findViewById(R.id.bar), "Image Saved. GO back to view it in gallery",
                                Snackbar.LENGTH_LONG).show();
                        mListener.onImageSaved(file);
                    }
                });
                break;
        }

        showSeekBar(view.getId() == R.id.size_picker);
    }

    private void showSeekBar(boolean show){
        seekbar.setVisibility(show? View.VISIBLE: View.GONE);
    }
    private void toggleColorPicker(){
        if (colorPickerDialog.isShowing()) {
            colorPickerDialog.hide();
        } else {
            colorPickerDialog.show();
        }
    }

    public interface OnFragmentInteractionListener {
        void onImageSaved(File file);
    }
}
