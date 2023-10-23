package com.tickerBell.domain.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNonMember is a Querydsl query type for NonMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNonMember extends EntityPathBase<NonMember> {

    private static final long serialVersionUID = -1165634630L;

    public static final QNonMember nonMember = new QNonMember("nonMember");

    public final com.tickerBell.domain.common.QBaseEntity _super = new com.tickerBell.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public final StringPath phone = createString("phone");

    public QNonMember(String variable) {
        super(NonMember.class, forVariable(variable));
    }

    public QNonMember(Path<? extends NonMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNonMember(PathMetadata metadata) {
        super(NonMember.class, metadata);
    }

}

