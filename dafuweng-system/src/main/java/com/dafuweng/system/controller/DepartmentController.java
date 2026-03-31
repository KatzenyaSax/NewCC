package com.dafuweng.system.controller;

import com.dafuweng.common.core.result.PageResult;
import com.dafuweng.common.core.result.R;
import com.dafuweng.system.domain.dto.*;
import com.dafuweng.system.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public R<Long> create(@Valid @RequestBody DepartmentCreateRequest request) {
        return R.ok(departmentService.create(request));
    }

    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody DepartmentUpdateRequest request) {
        request.setId(id);
        departmentService.update(request);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return R.ok();
    }

    @GetMapping("/{id}")
    public R<DepartmentVO> getById(@PathVariable Long id) {
        return R.ok(departmentService.getById(id));
    }

    @GetMapping("/tree")
    public R<List<DepartmentTreeVO>> getTree() {
        return R.ok(departmentService.getTree());
    }

    @GetMapping("/list")
    public R<List<DepartmentVO>> getList() {
        return R.ok(departmentService.getList());
    }

    @GetMapping("/page")
    public R<PageResult<DepartmentVO>> page(@Valid DepartmentPageRequest request) {
        return R.ok(departmentService.page(request));
    }
}
