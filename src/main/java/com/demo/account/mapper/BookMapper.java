package com.demo.account.mapper;

import com.demo.account.entity.BookKeeping;
import com.demo.account.entity.CustomFund;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BookMapper {

    @Select("SELECT * FROM bookkeeping WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName}")
    List<BookKeeping> selectByUidAndName(int uid,String bookKeepingName);

    //通过账本类型编号获取该账本应该有的款项编号字符串
    @Select("SELECT bookkeeping_type_funds_id FROM bookkeeping_tpye WHERE bookkeeping_type_id=#{bookkeepingTypeId}")
    String selectBookkeepingTypeList(int bookKeepingTypeId);

    @Select("SELECT bookkeeping_type_name FROM bookkeeping_tpye WHERE bookkeeping_type_id=#{bookkeepingTypeId}")
    String selectBookkeepingTypeName(int bookKeepingTypeId);

    @Select("SELECT fund_name FROM basic_funds WHERE fund_id=#{fundId};")
    String selectBasicFundName(String fundId);

    @Select("SELECT * FROM customed_funds WHERE uid=#{uid} AND bookkeeping_type_id=#{bookkeepingTypeId}")
    List<CustomFund> selectCustomFund(int uid,int bookkeepingTypeId);

    //在customed_funds表中插入一条记录
    @Insert("INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)\n" +
            "VALUES(CONCAT(#{type},replace(uuid(), _utf8'-', _utf8'')),#{uid},#{customedFundName},#{bookKeepingTypeId});")
    int insertCFund(String type,int uid,String customedFundName,int bookKeepingTypeId);
}
