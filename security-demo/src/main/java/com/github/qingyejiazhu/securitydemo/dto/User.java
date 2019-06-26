package com.github.qingyejiazhu.securitydemo.dto;

import com.github.qingyejiazhu.securitydemo.validator.MyConstraint;
import com.fasterxml.jackson.annotation.JsonView;
import javax.validation.constraints.Past;
import javax.validation.constraints.NotBlank;
import java.util.Date;
/**
 * @author gaoxiaofeng
 * @description
 * @date 2019/6/21 17:03
 */
public class User {

    // 简单视图
    public interface UserSimpleView {
    }

    // 详细视图
    public interface UserDetailView extends UserSimpleView {
    }

    private String id;
    @MyConstraint(message = "自定义注解演示使用")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;

    @Past(message = "生日必须是过去时间")   // 必须是过去时间
    @JsonView(UserSimpleView.class)
    private Date birthday;

    @JsonView(UserSimpleView.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(UserDetailView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
