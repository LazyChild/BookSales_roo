package com.ryliu.book.web;

import com.ryliu.book.domain.Book;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class, delete = false)
@RooWebFinder
public class BookController {
	
    @RequestMapping(value = "/{id}", params = "buy", produces = "text/html")
    public String buy(@PathVariable("id") Long id, Model uiModel) {
        return "books/show";
    }
}
