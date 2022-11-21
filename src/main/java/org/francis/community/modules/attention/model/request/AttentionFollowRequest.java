package org.francis.community.modules.attention.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Franc1s
 * @date 2022/11/21
 * @apiNote
 */
@Data
@ApiModel("关注参数")
public class AttentionFollowRequest {
    @ApiModelProperty("被关注id")
    private Long userId;
    @ApiModelProperty("是否关注(true:关注 false:取消关注)")
    private Boolean isFollow;
}
