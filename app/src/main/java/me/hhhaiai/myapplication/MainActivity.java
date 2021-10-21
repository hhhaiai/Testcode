package me.hhhaiai.myapplication;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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

    private String workDeclaredFields(Class<?> clz) throws NoSuchFieldException, IllegalAccessException {


        Field[] fields = clz.getDeclaredFields();
        if (fields == null) {
            return null;
        }
        StringBuilder res = new StringBuilder();
        for (Field f : fields) {
            Annotation[] as = f.getDeclaredAnnotations();
            StringBuffer line = new StringBuffer();
            if (as != null && as.length > 0) {
                for (Annotation a : as) {
                    line.append(a.toString()).append("\r\n");
                }
            }
            line.append(f.toGenericString());

            f.setAccessible(true);
            int modify = f.getModifiers();
            if (java.lang.reflect.Modifier.isFinal(modify)) {
                java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("accessFlags");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, modify & ~java.lang.reflect.Modifier.FINAL);
            }
            // 获取默认值
            if (Modifier.isStatic(modify)) {
                try {
                    Object resFieldValue = f.get(null);
                    if (resFieldValue != null) {
                        line.append(" = ").append(wrpper(resFieldValue));
                    }
                } catch (Throwable e) {
                    loge(f.toGenericString());
                    loge(e);
                }
            }

            if (!line.toString().endsWith(";")) {
                res.append(line.toString()).append(";").append("\r\n");
            }
        }

        return res.toString();
    }

    public static Object wrpper(Object res) {
        if (res instanceof Long) {
            return (Long) res + "L";
        } else if (res instanceof Float) {
            return (Float) res + "F";
        } else if (res instanceof Double) {
            return (Double) res + "D";
        } else if (res instanceof Number) {
            return res;
        } else if (res instanceof String) {
            return "\"" + res.toString() + "\"";
        } else if (res instanceof String[]) {
            return toString((String[]) res);
        }

        return null;
    }


    public static String toString(String[] a) {
        if (a == null || a.length == 0) {
            return "new String[]{}";
        }
        StringBuilder b = new StringBuilder();
        b.append("new String[]{");
        for (int i = 0; i < a.length; i++) {
            if (i != 0) {
                b.append(",");
            }
            b.append("\"").append(a[i]).append("\"");
        }
        return b.append('}').toString();
    }


    public void onClick(View view) {
        new Thread(() -> {
            try {
                Class<?> clz = Build.class;
                logi(workDeclaredFields(clz));

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