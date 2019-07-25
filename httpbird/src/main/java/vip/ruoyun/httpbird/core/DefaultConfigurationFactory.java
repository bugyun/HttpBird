package vip.ruoyun.httpbird.core;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by ruoyun on 2015/8/27.
 */
public class DefaultConfigurationFactory {

    private DefaultConfigurationFactory() {
    }

    public static String createDiskCache(Context context, String uniqueName) {
        String cachePath;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                cachePath = context.getExternalCacheDir().getPath();
                /** /sdcard/Android/data/<application package>/cache */
            } else {
                cachePath = context.getCacheDir().getPath();
                /** /data/data/<application package>/cache */
            }
        } catch (Exception e) {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }
}
