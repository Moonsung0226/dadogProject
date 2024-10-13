package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 1813148633L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QBoard board;

    //inherited
    public final DatePath<java.time.LocalDate> createTime = _super.createTime;

    public final StringPath replyContent = createString("replyContent");

    public final NumberPath<Long> replyNo = createNumber("replyNo", Long.class);

    public final StringPath replyWriter = createString("replyWriter");

    //inherited
    public final DatePath<java.time.LocalDate> updateTime = _super.updateTime;

    public final QUser user;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

