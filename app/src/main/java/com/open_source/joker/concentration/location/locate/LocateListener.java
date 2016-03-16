package com.open_source.joker.concentration.location.locate;

import com.open_source.joker.concentration.location.model.CoordModel;

import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public interface LocateListener {

    void onLocateFinish(List<CoordModel> resultList);
}
