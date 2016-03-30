package com.open_source.joker.concentration.location.geo;

import java.util.Map;

/**
 * 文件名：com.open_source.joker.concentration.location.geo
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public interface GeoListener {
    /**
     * 请求位置参数完成。
     *
     * @param params
     *            位置参数
     */
    void onRequestGeoParamsFinish(Map<String, String> params);
}
