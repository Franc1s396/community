package org.francis.community.modules.attention.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.francis.community.core.enums.CodeEnums;
import org.francis.community.core.exception.ServiceException;
import org.francis.community.core.model.AjaxResult;
import org.francis.community.core.model.request.PageQueryRequest;
import org.francis.community.modules.attention.model.Attention;
import org.francis.community.modules.attention.model.request.AttentionFollowRequest;
import org.francis.community.modules.attention.service.AttentionService;
import org.francis.community.modules.user.model.dto.UserDTO;
import org.francis.community.modules.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author francis
 * @since 2022-11-20
 */
@Api(tags = "用户关注模块接口")
@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {

    private final AttentionService attentionService;

    private final UserService userService;

    /**
     * 关注列表
     * 查看某个用户的关注列表
     */
    @GetMapping("/list")
    @ApiOperation("关注列表分页查询")
    public AjaxResult findFollowList(PageQueryRequest pageQueryRequest,
                                     @RequestParam(name = "用户id") Long userId) {
        // author表示被关注者
        IPage<Attention> attentionPageList = attentionService.findFollowList(pageQueryRequest, userId);

        Set<Long> authorIdSet = attentionPageList.getRecords().stream()
                .map(Attention::getAuthorId)
                .collect(Collectors.toSet());
        List<UserDTO> userList = userService.findUserListByIds(new ArrayList<>(authorIdSet));

        IPage<UserDTO> userDTOIPage = attentionPageList.convert(attention -> userList.stream()
                .filter(userDTO -> Objects.equals(userDTO.getId(), attention.getAuthorId()))
                .findFirst()
                .get());
        return AjaxResult.success(userDTOIPage);
    }

    /**
     * 粉丝列表
     * 查看某个用户的粉丝列表
     */
    @GetMapping("/fans/list")
    @ApiOperation("粉丝列表分页查询")
    public AjaxResult findFansList(PageQueryRequest pageQueryRequest, @RequestParam(name = "用户id") Long userId) {
        IPage<Attention> attentionPageList = attentionService.findFansList(pageQueryRequest, userId);

        Set<Long> fansIdSet = attentionPageList.getRecords().stream()
                .map(Attention::getFansId)
                .collect(Collectors.toSet());
        List<UserDTO> userList = userService.findUserListByIds(new ArrayList<>(fansIdSet));

        IPage<UserDTO> userDTOIPage = attentionPageList.convert(attention -> userList.stream()
                .filter(userDTO -> Objects.equals(userDTO.getId(), attention.getFansId()))
                .findFirst()
                .get());
        return AjaxResult.success(userDTOIPage);
    }

    @GetMapping("/common/{userId}")
    @ApiOperation("查看共同关注")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户id", required = true, dataTypeClass = Long.class)
    public AjaxResult findCommonAttention(@PathVariable Long userId) {
        Set<Long> userIds = attentionService.findCommonAttention(userId);
        if (CollectionUtils.isEmpty(userIds)) {
            return AjaxResult.success();
        }
        List<UserDTO> userDTOList = userService.findUserListByIds(new ArrayList<>(userIds));
        return AjaxResult.success(userDTOList);
    }

    /**
     * 关注或取消关注
     * 参数: 被关注人
     * 参数校验: 被关注人是否存在
     */
    @PostMapping("/follow")
    @ApiOperation("关注或取消关注用户")
    public AjaxResult followUser(@RequestBody @Validated AttentionFollowRequest attentionFollowRequest) {
        attentionService.followUser(attentionFollowRequest);
        return AjaxResult.success("关注成功");
    }
}

