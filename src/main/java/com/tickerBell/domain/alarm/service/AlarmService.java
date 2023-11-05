package com.tickerBell.domain.alarm.service;

import com.tickerBell.domain.alarm.dtos.SaveAlarmRequest;

public interface AlarmService {

    Long saveAlarm(SaveAlarmRequest request);
}
