package me.hhhaiai.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String work() {
        Class<?> clz = Build.class;

        Field[] fields = clz.getDeclaredFields();
        if (fields == null) {
            return null;
        }
        for (Field f : fields) {
            Annotation[] as = f.getDeclaredAnnotations();
            StringBuffer sb = new StringBuffer();
            if (as != null && as.length > 0) {
                for (Annotation a : as) {
                    sb.append(a.toString()).append("\r\n");
                }
            }
            sb.append(f.toGenericString());
            // 获取默认值
//            if (Modifier.isStatic(f.getModifiers()) ) {
//                try {
//                    Object res=  f.get(null);
//                    if (res != null) {
//                        sb.append(" = ").append(res);
//                    }
//                } catch (Throwable e) {
//                    loge(e);
//                }
//            }

            logi(sb.toString());
        }

        return null;
    }


    public void onClick(View view) {
        new Thread(() -> {
            try {
                work();
            } catch (Throwable e) {
                loge(e);
            }
        }).start();
    }


    private void logi(String info) {
        Log.i("sanbo", info);
    }

    private void loge(Throwable e) {
        Log.e("sanbo", Log.getStackTraceString(e));

    }

    private void loge(String info) {
        Log.e("sanbo", info);
    }
}