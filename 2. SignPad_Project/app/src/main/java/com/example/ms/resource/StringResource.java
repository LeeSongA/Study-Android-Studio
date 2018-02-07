package com.example.ms.resource;

import com.samsung.android.sdk.pen.Spen;

/**
 * Created by ms on 2018-02-07.
 */

public class StringResource {
    public final static String MARKETURL = "market://details?id=" + Spen.getSpenPackageName();
    public final static String BASE = "data:image/png;base64,";
    public final static String DEVICE_NOT_SUPPORTED_MSG = "이 기기는 Spen을 지원하지 않습니다.";
    public final static String LIBRARY_NOT_INSTALLED_MSG = "Spen software 설치가 필요합니다.";
    public final static String LIBRARY_UPDATE_IS_REQUIRED_MSG = "업데이트가 필요합니다.";
    public final static String LIBRARY_UPDATE_IS_RECOMMENDED_MSG = "업데이트를 추천합니다.";
}
