package com.ryliu.book.web;

import com.ryliu.book.domain.Book;
import com.ryliu.book.domain.BookOrder;
import com.ryliu.book.domain.BookTransaction;
import com.ryliu.book.domain.Person;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Book, String> getBookToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.ryliu.book.domain.Book, java.lang.String>() {
            public String convert(Book book) {
                return new StringBuilder().append(book.getIsbn()).append(' ').append(book.getPublisher()).append(' ').append(book.getPrice()).append(' ').append(book.getAmount()).toString();
            }
        };
    }

	public Converter<Long, Book> getIdToBookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.ryliu.book.domain.Book>() {
            public com.ryliu.book.domain.Book convert(java.lang.Long id) {
                return Book.findBook(id);
            }
        };
    }

	public Converter<String, Book> getStringToBookConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.ryliu.book.domain.Book>() {
            public com.ryliu.book.domain.Book convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Book.class);
            }
        };
    }

	public Converter<BookOrder, String> getBookOrderToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.ryliu.book.domain.BookOrder, java.lang.String>() {
            public String convert(BookOrder bookOrder) {
                return new StringBuilder().append(bookOrder.getFigure()).append(' ').append(bookOrder.getAmount()).toString();
            }
        };
    }

	public Converter<Long, BookOrder> getIdToBookOrderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.ryliu.book.domain.BookOrder>() {
            public com.ryliu.book.domain.BookOrder convert(java.lang.Long id) {
                return BookOrder.findBookOrder(id);
            }
        };
    }

	public Converter<String, BookOrder> getStringToBookOrderConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.ryliu.book.domain.BookOrder>() {
            public com.ryliu.book.domain.BookOrder convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BookOrder.class);
            }
        };
    }

	public Converter<BookTransaction, String> getBookTransactionToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.ryliu.book.domain.BookTransaction, java.lang.String>() {
            public String convert(BookTransaction bookTransaction) {
                return new StringBuilder().append(bookTransaction.getFigure()).append(' ').append(bookTransaction.getAmount()).append(' ').append(bookTransaction.getTransactionDate()).toString();
            }
        };
    }

	public Converter<Long, BookTransaction> getIdToBookTransactionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.ryliu.book.domain.BookTransaction>() {
            public com.ryliu.book.domain.BookTransaction convert(java.lang.Long id) {
                return BookTransaction.findBookTransaction(id);
            }
        };
    }

	public Converter<String, BookTransaction> getStringToBookTransactionConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.ryliu.book.domain.BookTransaction>() {
            public com.ryliu.book.domain.BookTransaction convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), BookTransaction.class);
            }
        };
    }

	public Converter<Person, String> getPersonToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.ryliu.book.domain.Person, java.lang.String>() {
            public String convert(Person person) {
                return new StringBuilder().append(person.getLogin()).append(' ').append(person.getPassword()).append(' ').append(person.getPid()).append(' ').append(person.getName()).toString();
            }
        };
    }

	public Converter<Long, Person> getIdToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.ryliu.book.domain.Person>() {
            public com.ryliu.book.domain.Person convert(java.lang.Long id) {
                return Person.findPerson(id);
            }
        };
    }

	public Converter<String, Person> getStringToPersonConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.ryliu.book.domain.Person>() {
            public com.ryliu.book.domain.Person convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Person.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getBookToStringConverter());
        registry.addConverter(getIdToBookConverter());
        registry.addConverter(getStringToBookConverter());
        registry.addConverter(getBookOrderToStringConverter());
        registry.addConverter(getIdToBookOrderConverter());
        registry.addConverter(getStringToBookOrderConverter());
        registry.addConverter(getBookTransactionToStringConverter());
        registry.addConverter(getIdToBookTransactionConverter());
        registry.addConverter(getStringToBookTransactionConverter());
        registry.addConverter(getPersonToStringConverter());
        registry.addConverter(getIdToPersonConverter());
        registry.addConverter(getStringToPersonConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
