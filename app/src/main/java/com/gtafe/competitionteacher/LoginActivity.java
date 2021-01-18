package com.gtafe.competitionteacher;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity implements DataChangeInterFace {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.user)
    EditText mUser;
    @BindView(R.id.pwd)
    EditText mPwd;

    @BindView(R.id.login)
    Button mLogin;

    @BindView(R.id.updataapp)
    TextView updataApp;
    @BindView(R.id.login_name_ch)
    TextView mLoginNameCh;
    @BindView(R.id.login_name_en)
    TextView mLoginNameEn;
    private SharedPreferences mSp;



    @Override
    protected int setView() {
        return R.layout.activity_login;
    }


    @Override
    protected void init() {
        initState();

        // mUser.setText(mSp.getString("username", "admin"));a
        updata();
        checkPermission();
    }


    private long mStartTime;

    private void updata() {

        updataApp.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "onTouch:333");
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mStartTime = System.currentTimeMillis();

                        Log.e(TAG, "onTouch: ACTION_DOWN");
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e(TAG, "onTouch: ACTION_UP" + (System.currentTimeMillis() - mStartTime + ""));
                        if ((System.currentTimeMillis() - mStartTime) > 5000) {
                            // updataAPP();
                        }
                        break;
                }

                return true;
            }
        });
    }


    @OnClick({R.id.login, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:

                finish();

                break;
            case R.id.login:

                login(null);
                break;

        }
    }




    private void login(String card) {

        String userName = mUser.getText().toString().trim();
        String password = mPwd.getText().toString().trim();
        startActivity(new Intent(mContext, MainActivity.class));
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            showToast("用户名或者密码不能为空！");
            return;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");

    }






    private void checkPermission() {

        if (!hasPermissions(mContext, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions,
                                           @androidx.annotation.NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                for (int index = 0; index < permissions.length; index++) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        /*
                         * If the user turned down the permission request in the past and chose the
                         * Don't ask again option in the permission request system dialog.
                         */
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[index])) {
                            Toast.makeText(mContext, "Required permission " + permissions[index]
                                            + " not granted. "
                                            + "Please go to settings and turn on for app",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(mContext, "Required permission " + permissions[index]
                                    + " not granted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,

    };

    @Override
    public void onConnectStateChange(boolean b) {

    }
      /*  private void updataAPP() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("是否更新软件？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialogUtils.showDialog(com.gtafe.wisdomtraining.LoginActivity.this, "正在连接服务器...");

                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request build = new Request.Builder()
                                // .url("https://gtafe.ygxy.com/apk/Wisdomtraining/app-release.apk")
                                .url("http://app.jxue.edu.cn:100/apk/Wisdomtraining/app-release.apk")
                                .build();
                        okHttpClient.newCall(build).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialogUtils.cancelDialog();
                                        showToast("服务器异常");

                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.code() == 200) {
                                    Log.e(TAG, "onResponse: 访问网络成功");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialogUtils.cancelDialog();
                                            AlertDialogUtils.showDialog(com.gtafe.wisdomtraining.LoginActivity.this, "正在下载，请稍后...");
                                        }
                                    });
                                    InputStream inputStream = response.body().byteStream();
                                    String path = Environment.getExternalStorageDirectory() + "/a.apk";
                                    File file = new File(path);
                                    FileOutputStream outputStream = new FileOutputStream(file);
                                    byte[] buffer = new byte[1024 * 2];
                                    int len = -1;
                                    while ((len = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, len);
                                        Log.e(TAG, "onResponse: " + len);
                                    }
                                    inputStream.close();
                                    outputStream.close();
                                    AlertDialogUtils.cancelDialog();

                                    //下载完成，安装应用
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
                                    mContext.startActivity(intent);
                                } else {
                                    Log.e(TAG, "onResponse: 访问网络异常：" + response.code());
                                }


                            }
                        });


                    }
                });
        mBuilder.show();


    }*/
}
