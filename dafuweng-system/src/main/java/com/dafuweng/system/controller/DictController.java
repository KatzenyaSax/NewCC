package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.DictService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody DictCreateRequest request) {
        return R.ok(dictService.create(request));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody DictUpdateRequest request) {
        request.setId(id);
        dictService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        dictService.delete(id);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R<DictVO> getById(@PathVariable Long id) {
        return R.ok(dictService.getById(id));
    }

    @GetMapping("/type/{dictType}")
    public R<List<DictVO>> getByType(@PathVariable String dictType) {
        return R.ok(dictService.getByType(dictType));
    }

    @GetMapping("/page")
    public R<PageResult<DictVO>> page(@Valid DictPageRequest request) {
        return R.ok(dictService.page(request));
    }
}
