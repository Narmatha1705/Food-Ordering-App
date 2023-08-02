package com.example.foodscout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.utils.Constant;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Khalti_payment extends AppCompatActivity {
    //KhaltiButton khaltiButton;
   // KhaltiCheckOut khaltiCheckOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khalti_payment);

        //khaltiButton = (KhaltiButton) findViewById(R.id.khalti_button);


       /* Map<String, Object> map = new HashMap<>();
        map.put("merchant_extra", "This is extra data");

        Config.Builder builder = new Config.Builder(Constant.pub, "Product ID", "Main", 1100L, new OnCheckOutListener() {
            @Override
            public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                Log.i(action, errorMap.toString());
            }

            @Override
            public void onSuccess(@NonNull Map<String, Object> data) {
                Log.i("success", data.toString());
            }
        })
                .paymentPreferences(new ArrayList<PaymentPreference>() {{
                    add(PaymentPreference.KHALTI);
                    add(PaymentPreference.EBANKING);
                    add(PaymentPreference.MOBILE_BANKING);
                    add(PaymentPreference.CONNECT_IPS);
                    add(PaymentPreference.SCT);
                }})
                .additionalData(map)
                .productUrl("http://example.com/product")
                .mobile("9800000000");  */


        //Config config = builder.build();
       // khaltiButton.setCheckOutConfig(config);

      //  khaltiCheckOut = new KhaltiCheckOut(this, config);

       // khaltiButton.setOnClickListener(new View.OnClickListener() {
          //  @Override
           // public void onClick(View view) {
              //  khaltiCheckOut.show();
       //     }
     //   });
    }
}
