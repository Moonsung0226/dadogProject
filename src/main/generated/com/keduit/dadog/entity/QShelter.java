package com.keduit.dadog.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QShelter is a Querydsl query type for Shelter
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShelter extends EntityPathBase<Shelter> {

    private static final long serialVersionUID = -357655020L;

    public static final QShelter shelter = new QShelter("shelter");

    public final StringPath shelAddr = createString("shelAddr");

    public final StringPath shelCity = createString("shelCity");

    public final StringPath shelNm = createString("shelNm");

    public final NumberPath<Long> shelNo = createNumber("shelNo", Long.class);

    public QShelter(String variable) {
        super(Shelter.class, forVariable(variable));
    }

    public QShelter(Path<? extends Shelter> path) {
        super(path.getType(), path.getMetadata());
    }

    public QShelter(PathMetadata metadata) {
        super(Shelter.class, metadata);
    }

}

