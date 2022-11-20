package org.francis.community.modules.attention.controller;


import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author francis
 * @since 2022-11-20
 */
@Api(tags = "用户关注模块接口")
@RestController
@RequestMapping("/attention")
@RequiredArgsConstructor
public class AttentionController {

}

