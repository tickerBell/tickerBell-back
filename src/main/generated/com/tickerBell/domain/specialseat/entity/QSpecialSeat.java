package com.tickerBell.domain.specialseat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpecialSeat is a Querydsl query type for SpecialSeat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpecialSeat extends EntityPathBase<SpecialSeat> {

    private static final long serialVersionUID = -1609334887L;

    public static final QSpecialSeat specialSeat = new QSpecialSeat("specialSeat");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isSpecialSeatA = createBoolean("isSpecialSeatA");

    public final BooleanPath isSpecialSeatB = createBoolean("isSpecialSeatB");

    public final BooleanPath isSpecialSeatC = createBoolean("isSpecialSeatC");

    public QSpecialSeat(String variable) {
        super(SpecialSeat.class, forVariable(variable));
    }

    public QSpecialSeat(Path<? extends SpecialSeat> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpecialSeat(PathMetadata metadata) {
        super(SpecialSeat.class, metadata);
    }

}

