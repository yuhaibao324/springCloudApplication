package com.mistra.userservice.controller;

import com.mistra.base.result.*;
import com.mistra.userservice.base.PageQueryCondition;
import com.mistra.userservice.vo.LoginDTO;
import com.mistra.userservice.vo.RegisterDTO;
import com.mistra.userservice.vo.TokenDTO;
import com.mistra.userservice.vo.UserDTO;
import com.mistra.userservice.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: WangRui
 * @Date: 2018-09-14
 * Time: 上午9:39
 * Description:
 */
@Api("用户模块controlle)
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    @ApiOperation("访问成功测试")
    public Result test(){
        return Results.success();
    }

    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "registerDTO", dataType = "RegisterDTO", value = "用户注册信息", required = true)
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @GetMapping("/login")
    @ApiOperation("登录")
    public GenericResult<TokenDTO> login(@Valid LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @GetMapping("/list")
    @ApiOperation("获取用户分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", dataType = "int", value = "当前页码", required = true),
            @ApiImplicitParam(name = "pageSize", dataType = "int", value = "分页大小", required = true)})
    public GenericResult<PaginationResult<UserDTO>> getUserList(@RequestParam(defaultValue = "1") @Min(0) int pageNumber,
                                                    @RequestParam(defaultValue = "10") @Min(0) int pageSize) {
        return null;
    }

    /**
     * * 使用mybatis-plus自带的分页插件查询，返回结果转换为自定义带DTO的PaginationResult
     * @param userDTO
     * @param pageQueryCondition
     * @return
     */
    @GetMapping("/selectList")
    @ApiOperation("带查询条件的分页列表---mybatis-plus分页插件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDTO", dataType = "UserDTO", value = "筛选条件", required = true),
            @ApiImplicitParam(name = "pageQueryCondition", dataType = "PageQueryCondition", value = "分页参数", required = true)
    })
    public GenericResult<PaginationResult<UserDTO>> getSelectList(@Valid @RequestBody UserDTO userDTO, @Valid @RequestBody PageQueryCondition pageQueryCondition) {
        return userService.getSelectList(userDTO, pageQueryCondition);
    }

    /**
     * * 使用github page-helper分页插件查询，返回结果转换为自定义带DTO的PaginationResult
     * @param userDTO
     * @param pageQueryCondition
     * @return
     */
    @GetMapping("/selectList2")
    @ApiOperation("带查询条件的分页列表---github分页插件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDTO", dataType = "UserDTO", value = "筛选条件", required = true),
            @ApiImplicitParam(name = "pageQueryCondition", dataType = "PageQueryCondition", value = "分页参数", required = true)
    })
    public GenericResult<PaginationResult<UserDTO>> getSelectList2(@Valid UserDTO userDTO, @Valid PageQueryCondition pageQueryCondition){
        return userService.getSelectList2(userDTO,pageQueryCondition);
    }

    @GetMapping("/resultTest")
    public MistraResult resultTest(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("wrmistra@gmail.com");
        userDTO.setUserName("Mistra");
        return Results.mistraSingleEntityResult(userDTO);
    }

    @GetMapping("/resultPageTest")
    public MistraResult resultPageTest(){

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("wrmistra@gmail.com");
        userDTO.setUserName("Mistra");
        List<UserDTO> list = new ArrayList<>();
        list.add(userDTO);
        return Results.mistraPageResult(list,2,3,4,4);
    }

}
