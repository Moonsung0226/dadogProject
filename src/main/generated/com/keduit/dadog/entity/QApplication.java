package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QApplication is a Querydsl query type for Application
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QApplication extends EntityPathBase<Application> {

    private static final long serialVersionUID = -697714945L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QApplication application = new QApplication("application");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final QAdopt adopt;

    public final EnumPath<com.keduit.dadog.constant.AdoptWait> adoptWaitStatus = createEnum("adoptWaitStatus", com.keduit.dadog.constant.AdoptWait.class);

    public final NumberPath<Long> appNo = createNumber("appNo", Long.class);

    //inherited
    public final DatePath<java.time.LocalDate> createTime = _super.createTime;

    //inherited
    public final DatePath<java.time.LocalDate> updateTime = _super.updateTime;

    public final QUser user;

    public QApplication(String variable) {
        this(Application.class, forVariable(variable), INITS);
    }

    public QApplication(Path<? extends Application> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QApplication(PathMetadata metadata, PathInits inits) {
        this(Application.class, metadata, inits);
    }

    public QApplication(Class<? extends Application> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.adopt = inits.isInitialized("adopt") ? new QAdopt(forProperty("adopt")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

