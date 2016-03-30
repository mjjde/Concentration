package com.open_source.joker.concentration.location.geo;

/**
 * 文件名：com.open_source.joker.concentration.location.geo
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public interface GeoService {

    /**
     * 添加监听器。
     *
     * @param listener
     *            监听器
     * @return 操作是否有效
     */
    boolean addListener(GeoListener listener);

    /**
     * 移除监听器。
     *
     * @param listener
     *            监听器
     * @return 操作是否有效
     */
    boolean removeListener(GeoListener listener);

    /**
     * 请求位置参数。
     * 异步
     */
    void requestGeoParams();
}
