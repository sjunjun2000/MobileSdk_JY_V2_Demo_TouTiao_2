package com.you9.gamesdk.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.gdt.action.ActionType;
import com.qq.gdt.action.GDTAction;
import com.ss.android.common.applog.TeaAgent;
import com.ss.android.common.applog.TeaConfigBuilder;
import com.ss.android.common.lib.AppLogNewUtils;
import com.ss.android.common.lib.EventUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.bean.JyUserDao;
import com.you9.gamesdk.bean.JyWxInfo;
import com.you9.gamesdk.bean.JyYou9;
import com.you9.gamesdk.bean.JyYou9Dao;
import com.you9.gamesdk.enums.JyPromotionApiUploadType;
import com.you9.gamesdk.enums.JyPromotionChannelType;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyPermissionListener;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.openUDID.OpenUDID_manager;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import io.reactivex.functions.Consumer;

public class JyUtils implements GLSurfaceView.Renderer {

    private static String mGpu = "";
    private static int screenWidth, screenHeight;
    private static int statusBarHeight;
    private static JyYou9Dao you9Dao;
    private static JyUserDao userDao;
    private static Context mContext;

    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length - 1];
            try {
                MediaStore.Images.Media.insertImage(mContext.getApplicationContext().getContentResolver(), picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
            Toast.makeText(mContext, "图片保存图库成功", Toast.LENGTH_LONG).show();
        }
    };

    public static void getPermissions(Context context, String permission, String permission2, final JyPermissionListener listener) {
        new RxPermissions((Activity) context).request(permission, permission2).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    listener.onPermissionSuccess();
                } else {
                    listener.onPermissionFailed();
                }


            }
        });
    }

    /**
     * 获取sdk配置文件信息
     */
    @SuppressLint("NewApi")
    public static JySdkConfigInfo getSdkConfigInfo(Context context) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            inputStream = context.getResources().getAssets().open(JyConstants.SDK_INFO_PATH);
            if (inputStream != null) {
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                properties.load(bf);
                JySdkConfigInfo jySdkConfigInfo = JySdkConfigInfo.getInstance();
                jySdkConfigInfo.setClientId(properties.getProperty("clientId"));
                jySdkConfigInfo.setAppId(properties.getProperty("appId"));
                jySdkConfigInfo.setGameName(properties.getProperty("gameName"));
                jySdkConfigInfo.setChannelName(properties.getProperty("channelName"));
                jySdkConfigInfo.setChannelId(properties.getProperty("channelId"));
                jySdkConfigInfo.setCompany(properties.getProperty("company"));
                jySdkConfigInfo.setKfUrl(properties.getProperty("kfUrl"));
                jySdkConfigInfo.setKfTel(properties.getProperty("kfTel"));
                jySdkConfigInfo.setTouTiaoAppId(Integer.parseInt(properties.getProperty("touTiaoAppId")));
                jySdkConfigInfo.setGdtActionSetId(properties.getProperty("gdtActionSetId"));
                jySdkConfigInfo.setGdtSecretKey(properties.getProperty("gdtSecretKey"));
                jySdkConfigInfo.setReportType(properties.getProperty("reportType"));
                jySdkConfigInfo.setE1(properties.getProperty("e1"));
                jySdkConfigInfo.setE2(properties.getProperty("e2"));
                jySdkConfigInfo.setE3(properties.getProperty("e3"));

                inputStream.close();

                return getSystemInfo(context, jySdkConfigInfo);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 获取登录信息
     */
    public static void getSdkLoginInfo(String xml) {

        try {


            Document doc = DocumentHelper.parseText(xml);
            Element rootElement = doc.getRootElement();

            JyYou9 jyYou9 = JyYou9.getInstance();
            jyYou9.setState(rootElement.elementTextTrim("state"));
            jyYou9.setStateDesc(rootElement.elementTextTrim("stateDesc"));
            jyYou9.setAuthCode(rootElement.elementTextTrim("authCode"));


            Iterator it = rootElement.elementIterator("user"); // 获取子节点user下的子节点

            Element userElement = (Element) it.next();
            JyUser jyUser = JyUser.getInstance();
            jyUser.setUserID(userElement.elementTextTrim("userID"));
            jyUser.setUsername(userElement.elementTextTrim("username"));
            jyUser.setNickname(userElement.elementTextTrim("nickname"));
            jyUser.setPasswordHash(userElement.elementTextTrim("passwordHash"));
            jyUser.setGendar(userElement.elementTextTrim("gendar"));
            jyUser.setRegDate(userElement.elementTextTrim("regDate"));
            jyUser.setRegTime(userElement.elementTextTrim("regTime"));
            jyUser.setSID(userElement.elementTextTrim("sID"));
            jyUser.setPwdTension(userElement.elementTextTrim("pwdTension"));
            jyUser.setIsMain(userElement.elementTextTrim("isMain"));
            jyUser.setIsToken(userElement.elementTextTrim("isToken"));
            jyUser.setFcm(userElement.elementTextTrim("fcm"));
            jyUser.setIdcard(userElement.elementTextTrim("idcard"));
            jyUser.setIsTrialUser(userElement.elementTextTrim("isTrialUser"));
            jyUser.setIsVIPUser(userElement.elementTextTrim("isVIPUser"));
            jyUser.setUserKey(userElement.elementTextTrim("userKey"));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取手机设备信息
     */

    @SuppressLint("NewApi")
    private static JySdkConfigInfo getSystemInfo(Context context, JySdkConfigInfo jySdkConfigInfo) {

        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }


        String imei = "";
        String iccid = "";
        String imsi = "";
        String model = "";
        String os = "";
        String cpu = "";
        String hz = "";
        String ram = "";
        String rom = "";
        String gpu = "";
        String networkType = "";
        String networkProvider = "";
        String phoneNum = "";
        String manufacturer = "";
        String root = "";
        String resolvingPower = "";
        String androidId = "";
        String userAgent = "";
        String uuid = "";
        String openUdid = "";
        String mac = "";


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            //android 10 用androidId当唯一标识
            imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) == null ? "" : Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        } else {
            //小于android 10 用imei当唯一标识
            imei = tm.getDeviceId() == null ? "" : tm.getDeviceId();
        }

        iccid = tm.getSimSerialNumber() == null ? "" : tm.getSimSerialNumber();
        imsi = tm.getSubscriberId() == null ? "" : tm.getSubscriberId();
        model = Build.MODEL;
        os = Build.VERSION.RELEASE;
        cpu = getCpuName();
        hz = getMaxCpuFreq();
        ram = formatSize(getTotalMemory());
        rom = formatSize(getTotalInternalMemorySize());
        gpu = mGpu;
        networkType = getNetworkType(context);
        networkProvider = getNetworkProvider(imsi);
        phoneNum = tm.getLine1Number() == null ? "" : tm.getLine1Number();
//        phoneNum = "13761091371";
        manufacturer = Build.MANUFACTURER;
        root = isRoot() ? "1" : "0";
        resolvingPower = getResolvingPower(context);
        androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID) == null ? "" : Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        userAgent = getUserAgent(context);
        Log.d("eeeee", "androidId=" + androidId);
        Log.d("eeeee", "UA=" + userAgent);
        uuid = getUUID(imei, iccid, androidId);
        Log.d("eeeee", "uuid=" + uuid);

        openUdid = getOpenUdid(context);
        Log.d("eeeee", "openUdid=" + openUdid);
        mac = getMac(context);
        Log.d("eeeee", "mac=" + mac);

        jySdkConfigInfo.setiMei(imei);
        jySdkConfigInfo.setIccid(iccid);
        jySdkConfigInfo.setImsi(imsi);
        jySdkConfigInfo.setModel(model);
        jySdkConfigInfo.setOs(os);
        jySdkConfigInfo.setCpu(cpu);
        jySdkConfigInfo.setHz(hz);
        jySdkConfigInfo.setRam(ram);
        jySdkConfigInfo.setRom(rom);
        jySdkConfigInfo.setGpu(gpu);
        jySdkConfigInfo.setNetworkType(networkType);
        jySdkConfigInfo.setNetworkProvider(networkProvider);
        jySdkConfigInfo.setPhoneNum(phoneNum);
        jySdkConfigInfo.setManufacturer(manufacturer);
        jySdkConfigInfo.setRoot(root);
        jySdkConfigInfo.setResolvingPower(resolvingPower);
        jySdkConfigInfo.setAndroidId(androidId);
        jySdkConfigInfo.setUserAgent(userAgent);
        jySdkConfigInfo.setUuid(uuid);
        jySdkConfigInfo.setOpenUdid(openUdid);
        jySdkConfigInfo.setMac(mac);
        return jySdkConfigInfo;

    }

    private static String getMac(Context context) {
//        String mac: String? = ""
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getMacFromHardware();
        }
        return "";
    }


    /**
     * Android 6.0 之前（不包括6.0）获取mac地址
     * 必须的权限 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * @param context * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info == null) {
            return "";
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }


    /**
     * Android 6.0-Android 7.0 获取mac地址
     */
    private static String getMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/wlan0/address")
                    .toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String loadFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * Android 7.0之后获取Mac地址
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET"></uses-permission>
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            ArrayList<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());


            for (NetworkInterface nif : all) {
                if (!nif.getName().equals("wlan0"))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) return "";
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "02:00:00:00:00:00";
    }


    /*
获取openUdid

*/
    private static String getOpenUdid(Context context) {
        OpenUDID_manager.sync(context);
        if (OpenUDID_manager.isInitialized()) {
            return OpenUDID_manager.getOpenUDID();

        }
        return "";
    }


    /*
    获取uuid

    */
    private static String getUUID(String imei, String iccid, String androidId) {
//      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//      final String tmDevice, tmSerial, androidId;
//      tmDevice = "" + tm.getDeviceId();
//      tmSerial = "" + tm.getSimSerialNumber();
//      androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) imei.hashCode() << 32) | iccid.hashCode());
        return deviceUuid.toString();

    }

    /*获取UserAgent*/
    public static String getUserAgent(Context content) {
        WebView wv = new WebView(content);
        WebSettings settings = wv.getSettings();
        return settings.getUserAgentString();
    }


    private static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            br.close();
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }


    private static String formatSize(long size) {
        String suffix = null;
        float fSize = 0;

        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    private static Long getTotalMemory() {
        BufferedReader localBufferedReader;
        String str2 = null;
        long tempLong = 0l;
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            String[] array = str2.split("\\:", 2);
            String[] array2 = array[1].trim().split("k", 2);
            tempLong = Integer.parseInt(array2[0].trim()) * 1000;
            localBufferedReader.close();
        } catch (IOException e) {

        }
        return tempLong;
    }

    private static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }


    private static String getNetworkType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
        String networkType = "";
        if (networkinfo != null) {
            networkType = networkinfo.getTypeName();
        }

        return networkType;
    }

    private static String getNetworkProvider(String imsi) {
        String providersName = "";
        if (imsi.startsWith("46000")
                || imsi.startsWith("46002")) {
            providersName = "中国移动";
        } else if (imsi.startsWith("46001")) {
            providersName = "中国联通";
        } else if (imsi.startsWith("46003")) {
            providersName = "中国电信";
        } else {
            providersName = "无sim卡";
        }
        return providersName;

    }

    private static boolean isRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                // process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static String getResolvingPower(Context context) {
        DisplayMetrics dm = new DisplayMetrics();

        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width + "," + height;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mGpu = gl.glGetString(GL10.GL_VERSION);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }


    /**
     * 检查用户名是否符合规则
     * <p>
     * //     * @param username
     */
//    public static void checkUsername(String username)
//            throws IllegalArgumentException {
//
//        if (isBlankOrNull(username)) {
//            throw new IllegalArgumentException(JyConstants.USERNAME_IS_EMPTY);
//        } else if (!doRegularExpression(JyConstants.USERNAME_REGRXP, username)) {
//            throw new IllegalArgumentException(JyConstants.USERNAME_FORMERR);
//        }
//
//    }
    public static boolean checkUsername(Context context, String username) {
        if (isBlankOrNull(username)) {
            Toast.makeText(context, JyConstants.USERNAME_IS_EMPTY, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!doRegularExpression(JyConstants.USERNAME_REGRXP, username)) {
            Toast.makeText(context, JyConstants.USERNAME_FORMERR, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 检查密码是否符合规则
     *
     * @param password
     */
    public static boolean checkPwd(Context context, String password) {

        if (isBlankOrNull(password)) {
            Toast.makeText(context, JyConstants.PWD_IS_EMPTY, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!doRegularExpression(JyConstants.PWD_REGRXP_LOGIN, password)) {
            Toast.makeText(context, JyConstants.PWD_FORMERR, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

//        if (isBlankOrNull(password)) {
//            throw new IllegalArgumentException(JyConstants.PWD_IS_EMPTY);
//        } else if (!doRegularExpression(JyConstants.PWD_REGRXP_LOGIN, password)) {
//            throw new IllegalArgumentException(JyConstants.PWD_FORMERR);
//        }
    }


    /**
     * 判断字符串是否为空或null
     *
     * @param text 待判断字符串
     * @return boolean
     */
    public static boolean isBlankOrNull(String text) {
        return (text == null || text.trim().equals("")) ? true : false;
    }


    /**
     * 正则表达式校验
     *
     * @param reg 正则表达式
     * @param str 待校验字符串
     * @return
     */
    private static boolean doRegularExpression(String reg, String str) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 获取本地IP ipv4
     *
     * @return
     */
    public static String getLocalIp() {
        String addr = JyConstants.DEFAULT_LOC_ADDR;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress()
                        // && inetAddress instanceof Inet6Address) {
                        addr = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addr;
    }

    /**
     * MD5加密
     */
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {

        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * J 转换byte到16进制
     *
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    // MessageDigest 为 JDK 提供的加密类
    public static String MD5Encode(String origin) {

        if (origin == null)
            return null;
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }


    /**
     * 检查电话号码是否符合规则
     *
     * @param telphone
     */
    public static boolean checkTelphone(Context context, String telphone) {
        if (isBlankOrNull(telphone)) {
            Toast.makeText(context, JyConstants.TELPHONE_IS_EMPTY, Toast.LENGTH_SHORT).show();
            return false;
        } else if (!doRegularExpression(JyConstants.TELPHONE_REGRXP, telphone)) {
            Toast.makeText(context, JyConstants.TELPHONE_FORMERR, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
//        ==
//        if (isBlankOrNull(telphone)) {
//            throw new IllegalArgumentException(JyConstants.TELPHONE_IS_EMPTY);
//        } else if (!doRegularExpression(JyConstants.TELPHONE_REGRXP, telphone)) {
//            throw new IllegalArgumentException(JyConstants.TELPHONE_FORMERR);
//        }
    }

    /**
     * 随机数对象
     */


    /**
     * 生成20位数字字符串（14位年月日时分秒 + 6位随机数 = 20位纯数字字符串）
     *
     * @return String 20位数字字符串
     */
    public static String getOrderId_20() {
        final Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(20);
        sb.append(getNow_yyyyMMddHHmmss());
        sb.append(100000 + RANDOM.nextInt(900000));
        return sb.toString();
    }


    /**
     * 生成12位随机注册帐号（jy + 7位分秒毫秒mmssSSS + 3位随机数 = 12位英文+数字字符串）
     *
     * @return String 12位数字字符串
     */
    public static String getUserName_12() {
        final Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(20);
        sb.append("jy");
        sb.append(new SimpleDateFormat("mmssSSS").format(new Date()));
        sb.append(100 + RANDOM.nextInt(900));
        return sb.toString();
    }

    /**
     * 得到今天日期（yyyyMMddHHmmss），例如 20080326020101
     *
     * @return String 今天日期（yyyyMMddHHmmss）
     */
    public static String getNow_yyyyMMddHHmmss() {

        return new SimpleDateFormat(
                "yyyyMMddHHmmss").format(new Date());
    }


    /**
     * 生成随机数字和字母的密码
     */
    public static String getRandomPwd(int length) {

        String val = "";
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    /**
     * 将单位分的金额转换成单位元的金额（315->3.15）
     *
     * @param fen 待转换的金额
     * @return String 已被转换的金额
     */
    public static String convertFenToYuan(int fen) {
        return String.valueOf(new BigDecimal(fen).divide(new BigDecimal("100"),
                2, BigDecimal.ROUND_HALF_UP));
    }


    /**
     * 判断app是否安装是否可用
     *
     * @param context
     * @return
     */
    public static boolean isAppAvilible(Context context, String packageName, int versionCode) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName) && !packageName.equals("com.you9.sdk")) {
                    return true;
                } else if (pn.equals(packageName) && pn.equals("com.you9.sdk")) {
                    if (pinfo.get(i).versionCode == versionCode) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            screenHeight = wm.getDefaultDisplay().getHeight();
            screenWidth = wm.getDefaultDisplay().getWidth();
        }
        return screenHeight;
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            screenHeight = wm.getDefaultDisplay().getHeight();
            screenWidth = wm.getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void getPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            (Activity) context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            JyConstants.READ_CONTACTS_REQUEST);
        } else {
            copyAssets(context, JyConstants.WXPAY_JY_NAME);
        }
    }

    public static void copyAssets(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        InputStream in;
        OutputStream out;
        try {
            in = assetManager.open(filename);
            String outFileName = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
            String copyName = System.currentTimeMillis() + JyConstants.WXPAY_JY_NAME;
            File outFile = new File(outFileName, copyName);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            out.flush();
            out.close();
            File file = new File(outFileName, copyName);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // >7.0时 用 provider 共享
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", new File(outFileName,
                                copyName));
                intent.setDataAndType(contentUri,
                        "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file),
                        "application/vnd.android.package-archive");
            }
            context.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * drawable转化成bitmap的方法
     *
     * @param drawable 需要转换的Drawable
     */
    public static byte[] drawableToBytes(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /**
     * byte[]转化成bitmap的方法
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    public static void saveUserInfo(boolean isAutoLogin, boolean isRemember) {
        userDao = JyPlatform.getDaoSession().getJyUserDao();
        you9Dao = JyPlatform.getDaoSession().getJyYou9Dao();

        if (isAutoLogin || isRemember) {
            JyYou9 oldJyYou9 = you9Dao.queryBuilder().build().unique();
            JyUser oldJyUser = userDao.queryBuilder().build().unique();
            if (oldJyYou9 != null) {
                JyYou9.getInstance().setId(oldJyYou9.getId());

            }
            if (oldJyUser != null) {
                JyUser.getInstance().setId(oldJyUser.getId());

            }
            JyUser.getInstance().setAutoLogin(isAutoLogin);
            JyYou9.getInstance().setGreenDaoUid(userDao.insertOrReplace((JyUser.getInstance())));
            you9Dao.insertOrReplace(JyYou9.getInstance());
        } else {
            userDao.deleteAll();
            you9Dao.deleteAll();
        }


    }


    /*新闻头条数据上报
     *
     * @param dataUploadType  数据上报类型 包括register  login
     * @param type       操作类型 （weixin, qq, mobile, username等）
     * @param isSuccess  时间是否成功（注册  登录 成功（）/失败）
     *
     * */
    public static void touTiaoDataUpload(Context context, String dataUploadType, String type, boolean isSuccess, String param) {
        Log.d("eeeee", "touTiaoDataUploadType=" + dataUploadType);


        if (JySdkConfigInfo.getInstance().getReportType().equals(JyPromotionChannelType.API_CHANNEL_TYPE_TOUTIAO.getCode())
                || JySdkConfigInfo.getInstance().getReportType().equals(JyPromotionChannelType.API_CHANNEL_TYPE_UC.getCode())) {


//            推广渠道API上报

            if (isSuccess) {
                String uploadType = dataUploadType;
                if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode())) {
                    Log.d("eeeee", "jinruIf");
                    uploadType = JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_RETAIN.getCode();
                }
                new JyAppRequest(context, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                }).promotionApiRequest(uploadType, "oaid");

            }


        } else if (JySdkConfigInfo.getInstance().getReportType().equals(JyPromotionChannelType.SDK_CHANNEL_TYPE_TOUTIAO.getCode())) {
//            头条推广渠道SDK上报
            if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ACTIVATION.getCode())) {
                //激活
                Log.d("eeeee", "头条激活");
                TeaAgent.init(TeaConfigBuilder.create(context)
                        .setAppName(JySdkConfigInfo.getInstance().getGameName())
                        .setChannel(JySdkConfigInfo.getInstance().getChannelId())
                        .setAid(JySdkConfigInfo.getInstance().getTouTiaoAppId())
                        .createTeaConfig());

            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode())) {
//                注册
                EventUtils.setRegister(type, isSuccess);
            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode())) {
//                登录
                EventUtils.setLogin(type, isSuccess);
                if (isSuccess) {
                    TeaAgent.setUserUniqueID(JyUtils.MD5Encode(param));
                }

            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_UPDATE_LEVEL.getCode())) {
//                升级
                EventUtils.setUpdateLevel(Integer.parseInt(param));

            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_CREATE_ROLE.getCode())) {
//                创角
                try {
                    JSONObject uploadParam = new JSONObject();
                    uploadParam.put("gamerole_id", param);
                    AppLogNewUtils.onEventV3("create_gamerole", uploadParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONRESUME.getCode())) {
//                头条onResume
                TeaAgent.onResume(context);
            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONPAUSE.getCode())) {
                TeaAgent.onPause(context);
            }


        } else if (JySdkConfigInfo.getInstance().getReportType().equals(JyPromotionChannelType.SDK_CHANNEL_TYPE_GDT.getCode())) {
//            广点通推广渠道SDK上报
            if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_INIT.getCode())) {
//                初始化
                GDTAction.init(context, JySdkConfigInfo.getInstance().getGdtActionSetId(), JySdkConfigInfo.getInstance().getGdtSecretKey());
            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONRESUME.getCode())) {
//                广点通onResume
                GDTAction.logAction(ActionType.START_APP);

            } else if (dataUploadType.equals(JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode())) {
//                注册
                try {
                    JSONObject actionParam = new JSONObject();
                    actionParam.put("isSuccess", isSuccess);
                    GDTAction.logAction(ActionType.REGISTER, actionParam);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    /*判断string是不是11位全数字*/
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches() || str.length() != 11) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 得到今天日期（yyyyMMddHHmmss），例如 20080326020101
     *
     * @param timeType - 0  时间格式 yyyyMMddHHmmss    1  时间格式 yyyy-MM-dd HH:mm
     * @return String 今天日期（yyyyMMddHHmmss）
     */
    public static String getNowTime(int timeType) {
        switch (timeType) {
            case 0:
                return new SimpleDateFormat(
                        "yyyyMMddHHmmss").format(new Date());


            case 1:
                return new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm").format(new Date());

        }
        return null;
    }

    public static JyWxInfo getWxInfo(String info) {
        try {
            JSONObject obj = new JSONObject(info);
            JyWxInfo jyWxInfo = JyWxInfo.getInstance();
            jyWxInfo.setAppId(obj.getString("appid"));
            jyWxInfo.setNonceStr(obj.getString("noncestr"));
            jyWxInfo.setPackageValue(obj.getString("package"));
            jyWxInfo.setPartnerId(obj.getString("partnerid"));
            jyWxInfo.setPrepayId(obj.getString("prepayid"));
            jyWxInfo.setSign(obj.getString("sign"));
            jyWxInfo.setTimeStamp(obj.getString("timestamp"));
            return jyWxInfo;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存注册信息图片到相册
     */
    public static void saveRegisterInfoPic(Context context, String userName, String password, View view) {
        mContext = context;
//        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_register_info"), null);
//        RelativeLayout rl_register_info = (RelativeLayout) view.findViewById(ResourceUtil.getId(context, "rl_register_info"));

        TextView tv_account = (TextView) view.findViewById(ResourceUtil.getId(context, "tv_account"));
        TextView tv_password = (TextView) view.findViewById(ResourceUtil.getId(context, "tv_password"));
        tv_account.setText(context.getResources().getString(ResourceUtil.getStringId(context, "jy_activity_register_account")) + userName);
        tv_password.setText(context.getResources().getString(ResourceUtil.getStringId(context, "jy_activity_register_pwd")) + password);

        //将view生成图片
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

//        将图片保存到相册
        new Thread(new Runnable() {
            @Override
            public void run() {
                String filePath = Environment.getExternalStorageDirectory().getPath();
                File file = new File(filePath + "/DCIM/Camera/" + getRandomPwd(6) + ".png");
                try {
                    file.createNewFile();
                    FileOutputStream fOut = null;
                    fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    Message msg = Message.obtain();
                    msg.obj = file.getPath();
                    handler.sendMessage(msg);
                    fOut.flush();
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


//        return true;


    }

    /**
     * 隐藏手机号中间四位
     */

    public static String hidePhoneNum(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;

    }


}
