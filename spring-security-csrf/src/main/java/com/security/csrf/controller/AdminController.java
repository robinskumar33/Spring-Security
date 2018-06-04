package com.security.csrf.controller;

import com.security.csrf.model.User;
import com.security.csrf.model.constant.Constant;
import com.security.csrf.service.AdminService;
import com.security.csrf.utils.ResponseJsonUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin/")
@Slf4j
@CrossOrigin
@SuppressWarnings({"unchecked", "rawtypes"})
public class AdminController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "addAdminUser",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> addAdminUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(this.adminService.addAdminUser(user), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "update/user/role",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> updateUser(@RequestBody User updateUser) {
        try {
            return new ResponseEntity(this.adminService.updateUser(updateUser), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "delete/user/{userId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> deleteUserById(@PathVariable("userId") long userId) {
        try {
            return new ResponseEntity(adminService.deleteUser(userId), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "userTypes/list",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> userTypesList() {
        try {
            return new ResponseEntity(this.adminService.userTypesList(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}