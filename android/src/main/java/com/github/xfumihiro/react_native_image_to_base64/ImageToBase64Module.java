package com.github.xfumihiro.react_native_image_to_base64;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.net.Uri;

public class ImageToBase64Module extends ReactContextBaseJavaModule {
 Context context;

 public ImageToBase64Module(ReactApplicationContext reactContext) {
   super(reactContext);
   this.context = (Context) reactContext;
 }

 @Override
 public String getName() {
   return "RNImageToBase64";
 }

 @ReactMethod
 public void getBase64String(String uri, Callback callback) {
     callback.invoke(null, getFileSize(Uri.parse(uri)));
 }

 private String getFileSize(Uri uri) {
   try {
     final File file = createFileFromURI(uri);
     String realPath = file.getAbsolutePath();
    //  Thread t = new Thread(){
    //    public void run() {
    //      file.delete();
    //    }
    //  };
    //  t.start();
     return Long.toString(file.length());
   } catch (Exception e) {
     e.printStackTrace();
     return "error";
   }
 }

 private File createFileFromURI(Uri uri) throws Exception {
   File file = new File(this.context.getCacheDir(), "photo-" + uri.getLastPathSegment());
   InputStream input = this.context.getContentResolver().openInputStream(uri);
   OutputStream output = new FileOutputStream(file);

   try {
     byte[] buffer = new byte[4 * 1024];
     int read;
     while ((read = input.read(buffer)) != -1) {
       output.write(buffer, 0, read);
     }
     output.flush();
   } finally {
     output.close();
     input.close();
   }

   return file;
 }
}
