package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdopt is a Querydsl query type for Adopt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdopt extends EntityPathBase<Adopt> {

    private static final long serialVersionUID = 1797418143L;

    public static final QAdopt adopt = new QAdopt("adopt");

    public final StringPath adoptAge = createString("adoptAge");

    public final StringPath adoptCareAddr = createString("adoptCareAddr");

    public final StringPath adoptCareNm = createString("adoptCareNm");

    public final StringPath adoptCareTel = createString("adoptCareTel");

    public final StringPath adoptEdt = createString("adoptEdt");

    public final StringPath adoptImgUrl = createString("adoptImgUrl");

    public final StringPath adoptKind = createString("adoptKind");

    public final NumberPath<Long> AdoptNo = createNumber("AdoptNo", Long.class);

    public final StringPath adoptSpecial = createString("adoptSpecial");

    public final StringPath adoptWeight = createString("adoptWeight");

    public final ListPath<Application, QApplication> applicationList = this.<Application, QApplication>createList("applicationList", Application.class, QApplication.class, PathInits.DIRECT2);

    public final EnumPath<com.keduit.dadog.constant.Current> current = createEnum("current", com.keduit.dadog.constant.Current.class);

    public final ListPath<Wish, QWish> wishList = this.<Wish, QWish>createList("wishList", Wish.class, QWish.class, PathInits.DIRECT2);

    public QAdopt(String variable) {
        super(Adopt.class, forVariable(variable));
    }

    public QAdopt(Path<? extends Adopt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdopt(PathMetadata metadata) {
        super(Adopt.class, metadata);
    }

}

