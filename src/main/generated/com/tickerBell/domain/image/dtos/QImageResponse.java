package com.tickerBell.domain.image.dtos;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.tickerBell.domain.image.dtos.QImageResponse is a Querydsl Projection type for ImageResponse
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QImageResponse extends ConstructorExpression<ImageResponse> {

    private static final long serialVersionUID = -263831093L;

    public QImageResponse(com.querydsl.core.types.Expression<Long> imageId, com.querydsl.core.types.Expression<String> s3Url, com.querydsl.core.types.Expression<String> originImgName, com.querydsl.core.types.Expression<Boolean> isThumbnail) {
        super(ImageResponse.class, new Class<?>[]{long.class, String.class, String.class, boolean.class}, imageId, s3Url, originImgName, isThumbnail);
    }

}

