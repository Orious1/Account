package com.demo.account;

import com.demo.account.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccountApplicationTests {

    @Autowired
    BookService bookService;
    @Test
    void contextLoads() {
        //bookService.countWeekIncome(1,"我的账簿1","日常开销","2023-04-03 03:26:54");
        //bookService.countMonthIncome(1,"我的账簿1","日常开销","2023-04-01 03:26:54","2023-04-30 03:26:54");
        //bookService.countYearIncome(1,"我的账簿1","日常开销","2023-01-01 03:26:54","2023-12-31 03:26:54");
        //bookService.countWeekPayment(1,"我的账簿1","日常开销","2023-04-03 03:26:54");
        //bookService.countMonthPayment(1,"我的账簿1","日常开销","2023-04-01 03:26:54","2023-04-30 03:26:54");
        bookService.countYearPayment(1,"我的账簿1","日常开销","2023-01-01 03:26:54","2023-12-31 03:26:54");
    }

}
