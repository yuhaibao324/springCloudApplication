package com.mistra.excelservice.controller;

import com.mistra.base.result.PaginationResult;
import com.mistra.base.result.Result;
import com.mistra.base.result.ResultCode;
import com.mistra.base.result.Results;
import com.mistra.excelservice.entity.ExcelEntity;
import com.mistra.excelservice.service.ExcelService;
import com.mistra.excelservice.util.ExcelImport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @Author: WangRui
 * @Date: 2018/9/20
 * Time: 下午10:43
 * Description:
 */
@RestController
@Api("Excel导入Controller")
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @ApiOperation("Excel导入")
    @ApiImplicitParam(name = "file",dataType = "MultipartFile",value = "Excel文件",required = true)
    @PostMapping(value = "/import")
    public Result<PaginationResult<ExcelEntity>> ledgerImport(MultipartFile file) throws ParseException {
        try {
            List<ExcelEntity> list = ExcelImport.excelTransformationEntityList(ExcelEntity.class, file.getInputStream(), file.getOriginalFilename(), 1, 1);
            excelService.saveBatch(list);
            PaginationResult<ExcelEntity> paginationResult = new PaginationResult<>();
            Result result = new Result(true, ResultCode.SUCCESS.value(),"",paginationResult);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<PaginationResult<ExcelEntity>>(true, ResultCode.SUCCESS.value(),"",null);
        }
    }

}
