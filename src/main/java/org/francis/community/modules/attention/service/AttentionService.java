package org.francis.community.modules.attention.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.attention.model.Attention;
import com.baomidou.mybatisplus.extension.service.IService;
import org.francis.community.modules.attention.model.request.AttentionFollowRequest;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author francis
 * @since 2022-11-20
 */
public interface AttentionService extends IService<Attention> {

    IPage<Attention> findFollowList(PageQueryRequest pageQueryRequest, Long userId);

    IPage<Attention> findFansList(PageQueryRequest pageQueryRequest, Long userId);

    /**
     * 关注用户
     */
    void followUser(AttentionFollowRequest attentionFollowRequest);

    List<Attention> findFansList(Long userId);

    Set<Long> findCommonAttention(Long userId);
}
