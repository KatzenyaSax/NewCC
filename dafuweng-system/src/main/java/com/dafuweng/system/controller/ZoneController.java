package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody ZoneCreateRequest request) {
        return R.ok(zoneService.create(request));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody ZoneUpdateRequest request) {
        request.setId(id);
        zoneService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return R.ok();
    }

    @GetMapping
    public R<List<ZoneVO>> getList() {
        return R.ok(zoneService.getList());
    }

    @GetMapping("/page")
    public R<PageResult<ZoneVO>> page(@Valid ZonePageRequest request) {
        return R.ok(zoneService.page(request));
    }
}
