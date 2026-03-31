package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.SystemParamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-param")
@RequiredArgsConstructor
public class SystemParamController {

    private final SystemParamService systemParamService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody SystemParamCreateRequest request) {
        return R.ok(systemParamService.create(request));
    }

    @PutMapping("/{key}")
    public R<Void> update(@PathVariable String key, @Valid @RequestBody SystemParamUpdateRequest request) {
        request.setParamKey(key);
        systemParamService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{key}")
    public R<Void> delete(@PathVariable String key) {
        systemParamService.delete(key);
        return R.ok();
    }

    @GetMapping("/{key}")
    public R<SystemParamVO> getByKey(@PathVariable String key) {
        return R.ok(systemParamService.getByKey(key));
    }

    @GetMapping("/value/{key}")
    public R<String> getValue(@PathVariable String key) {
        return R.ok(systemParamService.getValue(key));
    }

    @GetMapping("/page")
    public R<PageResult<SystemParamVO>> page(@Valid SystemParamPageRequest request) {
        return R.ok(systemParamService.page(request));
    }
}
