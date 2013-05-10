package com.ryliu.book.web;

import com.ryliu.book.domain.BookOrder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bookorders")
@Controller
@RooWebScaffold(path = "bookorders", formBackingObject = BookOrder.class,
delete = false, update = false)
public class BookOrderController {
}
