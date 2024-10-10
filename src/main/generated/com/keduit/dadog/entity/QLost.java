package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLost is a Querydsl query type for Lost
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLost extends EntityPathBase<Lost> {

    private static final long serialVersionUID = -1742795691L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLost lost = new QLost("lost");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final DatePath<java.time.LocalDate> lostDate = createDate("lostDate", java.time.LocalDate.class);

    public final StringPath lostDetail = createString("lostDetail");

    public final StringPath lostFileName = createString("lostFileName");

    public final StringPath lostImgUrl = createString("lostImgUrl");

    public final StringPath lostKind = createString("lostKind");

    public final NumberPath<Long> lostNo = createNumber("lostNo", Long.class);

    public final StringPath lostOriName = createString("lostOriName");

    public final StringPath lostPlace = createString("lostPlace");

    public final StringPath lostTel = createString("lostTel");

    public final StringPath lostWriter = createString("lostWriter");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final QUser user;

    public QLost(String variable) {
        this(Lost.class, forVariable(variable), INITS);
    }

    public QLost(Path<? extends Lost> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLost(PathMetadata metadata, PathInits inits) {
        this(Lost.class, metadata, inits);
    }

    public QLost(Class<? extends Lost> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

