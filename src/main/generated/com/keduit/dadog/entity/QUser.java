package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1742524164L;

    public static final QUser user = new QUser("user");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final ListPath<Board, QBoard> boardList = this.<Board, QBoard>createList("boardList", Board.class, QBoard.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createTime = _super.createTime;

    public final ListPath<Lost, QLost> lostList = this.<Lost, QLost>createList("lostList", Lost.class, QLost.class, PathInits.DIRECT2);

    public final ListPath<Protect, QProtect> protectList = this.<Protect, QProtect>createList("protectList", Protect.class, QProtect.class, PathInits.DIRECT2);

    public final EnumPath<com.keduit.dadog.constant.Role> role = createEnum("role", com.keduit.dadog.constant.Role.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateTime = _super.updateTime;

    public final StringPath userAddr = createString("userAddr");

    public final StringPath userEmail = createString("userEmail");

    public final StringPath userId = createString("userId");

    public final StringPath userName = createString("userName");

    public final NumberPath<Long> userNo = createNumber("userNo", Long.class);

    public final StringPath userPwd = createString("userPwd");

    public final StringPath userTel = createString("userTel");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

