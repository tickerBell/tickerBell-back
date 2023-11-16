package com.tickerBell.domain.utils;

import com.tickerBell.global.exception.CustomException;
import com.tickerBell.global.exception.ErrorCode;

public class SeatPriceCalculator {

    public static float getSeatPrice(float saleDegree, int price) {
        float seatPrice = 0;
        if (saleDegree == 0) { // 할인 x
            seatPrice = price;
        } else if (saleDegree >= 1.0) { // 상수값 할인
            seatPrice = price - saleDegree;
        } else if (saleDegree < 1.0 && saleDegree > 0) { // 퍼센트 할인
            seatPrice = price - (price * saleDegree);
        } else {
            // saleDegree 가 위 3개에 해당하지 않을 때
            throw new CustomException(ErrorCode.SALE_DEGREE_NOT_VALID_FORMAT);
        }
        return seatPrice;
    }
}
