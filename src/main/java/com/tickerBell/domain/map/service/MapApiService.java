package com.tickerBell.domain.map.service;

import com.tickerBell.domain.map.dtos.MapApiRequest;
import com.tickerBell.domain.map.dtos.MapApiResultPath;

import java.util.List;

public interface MapApiService {
    MapApiResultPath callMapApi(MapApiRequest request);
}
