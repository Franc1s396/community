package org.francis.community.modules.article.mapper;

import org.francis.community.modules.article.model.Like;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Franc1s
 * @date 2022/11/20
 * @apiNote
 */
@SpringBootTest
class LikeMapperTest {
    @Autowired
    LikeMapper likeMapper;

    @Test
    void updateBatchLike() {
        List<Like> likeList = new ArrayList<>();
//        Like like = new Like();
//        like.setArticleId(1L);
//        like.setUserId(1L);
//        likeList.add(like);
//        Like like2 = new Like();
//        like2.setArticleId(2L);
//        like2.setUserId(1L);
//        likeList.add(like2);
        likeMapper.updateBatchLike(likeList);
    }
}