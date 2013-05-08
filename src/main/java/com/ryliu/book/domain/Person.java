package com.ryliu.book.domain;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.util.DigestUtils;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findPeopleByLogin" })
public class Person {

    @NotNull
    @Column(unique = true)
    private String login;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @Enumerated
    private PersonRole personRole;

    @PrePersist
    @PreUpdate
    protected void encryptPassword() {
        if (password != null && !password.matches("^[0-9a-fA-F]+$")) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
        }
    }
}
