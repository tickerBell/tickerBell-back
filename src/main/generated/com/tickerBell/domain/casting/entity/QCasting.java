package com.tickerBell.domain.casting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCasting is a Querydsl query type for Casting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCasting extends EntityPathBase<Casting> {

    private static final long serialVersionUID = 1144193369L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCasting casting = new QCasting("casting");

    public final StringPath castingName = createString("castingName");

    public final com.tickerBell.domain.event.entity.QEvent event;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QCasting(String variable) {
        this(Casting.class, forVariable(variable), INITS);
    }

    public QCasting(Path<? extends Casting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCasting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCasting(PathMetadata metadata, PathInits inits) {
        this(Casting.class, metadata, inits);
    }

    public QCasting(Class<? extends Casting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new com.tickerBell.domain.event.entity.QEvent(forProperty("event"), inits.get("event")) : null;
    }

}

