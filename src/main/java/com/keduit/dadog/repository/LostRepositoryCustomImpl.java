package com.keduit.dadog.repository;

import com.keduit.dadog.dto.SearchDTO;
import com.keduit.dadog.entity.Adopt;
import com.keduit.dadog.entity.Lost;
import com.keduit.dadog.entity.QAdopt;
import com.keduit.dadog.entity.QLost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class LostRepositoryCustomImpl implements LostRepositoryCustom {

    private JPAQueryFactory queryFactory;
    private UserRepository userRepository;

    public LostRepositoryCustomImpl(EntityManager em) {this.queryFactory = new JPAQueryFactory(em);}

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("kind",searchBy)){
            return QLost.lost.lostKind.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("place",searchBy)){
            return QLost.lost.lostPlace.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("detail",searchBy)){
            return QLost.lost.lostDetail.like("%" + searchQuery + "%");
        }else if(StringUtils.equals("title",searchBy)){
            System.out.println("-------여기");
            return QLost.lost.lostTitle.like("%" + searchQuery + "%");
        }
        return null;
    }

    public List<Lost> mainLostList(){
        List<Lost> result = queryFactory.selectFrom(QLost.lost)
                .orderBy(QLost.lost.lostDate.desc())
                .limit(4)
                .fetch();
        return result;
    }

    @Override
    public Page<Lost> getAdoptListPage(SearchDTO searchDTO, Pageable pageable) {

        List<Lost> result = queryFactory.selectFrom(QLost.lost)
                .where(searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()))
                .orderBy(QLost.lost.lostNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count)
                .from(QLost.lost)
                .where(searchByLike(searchDTO.getSearchBy(), searchDTO.getSearchQuery()))
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    public Page<Lost> getMyLostPage(Pageable pageable, Long userNo) {

        List<Lost> result = queryFactory.selectFrom(QLost.lost)
                .where(QLost.lost.user.userNo.eq(userNo))
                .orderBy(QLost.lost.lostNo.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(Wildcard.count)
                .from(QLost.lost)
                .where(QLost.lost.user.userNo.eq(userNo))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);
    }


}
