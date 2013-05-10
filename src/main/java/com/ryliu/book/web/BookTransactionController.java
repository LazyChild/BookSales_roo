package com.ryliu.book.web;

import com.ryliu.book.domain.BookTransaction;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/booktransactions")
@Controller
@RooWebScaffold(path = "booktransactions", formBackingObject = BookTransaction.class,
create=false, delete=false, update=false)
public class BookTransactionController {
}
