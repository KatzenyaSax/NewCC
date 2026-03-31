package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody AccountCreateRequest request) {
        return R.ok(accountService.create(request));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateRequest request) {
        request.setId(id);
        accountService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R<AccountVO> getById(@PathVariable Long id) {
        return R.ok(accountService.getById(id));
    }

    @PutMapping("/{id}/reset-password")
    public R<Void> resetPassword(@PathVariable Long id, @Valid @RequestBody AccountResetPasswordRequest request) {
        request.setId(id);
        accountService.resetPassword(request);
        return R.ok();
    }

    @PutMapping("/{id}/change-password")
    public R<Void> changePassword(@PathVariable Long id, @Valid @RequestBody AccountChangePasswordRequest request) {
        request.setId(id);
        accountService.changePassword(request);
        return R.ok();
    }

    @GetMapping("/page")
    public R<PageResult<AccountVO>> page(@Valid AccountPageRequest request) {
        return R.ok(accountService.page(request));
    }
}
