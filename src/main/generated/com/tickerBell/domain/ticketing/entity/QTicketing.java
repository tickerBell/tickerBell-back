package com.tickerBell.domain.ticketing.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketing is a Querydsl query type for Ticketing
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketing extends EntityPathBase<Ticketing> {

    private static final long serialVersionUID = -2127731015L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketing ticketing = new QTicketing("ticketing");

    public final com.tickerBell.domain.common.QBaseEntity _super = new com.tickerBell.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final com.tickerBell.domain.event.entity.QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final com.tickerBell.domain.member.entity.QMember member;

    public final com.tickerBell.domain.member.entity.QNonMember nonMember;

    public final ListPath<com.tickerBell.domain.selectedSeat.entity.SelectedSeat, com.tickerBell.domain.selectedSeat.entity.QSelectedSeat> selectedSeatList = this.<com.tickerBell.domain.selectedSeat.entity.SelectedSeat, com.tickerBell.domain.selectedSeat.entity.QSelectedSeat>createList("selectedSeatList", com.tickerBell.domain.selectedSeat.entity.SelectedSeat.class, com.tickerBell.domain.selectedSeat.entity.QSelectedSeat.class, PathInits.DIRECT2);

    public QTicketing(String variable) {
        this(Ticketing.class, forVariable(variable), INITS);
    }

    public QTicketing(Path<? extends Ticketing> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketing(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketing(PathMetadata metadata, PathInits inits) {
        this(Ticketing.class, metadata, inits);
    }

    public QTicketing(Class<? extends Ticketing> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new com.tickerBell.domain.event.entity.QEvent(forProperty("event"), inits.get("event")) : null;
        this.member = inits.isInitialized("member") ? new com.tickerBell.domain.member.entity.QMember(forProperty("member")) : null;
        this.nonMember = inits.isInitialized("nonMember") ? new com.tickerBell.domain.member.entity.QNonMember(forProperty("nonMember")) : null;
    }

}

