package com.tickerBell.domain.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = 444778937L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEvent event = new QEvent("event");

    public final com.tickerBell.domain.common.QBaseEntity _super = new com.tickerBell.domain.common.QBaseEntity(this);

    public final DateTimePath<java.time.LocalDateTime> availablePurchaseTime = createDateTime("availablePurchaseTime", java.time.LocalDateTime.class);

    public final EnumPath<Category> category = createEnum("category", Category.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> endEvent = createDateTime("endEvent", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isAdult = createBoolean("isAdult");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.tickerBell.domain.member.entity.QMember member;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> normalPrice = createNumber("normalPrice", Integer.class);

    public final StringPath place = createString("place");

    public final NumberPath<Integer> premiumPrice = createNumber("premiumPrice", Integer.class);

    public final NumberPath<Integer> remainSeat = createNumber("remainSeat", Integer.class);

    public final NumberPath<Float> saleDegree = createNumber("saleDegree", Float.class);

    public final com.tickerBell.domain.specialseat.entity.QSpecialSeat specialSeat;

    public final DateTimePath<java.time.LocalDateTime> startEvent = createDateTime("startEvent", java.time.LocalDateTime.class);

    public final NumberPath<Integer> totalSeat = createNumber("totalSeat", Integer.class);

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QEvent(String variable) {
        this(Event.class, forVariable(variable), INITS);
    }

    public QEvent(Path<? extends Event> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEvent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEvent(PathMetadata metadata, PathInits inits) {
        this(Event.class, metadata, inits);
    }

    public QEvent(Class<? extends Event> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.tickerBell.domain.member.entity.QMember(forProperty("member")) : null;
        this.specialSeat = inits.isInitialized("specialSeat") ? new com.tickerBell.domain.specialseat.entity.QSpecialSeat(forProperty("specialSeat")) : null;
    }

}

