package com.demo.account.service.impl;

import com.demo.account.entity.BookKeeping;
import com.demo.account.entity.CustomFund;
import com.demo.account.mapper.BookMapper;
import com.demo.account.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class BookServiceImpl implements BookService {
    @Resource
    private BookMapper bookMapper;

    @Override
    public List<String> getExpenditureType(int uid, String bookKeepingName,String bookKeepingTypeName) {
        List<String> l=getPaymentType(uid,bookKeepingName,"O",bookKeepingTypeName);
        return l;
    }

    @Override
    public List<String> getIncomeType(int uid, String bookKeepingName,String bookKeepingTypeName) {
        List<String> l=getPaymentType(uid,bookKeepingName,"I",bookKeepingTypeName);
        return l;
    }

    private List<String> getPaymentType(int uid, String bookKeepingName,String type,String bookKeepingTypeName){
        List<BookKeeping> bookKeeping=bookMapper.selectByUidAndName(uid,bookKeepingName);
        List<String> l=new ArrayList<>();
        for (BookKeeping i: bookKeeping){
            String temp=bookMapper.selectBookkeepingTypeName(i.getBookkeeping_type_id());
            // 找到对应的模板类型的处理
            if (Objects.equals(temp, bookKeepingTypeName)){
                String ls=bookMapper.selectBookkeepingTypeList(i.getBookkeeping_type_id());
                String[] split=ls.split("-");
                for (String j: split){
                    //通过type 判断是支出还是收入 O为支出 I为收入
                    if (j.contains(type)){
                        l.add(bookMapper.selectBasicFundName(j));
                    }
                }
                // 从用户自定义款项中找是否存在，存在加入，不存在不做处理
                List<CustomFund> lcf=bookMapper.selectCustomFund(uid,i.getBookkeeping_type_id());
                if (lcf!=null && !lcf.isEmpty()){
                    for (CustomFund j:lcf){
                        if (j.getCustomed_fund_id().contains(type)){
                            l.add(j.getCustomed_fund_name());
                        }
                    }
                }
            }
        }
        return l;
    }
}
