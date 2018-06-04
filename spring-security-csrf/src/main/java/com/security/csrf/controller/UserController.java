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

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/api/user/")
public class UserController {

    @Autowired
    AdminService adminService;

    @RequestMapping(value = "/searchUser/{startsWith}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> getAutocompleteUser(@PathVariable String startsWith) {
        try {
            return new ResponseEntity<>(ResponseJsonUtil.getSuccessResponseJson(
                    this.adminService.searchUsersByName(startsWith)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/searchByEmail",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> searchUsersByEmail(@RequestParam("email") String email) {
        try {
            return new ResponseEntity<>(ResponseJsonUtil.getSuccessResponseJson(
                    this.adminService.searchUsersByEmail(email)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "getUser/{userId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> getUserById(@PathVariable("userId") long userId) {
        try {
            User user = this.adminService.getUsersById(userId);
            user.setPassword(null);
            return new ResponseEntity(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "validateEmail",
            method = RequestMethod.POST,
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> validateEmail(@RequestParam("email") String email) {
        try {
            Map<String, Boolean> data = new HashMap<>();
            data.put("canUseEmail", this.adminService.validateEmail(email));
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "users/list",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-XSRF-TOKEN", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")
    })
    public ResponseEntity<Object> usersList() {
        try {
            return new ResponseEntity(this.adminService.listAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ResponseJsonUtil.getFailedResponseJson(
                    Constant.INTERNAL_ERROR, e.getMessage()), HttpStatus.OK);
        }
    }
}
