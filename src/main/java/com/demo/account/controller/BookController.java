package com.demo.account.controller;

import com.demo.account.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private BookService bookService;
    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * @param uid
     * @param bookKeepingName
     * @return  List<String> 表示支出款项类型的列表
     * 功能：在”记一笔”支出页面中可以根据账本的类型渲染出对应的款项类型1
     */
    @RequestMapping(method = RequestMethod.GET,value = "/out")
    List<String> getExpenditureType(int uid,String bookKeepingName,String bookKeepingTypeName){
        return bookService.getExpenditureType(uid,bookKeepingName,bookKeepingTypeName);
    }

    /**
     * @param uid
     * @param bookKeepingName
     * @return List<String> 表示收入款项类型的列表
     *功能：在”记一笔”收入页面中可以根据账本的类型渲染出对应的款项类型
     */
    @RequestMapping(method = RequestMethod.GET,value = "/in")
    List<String> getIncomeType(int uid,String bookKeepingName,String bookKeepingTypeName){
        return bookService.getIncomeType(uid,bookKeepingName,bookKeepingTypeName);
    }

}
