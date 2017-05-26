package com.tjstudy.wifidemo;

import android.Manifest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;

import com.tjstudy.wifidemo.base.BaseActivity;
import com.tjstudy.wifidemo.base.OnPermissionCallbackListener;

import java.util.List;

/**
 * 切换到指定wifi
 */
public class MainActivity extends BaseActivity {

    private WifiManager mWifiManager;

    private void initPermission() {
        onRequestPermission(new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION
        }, new OnPermissionCallbackListener() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
            }
        });
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        initPermission();

        findViewById(R.id.btn_change_wifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeWifi();
            }
        });
    }

    @Override
    public void installData() {
        mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    }

    private String aimWifiName = "H10";
    private String aimWifiPwd = "honmax2005";

    /**
     * 切换wifi
     */
    private void changeWifi() {
//        WifiConfiguration wifiNewConfiguration = createWifiInfo(aimWifiName, aimWifiPwd);//使用wpa2的wifi加密方式
//        int newNetworkId = wifiManager.addNetwork(wifiNewConfiguration);
//        Log.e("MainActivity", "newNetworkId:" + newNetworkId);
//        /**
//         * addNetwork打印结果:
//         * 1、未保存密码             ==32
//         * 2、在代码里面保存了密码     ==32--意思是在代码里面add了 没有remove的情况
//         * 3、手动连接了密码          ==-1
//         */
//        boolean enableNetwork = wifiManager.enableNetwork(newNetworkId, true);
//        Log.e("MainActivity", "enableNetwork:" + enableNetwork);
//        /**
//         * enableNetwork打印结果:
//         * 1、未保存密码             切换成功
//         * 2、在代码里面保存了密码     切换成功
//         * 3、手动连接了密码          app无响应了
//         */

        //最终解决方案
        WifiConfiguration wifiNewConfiguration = createWifiInfo(aimWifiName, aimWifiPwd);//使用wpa2的wifi加密方式
        int newNetworkId = mWifiManager.addNetwork(wifiNewConfiguration);
        if (newNetworkId == -1) {
            Log.e("MainActivity", "操作失败,需要您到手机wifi列表中取消对设备连接的保存");
        } else {
            boolean enableNetwork = mWifiManager.enableNetwork(newNetworkId, true);
            if (!enableNetwork) {
                Log.e("MainActivity", "设备账号信息有误");
            } else {
                Log.e("MainActivity", "切换到指定wifi成功");
            }
        }
    }

    /**
     * 创建 WifiConfiguration，这里创建的是wpa2加密方式的wifi
     *
     * @param ssid     wifi账号
     * @param password wifi密码
     * @return
     */
    public WifiConfiguration createWifiInfo(String ssid, String password) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.status = WifiConfiguration.Status.ENABLED;
        return config;
    }
}
