package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProtect is a Querydsl query type for Protect
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProtect extends EntityPathBase<Protect> {

    private static final long serialVersionUID = 1570551806L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProtect protect = new QProtect("protect");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final DatePath<java.time.LocalDate> proDate = createDate("proDate", java.time.LocalDate.class);

    public final StringPath proDetail = createString("proDetail");

    public final StringPath proFileName = createString("proFileName");

    public final StringPath proImgUrl = createString("proImgUrl");

    public final StringPath proKind = createString("proKind");

    public final NumberPath<Long> proNo = createNumber("proNo", Long.class);

    public final StringPath proOriName = createString("proOriName");

    public final StringPath proPlace = createString("proPlace");

    public final StringPath proTel = createString("proTel");

    public final StringPath proTitle = createString("proTitle");

    public final StringPath proWriter = createString("proWriter");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final QUser user;

    public QProtect(String variable) {
        this(Protect.class, forVariable(variable), INITS);
    }

    public QProtect(Path<? extends Protect> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProtect(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProtect(PathMetadata metadata, PathInits inits) {
        this(Protect.class, metadata, inits);
    }

    public QProtect(Class<? extends Protect> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

