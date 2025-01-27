package ru.badin.buttonlocktelephone;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);



        Button lockButton = findViewById(R.id.buttonLock);
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lockScreen();
                Toast.makeText(MainActivity.this, "Кнопка была нажата!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void lockScreen(){

        if (isAdminActive()) {
            devicePolicyManager.lockNow();
        } else {
            // Запросить у пользователя разрешение на администрирование устройства.
            activateAdmin();
        }
    }

    private void activateAdmin(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Для блокировки экрана.");
        startActivityForResult(intent, 0);
    }

    private boolean isAdminActive() {
        return devicePolicyManager.isAdminActive(componentName);
    }
}