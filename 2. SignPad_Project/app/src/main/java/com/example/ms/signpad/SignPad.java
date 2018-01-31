package com.example.ms.signpad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pen.Spen;
import com.samsung.android.sdk.pen.document.SpenNoteDoc;
import com.samsung.android.sdk.pen.document.SpenPageDoc;
import com.samsung.android.sdk.pen.engine.SpenSurfaceView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by ms on 2018-01-18.
 */

public class SignPad extends Activity {
    private Context context;
    private SpenNoteDoc spenNoteDoc;
    private SpenPageDoc spenPageDoc;
    private SpenSurfaceView spenSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      // 타이틀바 없애기
        context = this;

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        String BackgroundColor = "#eeeeee";
        linearLayout.setBackgroundColor(Color.parseColor((BackgroundColor)));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
            textView.setLayoutParams(textViewParams);
            textView.setText("SignPad");
            String BackgroundColor1 = "#888888";
            textView.setBackgroundColor(Color.parseColor(BackgroundColor1));
            textView.setGravity(Gravity.CENTER);
            String TextColor = "#fff";
            textView.setTextColor(Color.parseColor(TextColor));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            linearLayout.addView(textView);

            LinearLayout linearLayout1 = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(300, 100);
            linearLayout1.setLayoutParams(layoutParams1);
            linearLayout1.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(linearLayout1);

            View view = new View(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            view.setLayoutParams(viewParams);
            String BackgroundColor2 = "#888888";
            view.setBackgroundColor(Color.parseColor(BackgroundColor2));
            linearLayout.addView(view);

        setContentView(linearLayout);


        // Initialize Spen
        boolean isSpenFeatureEnabled = false;
        Spen spenPackage = new Spen();
        try {
            spenPackage.initialize(this);
            isSpenFeatureEnabled = spenPackage.isFeatureEnabled(Spen.DEVICE_PEN);
        } catch (SsdkUnsupportedException e) {
            if( processUnsupportedException(e) == true) {
                return;
            }
        } catch (Exception e1) {
            Toast.makeText(context, "Cannot initialize Spen.",
                    Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
            finish();
        }

        // Create Spen View
        LinearLayout spenViewLayout = (LinearLayout) findViewById(R.id.linearLayout);
        spenSurfaceView = new SpenSurfaceView(context);
        if (spenSurfaceView == null) {
            Toast.makeText(context, "Cannot create new SpenView.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        spenViewLayout.addView(spenSurfaceView);

        // Get the dimension of the device screen.
        Display display = getWindowManager().getDefaultDisplay();
        Rect rect = new Rect();
        display.getRectSize(rect);

        // Create SpenNoteDoc
        try {
            spenNoteDoc =
                    new SpenNoteDoc(context, rect.width(), rect.height());
        } catch (IOException e) {
            Toast.makeText(context, "Cannot create new NoteDoc.",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        // Add a Page to NoteDoc, get an instance, and set it to the member variable.
        spenPageDoc = spenNoteDoc.appendPage();
        spenPageDoc.setBackgroundColor(0xFFFFFFFF);
        spenPageDoc.clearHistory();

        // Set PageDoc to View.
        spenSurfaceView.setPageDoc(spenPageDoc, true);

        if(isSpenFeatureEnabled == false) {
            spenSurfaceView.setToolTypeAction(SpenSurfaceView.TOOL_FINGER, SpenSurfaceView.ACTION_STROKE);
            Toast.makeText(context,
                    "Device does not support Spen. \n You can draw stroke by finger.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean processUnsupportedException(SsdkUnsupportedException e) {
        e.printStackTrace();
        int errType = e.getType();
        // If the device is not a Samsung device or if the device does not support Pen.
        if (errType == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED
                || errType == SsdkUnsupportedException.DEVICE_NOT_SUPPORTED) {
            Toast.makeText(context, "This device does not support Spen.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        else if (errType == SsdkUnsupportedException.LIBRARY_NOT_INSTALLED) {
            // If SpenSDK APK is not installed.
            showAlertDialog( "You need to install additional Spen software"
                            +" to use this application."
                            + "You will be taken to the installation screen."
                            + "Restart this application after the software has been installed."
                    , true);
        } else if (errType
                == SsdkUnsupportedException.LIBRARY_UPDATE_IS_REQUIRED) {
            // SpenSDK APK must be updated.
            showAlertDialog( "You need to update your Spen software "
                            + "to use this application."
                            + " You will be taken to the installation screen."
                            + " Restart this application after the software has been updated."
                    , true);
        } else if (errType
                == SsdkUnsupportedException.LIBRARY_UPDATE_IS_RECOMMENDED) {
            // Update of SpenSDK APK to an available new version is recommended.
            showAlertDialog( "We recommend that you update your Spen software"
                            +" before using this application."
                            + " You will be taken to the installation screen."
                            + " Restart this application after the software has been updated."
                    , false);
            return false;
        }
        return true;
    }

    private void showAlertDialog(String msg, final boolean closeActivity) {

        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(getResources().getDrawable(
                android.R.drawable.ic_dialog_alert));
        dlg.setTitle("Upgrade Notification")
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog, int which) {
                                // Go to the market website and install/update APK.
                                Uri uri = Uri.parse("market://details?id=" + Spen.getSpenPackageName());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                dialog.dismiss();
                                finish();
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialog, int which) {
                                if(closeActivity == true) {
                                    // Terminate the activity if APK is not installed.
                                    finish();
                                }
                                dialog.dismiss();
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if(closeActivity == true) {
                            // Terminate the activity if APK is not installed.
                            finish();
                        }
                    }
                })
                .show();
        dlg = null;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (spenSurfaceView != null) {
            spenSurfaceView.close();
            spenSurfaceView = null;
        }

        if(spenNoteDoc != null) {
            try {
                spenNoteDoc.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            spenNoteDoc = null;
        }
    };

    public void btn_Save_event(View v) {      // 서명 html canvas 로 전달
        Intent intent = new Intent();
        intent.putExtra("result", "Save");
        intent.putExtra("data", saveSign());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btn_Clear_event(View v) {       // 지우기
        Intent intent = new Intent();
        intent.putExtra("result", "Clear");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void btn_Cancel_event(View v) {      // Screen 화면 닫기
        Intent intent = new Intent();
        intent.putExtra("result", "Cancel");
        setResult(RESULT_OK, intent);
        finish();
    }

    public String saveSign() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            spenSurfaceView.captureCurrentView(true).compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

