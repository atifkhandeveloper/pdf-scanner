package com.myspps.pdfscanner.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.lifecycle.LifecycleOwner;
import com.myspps.pdfscanner.R;
import com.myspps.pdfscanner.model.ImageModel;
import com.myspps.pdfscanner.utils.CustomToast;
import com.myspps.pdfscanner.utils.Permissions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {
    ImageView backBtn;
    private CameraManager camManager;
    ExecutorService cameraExecutor;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ImageView captureButton;
    Context context = this;
    File fileName;
    ImageView flashButton;
    ImageView galleryPicker;
    String galleryPickerType;
    ImageView idCardImage;
    ImageCapture imageCapture;
    String imageType;
    ArrayList<ImageModel> imageUriArray = new ArrayList<>();
    private Camera mCamera;
    ImageView multipleImages;
    boolean multipleSelection = false;
    File outputDirectory;
    private Camera.Parameters parameters;
    int position = 0;
    PreviewView previewView;
    ProgressBar progressBar;
    ShapeableImageView showImage;
    TextView tvFrontBackHint;
    TextView tvImageSize;
    String type = "camera";

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_camera);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);




        this.previewView = (PreviewView) findViewById(R.id.perviewView);
        this.captureButton = (ImageView) findViewById(R.id.capture);
        this.showImage = (ShapeableImageView) findViewById(R.id.show_images);
        this.flashButton = (ImageView) findViewById(R.id.flashButton);
        this.multipleImages = (ImageView) findViewById(R.id.multiple_images);
        this.tvImageSize = (TextView) findViewById(R.id.tv_image_size);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.tvFrontBackHint = (TextView) findViewById(R.id.tv_front_back);
        this.idCardImage = (ImageView) findViewById(R.id.idCardImage);
        this.galleryPicker = (ImageView) findViewById(R.id.gallery_images);
        this.backBtn = (ImageView) findViewById(R.id.backBtn);
        this.imageType = getIntent().getStringExtra("textFromButton");
        this.flashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CameraActivity.this.imageCapture.getFlashMode() == 2) {
                    CameraActivity.this.imageCapture.setFlashMode(1);
                    CameraActivity.this.flashButton.setImageResource(R.drawable.ic_flash_on);
                    return;
                }
                CameraActivity.this.imageCapture.setFlashMode(2);
                CameraActivity.this.flashButton.setImageResource(R.drawable.ic_flash_off);
            }
        });
        this.backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraActivity.this.onBackPressed();
            }
        });
        this.multipleImages.setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                CameraActivity.this.lambda$onCreate$0$CameraActivity(view);
            }
        });
        File file = new File(new ContextWrapper(this.context).getExternalCacheDir(), "Images");
        File file2 = new File(file, "");
        if (!file.exists()) {
            file.mkdirs();
        } else {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                Context context2 = this.context;
                new CustomToast(context2, "" + e);
            }
        }
        this.outputDirectory = file;
        this.cameraExecutor = Executors.newSingleThreadExecutor();
        this.cameraProviderFuture = ProcessCameraProvider.getInstance(CameraActivity.this);
        startCamera();
        String str = this.imageType;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1953280235:
                if (str.equals("ID Card")) {
                    c = 0;
                    break;
                }
                break;
            case -409933107:
                if (str.equals("ID Photo")) {
                    c = 1;
                    break;
                }
                break;
            case 2076425:
                if (str.equals("Book")) {
                    c = 2;
                    break;
                }
                break;
            case 2135643:
                if (str.equals("Docs")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.idCardImage.setVisibility(0);
                this.multipleImages.setVisibility(8);
                this.tvFrontBackHint.setVisibility(0);
                this.tvFrontBackHint.setHint("Front Page");
                this.captureButton.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        CameraActivity.this.lambda$onCreate$3$CameraActivity(view);
                    }
                });
                this.galleryPicker.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        CameraActivity.this.lambda$onCreate$4$CameraActivity(view);
                    }
                });
                break;
            case 1:
                this.galleryPicker.setVisibility(8);
                this.captureButton.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        CameraActivity.this.lambda$onCreate$5$CameraActivity(view);
                    }
                });
                break;
            case 2:
            case 3:
                this.captureButton.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        CameraActivity.this.lambda$onCreate$1$CameraActivity(view);
                    }
                });
                this.galleryPicker.setOnClickListener(new View.OnClickListener() {
                    public final void onClick(View view) {
                        CameraActivity.this.lambda$onCreate$2$CameraActivity(view);
                    }
                });
                break;
        }
        this.showImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraActivity.this.goToCropperActivity();
            }
        });
    }

    public  void lambda$onCreate$0$CameraActivity(View view) {
        if (!this.multipleSelection) {
            this.multipleImages.setImageResource(R.drawable.ic_multipleblue);
            this.multipleSelection = true;
            new CustomToast(this.context, "Batch Mode Enabled");
            return;
        }
        this.multipleImages.setImageResource(R.drawable.ic_multipleee);
        this.multipleSelection = false;
        new CustomToast(this.context, "Single Mode Enabled");
    }

    public  void lambda$onCreate$1$CameraActivity(View view) {
        captureDocsPhoto();
    }

    public  void lambda$onCreate$2$CameraActivity(View view) {
        this.galleryPickerType = "Docs";
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            goToImagePickerActivity();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Permissions.requestMediaPermission(this);
            } else {
                Permissions.requestReadStoragePermission(this);
            }
        }
    }

    public  void lambda$onCreate$3$CameraActivity(View view) {
        captureIDCardPhoto();
    }

    public  void lambda$onCreate$4$CameraActivity(View view) {
        this.galleryPickerType = "ID Card";
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            goToImagePickerActivity();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Permissions.requestMediaPermission(this);
            } else {
                Permissions.requestReadStoragePermission(this);
            }
        }
    }

    public  void lambda$onCreate$5$CameraActivity(View view) {
        captureDocsPhoto();
    }

    private void goToImagePickerActivity() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra("imageType", this.galleryPickerType);
        startActivity(intent);
    }

    private void captureIDCardPhoto() {
        this.galleryPicker.setVisibility(8);
        File file = new File(this.outputDirectory, fileName());
        this.fileName = file;
        this.imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(file).build(), ContextCompat.getMainExecutor(this.context), new ImageCapture.OnImageSavedCallback() {
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                Uri fromFile = Uri.fromFile(CameraActivity.this.fileName);
                CameraActivity.this.showImage.setVisibility(0);
                CameraActivity.this.showImage.setImageURI(fromFile);
                CameraActivity.this.imageUriArray.add(new ImageModel(fromFile));
                CameraActivity.this.position++;
                if (CameraActivity.this.position == 1) {
                    CameraActivity.this.tvFrontBackHint.setVisibility(0);
                    CameraActivity.this.tvFrontBackHint.setHint("Back Page");
                } else if (CameraActivity.this.position == 2) {
                    CameraActivity.this.goToCropperActivity();
                }
            }

            public void onError(ImageCaptureException imageCaptureException) {
                Context context = CameraActivity.this.context;
                new CustomToast(context, "Can't take picture" + imageCaptureException.getMessage());
            }
        });
    }

    private void captureDocsPhoto() {
        this.galleryPicker.setVisibility(8);
        File file = new File(this.outputDirectory, fileName());
        this.fileName = file;
        this.imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(file).build(), ContextCompat.getMainExecutor(this.context), new ImageCapture.OnImageSavedCallback() {
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                Uri fromFile = Uri.fromFile(CameraActivity.this.fileName);
                if (CameraActivity.this.previewView.getPreviewStreamState().getValue() == PreviewView.StreamState.STREAMING) {
                    CameraActivity.this.progressBar.setVisibility(8);
                } else {
                    CameraActivity.this.progressBar.setVisibility(0);
                }
                if (CameraActivity.this.multipleSelection) {
                    CameraActivity.this.multipleImages.setVisibility(8);
                    CameraActivity.this.showImage.setVisibility(0);
                    CameraActivity.this.tvImageSize.setVisibility(0);
                    CameraActivity.this.showImage.setImageURI(fromFile);
                    CameraActivity.this.imageUriArray.add(new ImageModel(fromFile));
                    CameraActivity.this.tvImageSize.setText(String.valueOf(CameraActivity.this.imageUriArray.size()));
                    CameraActivity.this.position++;
                    return;
                }
                CameraActivity.this.imageUriArray.add(new ImageModel(fromFile));
                CameraActivity.this.goToCropperActivity();
            }

            public void onError(ImageCaptureException imageCaptureException) {
                Context context = CameraActivity.this.context;
                new CustomToast(context, "Can't take picture" + imageCaptureException.getMessage());
            }
        });
    }

    
    public void goToCropperActivity() {
        Intent intent = new Intent(this, CropperActivity.class);
        intent.putParcelableArrayListExtra("imageUri", this.imageUriArray);
        intent.putExtra("gallery", this.type);
        startActivity(intent);
        finish();
    }

    private void startCamera() {
        this.previewView.post(new Runnable() {
            public final void run() {
                CameraActivity.this.lambda$startCamera$7$CameraActivity();
            }
        });
    }

    public  void lambda$startCamera$7$CameraActivity() {
        this.cameraProviderFuture.addListener(new Runnable() {
            public final void run() {
                CameraActivity.this.lambda$null$6$CameraActivity();
            }
        }, ContextCompat.getMainExecutor(this.context));
    }

    public  void lambda$null$6$CameraActivity() {
        try {
            bindPreview((ProcessCameraProvider) this.cameraProviderFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        Preview build = new Preview.Builder().build();
        CameraSelector build2 = new CameraSelector.Builder().requireLensFacing(1).build();
        //build.setSurfaceProvider(this.previewView.getSurfaceProvider());
        build.setSurfaceProvider(previewView.createSurfaceProvider());
        if (Build.VERSION.SDK_INT >= 30) {
            this.imageCapture = new ImageCapture.Builder().setTargetRotation(getDisplay().getRotation()).build();
        } else {
            this.imageCapture = new ImageCapture.Builder().setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        }
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle((LifecycleOwner) this, build2, this.imageCapture, build);
    }

    private String fileName() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("IMG_");
        stringBuffer.append(new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Long.valueOf(System.currentTimeMillis())));
        stringBuffer.append(".png");
        return stringBuffer.toString();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i != 2) {
            return;
        }
        if (iArr.length > 0 && iArr[0] == 0) {
            goToImagePickerActivity();
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (!shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE")) {
                new AlertDialog.Builder(this.context).setMessage("Permission Necessary").setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.parse("package:" + CameraActivity.this.getPackageName()));
                        CameraActivity.this.startActivityForResult(intent, 0);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CameraActivity.this.finish();
                    }
                }).show();
            } else {
                finish();
            }
        }
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage((CharSequence) "Do you want to quit without saving").setPositiveButton((CharSequence) "I'm sure", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                CameraActivity.this.lambda$onBackPressed$8$CameraActivity(dialogInterface, i);
            }
        }).setNegativeButton((CharSequence) "No", (DialogInterface.OnClickListener) $$Lambda$CameraActivity$mxcpdbFm0mXqCuB4IeSp_4wxKEA.INSTANCE).show();
    }

    public  void lambda$onBackPressed$8$CameraActivity(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        super.onBackPressed();
    }
}
