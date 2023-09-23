package com.rasel.moviedb.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Locale;


public class LocalFileUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mDefaultHandler;

    public LocalFileUncaughtExceptionHandler(Context context, Thread.UncaughtExceptionHandler defaultHandler) {
        this.mDefaultHandler = defaultHandler;
        this.mContext = context;
    }

    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable ex) {
        Log.e("Crash", "Application crash", ex);
        writeFile(thread, ex);

        mDefaultHandler.uncaughtException(thread, ex);
    }

    private void writeFile(final Thread thread, final Throwable ex) {
        try {
            OutputStream os = getLogStream();
            os.write(getExceptionInformation(thread, ex).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OutputStream getLogStream() throws IOException {
        //crash_log_pkgname.log
//        String fileName = String.format("crash_%s.log", mContext.getPackageName());
        String fileName = "crash_multi_type_file_picker.log";
       // File file = new File(Environment.getExternalStorageDirectory(), fileName);

        // modified to make compatible with scoped storage
        File file = new File( mContext.getExternalCacheDir(), fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        return new FileOutputStream(file, true);
    }

    private String getExceptionInformation(Thread thread, Throwable ex) {
        long current = System.currentTimeMillis();

        return '\n' +
                "THREAD: " + thread + '\n' +
                "BOARD: " + Build.BOARD + '\n' +
                "BOOTLOADER: " + Build.BOOTLOADER + '\n' +
                "BRAND: " + Build.BRAND + '\n' +
                "CPU_ABI: " + Build.CPU_ABI + '\n' +
                "CPU_ABI2: " + Build.CPU_ABI2 + '\n' +
                "DEVICE: " + Build.DEVICE + '\n' +
                "DISPLAY: " + Build.DISPLAY + '\n' +
                "FINGERPRINT: " + Build.FINGERPRINT + '\n' +
                "HARDWARE: " + Build.HARDWARE + '\n' +
                "HOST: " + Build.HOST + '\n' +
                "ID: " + Build.ID + '\n' +
                "MANUFACTURER: " + Build.MANUFACTURER + '\n' +
                "MODEL: " + Build.MODEL + '\n' +
                "PRODUCT: " + Build.PRODUCT + '\n' +
                "SERIAL: " + Build.SERIAL + '\n' +
                "TAGS: " + Build.TAGS + '\n' +
                "TIME: " + Build.TIME + ' ' + toDateString(Build.TIME) + '\n' +
                "TYPE: " + Build.TYPE + '\n' +
                "USER: " + Build.USER + '\n' +
                "VERSION.CODENAME: " + Build.VERSION.CODENAME + '\n' +
                "VERSION.INCREMENTAL: " + Build.VERSION.INCREMENTAL + '\n' +
                "VERSION.RELEASE: " + Build.VERSION.RELEASE + '\n' +
                "VERSION.SDK_INT: " + Build.VERSION.SDK_INT + '\n' +
                "LANG: " + mContext.getResources().getConfiguration().locale.getLanguage() + '\n' +
                "APP.VERSION.NAME: " + getVersionName() + '\n' +
                "APP.VERSION.CODE: " + getVersionCode() + '\n' +
                "CURRENT: " + current + ' ' + toDateString(current) + '\n' +
                getErrorInformation(ex);
    }

    private String getVersionName() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo;
        String version = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    private int getVersionCode() {
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo;
        int version = 0;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            version = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    private String getErrorInformation(Throwable t) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);
        t.printStackTrace(writer);
        writer.flush();
        String result = baos.toString();
        writer.close();

        return result;
    }

    private String toDateString(long timeMilli) {
        Calendar calc = Calendar.getInstance();
        calc.setTimeInMillis(timeMilli);
        return String.format(Locale.CHINESE, "%04d.%02d.%02d %02d:%02d:%02d:%03d",
                calc.get(Calendar.YEAR), calc.get(Calendar.MONTH) + 1, calc.get(Calendar.DAY_OF_MONTH),
                calc.get(Calendar.HOUR_OF_DAY), calc.get(Calendar.MINUTE), calc.get(Calendar.SECOND), calc.get(Calendar.MILLISECOND));
    }

}
