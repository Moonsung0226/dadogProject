package com.keduit.dadog.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QKakaoMember is a Querydsl query type for KakaoMember
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QKakaoMember extends EntityPathBase<KakaoMember> {

    private static final long serialVersionUID = -1322354099L;

    public static final QKakaoMember kakaoMember = new QKakaoMember("kakaoMember");

    public final StringPath email = createString("email");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath username = createString("username");

    public QKakaoMember(String variable) {
        super(KakaoMember.class, forVariable(variable));
    }

    public QKakaoMember(Path<? extends KakaoMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKakaoMember(PathMetadata metadata) {
        super(KakaoMember.class, metadata);
    }

}

