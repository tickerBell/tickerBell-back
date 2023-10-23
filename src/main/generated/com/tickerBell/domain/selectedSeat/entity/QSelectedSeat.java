package com.tickerBell.domain.selectedSeat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSelectedSeat is a Querydsl query type for SelectedSeat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSelectedSeat extends EntityPathBase<SelectedSeat> {

    private static final long serialVersionUID = 721968019L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSelectedSeat selectedSeat = new QSelectedSeat("selectedSeat");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath seatInfo = createString("seatInfo");

    public final NumberPath<Float> seatPrice = createNumber("seatPrice", Float.class);

    public final com.tickerBell.domain.ticketing.entity.QTicketing ticketing;

    public QSelectedSeat(String variable) {
        this(SelectedSeat.class, forVariable(variable), INITS);
    }

    public QSelectedSeat(Path<? extends SelectedSeat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSelectedSeat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSelectedSeat(PathMetadata metadata, PathInits inits) {
        this(SelectedSeat.class, metadata, inits);
    }

    public QSelectedSeat(Class<? extends SelectedSeat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticketing = inits.isInitialized("ticketing") ? new com.tickerBell.domain.ticketing.entity.QTicketing(forProperty("ticketing"), inits.get("ticketing")) : null;
    }

}

