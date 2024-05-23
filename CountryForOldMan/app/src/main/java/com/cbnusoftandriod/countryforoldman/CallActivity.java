//package com.cbnusoftandriod.countryforoldman;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.material.snackbar.Snackbar;
//
//public class CallActivity extends AppCompatActivity {
//
//    private Button btnCall;
//
//    // 권한 요청 코드
//    private static final int REQUEST_CALL_PHONE_PERMISSION = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //etContentView(R.layout.menu_delivery);
//
//        btnCall = findViewById(R.id.btnCall);
//
//        // 권한 체크 및 요청
//        checkCallPhonePermission();
//
//        btnCall.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String phoneNumber = getPhoneNumberById(1); // ID는 필요에 따라 변경
//                Uri uri = Uri.parse("tel:" + phoneNumber);
//                Intent intent = new Intent(Intent.ACTION_CALL, uri);
//                if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                    startActivity(intent);
//                } else {
//                    // 권한이 없으면 요청
//                    ActivityCompat.requestPermissions(CallActivity.this,
//                            new String[]{Manifest.permission.CALL_PHONE},
//                            REQUEST_CALL_PHONE_PERMISSION);
//                }
//            }
//        });
//    }
//
//    // 권한 체크 및 요청 메서드
//    private void checkCallPhonePermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 권한이 없으면 요청
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    REQUEST_CALL_PHONE_PERMISSION);
//        }
//    }
//
//    // 권한 요청 결과 처리
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 권한이 승인되면 버튼 클릭 리스너 다시 설정
//                btnCall.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        String phoneNumber = getPhoneNumberById(new Order order.getId());
//                        Uri uri = Uri.parse("tel:" + phoneNumber);
//                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
//                        if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                            startActivity(intent);
//                        }
//                    }
//                });
//            } else {
//                // 권한이 거부됨
//                showPermissionRequestMessage();
//            }
//        }
//    }
//
//    // 권한 거부 메시지 표시
//    private void showPermissionRequestMessage() {
//        Snackbar.make(btnCall, "전화 걸기 권한이 필요합니다.", Snackbar.LENGTH_LONG)
//                .setAction("권한 요청", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ActivityCompat.requestPermissions(CallActivity.this,
//                                new String[]{Manifest.permission.CALL_PHONE},
//                                REQUEST_CALL_PHONE_PERMISSION);
//                    }
//                }).show();
//    }
//
//
//
//}