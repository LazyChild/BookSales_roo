package com.ryliu.book.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findBookTransactionsByTransactionDateBetween" })
public class BookTransaction {

    @Enumerated
    private TransactionType transactionType;

    @ManyToOne
    private Book book;

    @DecimalMin("0")
    private Double figure;

    @Min(0)
    private Long amount;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date transactionDate;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new BookTransaction().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countBookTransactions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BookTransaction o", Long.class).getSingleResult();
    }

	public static List<BookTransaction> findAllBookTransactions() {
        return entityManager().createQuery("SELECT o FROM BookTransaction o", BookTransaction.class).getResultList();
    }

	public static BookTransaction findBookTransaction(Long id) {
        if (id == null) return null;
        return entityManager().find(BookTransaction.class, id);
    }

	public static List<BookTransaction> findBookTransactionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BookTransaction o", BookTransaction.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            BookTransaction attached = BookTransaction.findBookTransaction(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public BookTransaction merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BookTransaction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public TransactionType getTransactionType() {
        return this.transactionType;
    }

	public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

	public Book getBook() {
        return this.book;
    }

	public void setBook(Book book) {
        this.book = book;
    }

	public Double getFigure() {
        return this.figure;
    }

	public void setFigure(Double figure) {
        this.figure = figure;
    }

	public Long getAmount() {
        return this.amount;
    }

	public void setAmount(Long amount) {
        this.amount = amount;
    }

	public Date getTransactionDate() {
        return this.transactionDate;
    }

	public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

	public static TypedQuery<BookTransaction> findBookTransactionsByTransactionDateBetween(Date minTransactionDate, Date maxTransactionDate) {
        if (minTransactionDate == null) throw new IllegalArgumentException("The minTransactionDate argument is required");
        if (maxTransactionDate == null) throw new IllegalArgumentException("The maxTransactionDate argument is required");
        EntityManager em = BookTransaction.entityManager();
        TypedQuery<BookTransaction> q = em.createQuery("SELECT o FROM BookTransaction AS o WHERE o.transactionDate BETWEEN :minTransactionDate AND :maxTransactionDate", BookTransaction.class);
        q.setParameter("minTransactionDate", minTransactionDate);
        q.setParameter("maxTransactionDate", maxTransactionDate);
        return q;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }
}
