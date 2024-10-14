package com.keduit.dadog.repository;

import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.Protect;
import com.keduit.dadog.entity.QLost;
import com.keduit.dadog.entity.QProtect;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class ProtectRepositoryCustomImpl implements ProtectRepositoryCustom {

    private JPAQueryFactory queryFactory;
    public ProtectRepositoryCustomImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("kind",searchBy)){
            return QProtect.protect.proKind.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("place",searchBy)){
            return QProtect.protect.proPlace.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("detail",searchBy)){
            return QProtect.protect.proDetail.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("title",searchBy)){
            return QProtect.protect.proTitle.like("%" + searchQuery + "%");
        }
        return null;
    }

    public List<Protect> mainProtectList(){
        List<Protect> result = queryFactory.selectFrom(QProtect.protect)
                .orderBy(QProtect.protect.proDate.desc())
                .limit(4)
                .fetch();
        return result;
    }

    @Override
    public Page<Protect> getProtectListPage(SearchDTO searchDTO, Pageable pageable) {
        List<Protect> result = queryFactory.selectFrom(QProtect.protect)
                .where(searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()))
                .orderBy(QProtect.protect.proNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count)
                .from(QProtect.protect)
                .where(searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }
}
