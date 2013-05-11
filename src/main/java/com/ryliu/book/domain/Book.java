package com.ryliu.book.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSON;

@RooJavaBean
@RooJson
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
    
    public static TypedQuery<Book> findBooksByTitleOrAuthorLike(String keyword) {
        if (keyword == null || keyword.length() == 0) throw new IllegalArgumentException("The keyword argument is required");
        keyword = keyword.replace('*', '%');
        if (keyword.charAt(0) != '%') {
            keyword = "%" + keyword;
        }
        if (keyword.charAt(keyword.length() - 1) != '%') {
            keyword = keyword + "%";
        }
        EntityManager em = Book.entityManager();
        TypedQuery<Book> q = em.createQuery("SELECT o FROM Book AS o WHERE LOWER(o.title) LIKE LOWER(:keyword) OR LOWER(o.author) LIKE LOWER(:keyword)", Book.class);
        q.setParameter("keyword", keyword);
        return q;
    }

    @JSON(include = false)
	public String getIsbn() {
        return this.isbn;
    }
	
	@JSON(include = false)
	public String getPublisher() {
        return this.publisher;
    }

	@JSON(include = false)
	public Double getPrice() {
        return this.price;
    }

	@JSON(include = false)
	public Long getAmount() {
        return this.amount;
    }

	@JSON(include = false)
	public String getUrl() {
        return this.url;
    }
}
