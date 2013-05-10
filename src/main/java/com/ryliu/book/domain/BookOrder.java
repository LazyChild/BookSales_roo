package com.ryliu.book.domain;

import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BookOrder {

    @ManyToOne
    private Book book;
    
    @DecimalMin("0")
    private Double figure;
    
    @Min(0)
    private Long amount;
    
    @Enumerated
    private OrderStatus status = OrderStatus.UNPAID;
}
