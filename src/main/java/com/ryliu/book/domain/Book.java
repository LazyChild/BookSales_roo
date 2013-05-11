package com.ryliu.book.domain;

import javax.persistence.Column;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findBooksByTitleLike", "findBooksByIsbnEquals", "findBooksByAuthorLike", "findBooksByPublisherLike" })
public class Book {

    @NotNull
    @Column(unique = true)
    @Size(min = 13, max = 13)
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @NotNull
    private String publisher;

    @DecimalMin("0")
    private Double price;

    @Min(0)
    private Long amount = 0L;

    private String url;
}
