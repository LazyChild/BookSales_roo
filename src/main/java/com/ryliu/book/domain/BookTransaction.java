package com.ryliu.book.domain;

import java.util.Date;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BookTransaction {

    @Enumerated
    private TransactionType transactionType;
    
    @DecimalMin("0")
    private Double figure;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "YYYY-MM-DD HH:mm:ss")
    private Date transactionDate;
}
