package com.web.sys.controller;

import com.base.web.response.dto.R;
import com.web.sys.authentication.annotation.CurrLoginUser;
import com.web.sys.authentication.annotation.Permit;
import com.web.sys.authentication.user.LoginUser;
import com.web.sys.dto.base.IdBody;
import com.web.sys.dto.user.req.UserLoginReqDto;
import com.web.sys.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suyh
 * @since 2024-09-02
 */
@Tag(name = "用户")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final IUserService userService;

    @Operation(summary = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Permit(required = false)
    public R<String> login(@RequestBody @Validated UserLoginReqDto userLogin) {
        String token = userService.login(
                userLogin.getUsername(), userLogin.getPassword(), userLogin.getCode());
        return R.ofSuccess(token);
    }

    @Operation(summary = "用户登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public R<Boolean> logout(@Parameter(hidden = true) @CurrLoginUser(required = false) LoginUser loginUser) {
        userService.logout(loginUser);
        return R.ofSuccess();
    }

    @Operation(summary = "重置2FA：用户ID")
    @RequestMapping(value = "/reset/twoFactorAuthKey/byId", method = RequestMethod.POST)
    public R<String> resetTwoFactorAuthKeyById(@RequestBody @Validated IdBody idBody) {
        String fa = userService.resetTwoFactorAuthKey(idBody.getId());
        return R.ofSuccess(fa);
    }

    @Operation(summary = "重置2FA：当前登录用户")
    @RequestMapping(value = "/reset/twoFactorAuthKey/self", method = RequestMethod.POST)
    public R<String> resetTwoFactorAuthKeySelf(
            @Parameter(hidden = true) @CurrLoginUser LoginUser loginUser) {
        String key = userService.resetTwoFactorAuthKey(loginUser.getId());
        return R.ofSuccess(key);
    }
}
