package com.keduit.dadog.repository;

import com.keduit.dadog.dto.AdoptSearchDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.QAdopt;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class AdoptRepositoryCustomImpl implements AdoptRepositoryCustom{

    private JPAQueryFactory queryFactory;

    //엔티티 매니저
    public AdoptRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    //마감 기한 정렬 DESC = 내림차순,  default or ASC  = 오름차순
    private OrderSpecifier<String> dateDescOrAsc(String searchDescOrAsc){
        if(StringUtils.isEmpty(searchDescOrAsc) || searchDescOrAsc.equals("asc")) {
            return QAdopt.adopt.adoptEdt.asc();
        }
            return QAdopt.adopt.adoptEdt.desc();
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("kind",searchBy)){
            return QAdopt.adopt.adoptKind.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("careName",searchBy)){
            return QAdopt.adopt.adoptCareNm.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("careAddr",searchBy)){
            return QAdopt.adopt.adoptCareAddr.like("%" + searchQuery + "%");
        }
        return null;
    }

    public List<Adopt> mainAdoptList(){
        List<Adopt> result = queryFactory.selectFrom(QAdopt.adopt)
                .orderBy(QAdopt.adopt.adoptEdt.asc())
                .limit(4)
                .fetch();
        return result;
    }

    @Override
    public Page<Adopt> getAdoptListPage(AdoptSearchDTO adoptSearchDTO, Pageable pageable) {

        List<Adopt> result = queryFactory.selectFrom(QAdopt.adopt)
                .where(searchByLike(adoptSearchDTO.getSearchBy(),adoptSearchDTO.getSearchQuery()))
                .orderBy(dateDescOrAsc(adoptSearchDTO.getSearchDescOrAsc()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count)
                .from(QAdopt.adopt)
                .where(searchByLike(adoptSearchDTO.getSearchBy(),adoptSearchDTO.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }
}
