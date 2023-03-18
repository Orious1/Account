package com.demo.account.mapper;

import com.demo.account.entity.BasicFund;
import com.demo.account.entity.BookKeeping;
import com.demo.account.entity.CustomFund;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.StatementType;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

    @Update("UPDATE bookkeeping_tpye SET bookkeeping_type_funds_id=#{bookkeepingTypeFundsId} WHERE bookkeeping_type_id=#{bookKeepingTypeId};")
    int updateBookKeepingTypeList(String bookkeepingTypeFundsId, int bookKeepingTypeId);

    @Select("SELECT * FROM basic_funds")
    List<BasicFund> selectAllBasicFunds();

    @Select("CALL find_bookkeeping_type_id(#{uid},#{bookKeepingName},#{bookkeepingTypeName})")
    @Options(statementType = StatementType.CALLABLE)
    Integer findBookKeepingTypeIdInConditions(int uid, String bookKeepingName, String bookkeepingTypeName);

    @Insert("INSERT INTO payment(uid,bookkeeping_id,account_id,amount,time,fund_id,customed_fund_id,comment,enclosure)\n" +
            "VALUES(#{uid},#{bookKeepingId},#{accountId},#{amount},#{time},#{fundId},#{customedFundId},#{comment},#{enclosure});")
    int insertIntoPayment(int uid, int bookKeepingId, int accountId, String amount, Timestamp time,
                          String fundId,String customedFundId,String comment,String enclosure);

    @Insert("INSERT INTO income(uid,bookkeeping_id,account_id,amount,time,fund_id,customed_fund_id,comment,enclosure)\n" +
            "VALUES(#{uid},#{bookKeepingId},#{accountId},#{amount},#{time},#{fundId},#{customedFundId},#{comment},#{enclosure});")
    int insertIntoIncome(int uid, int bookKeepingId, int accountId, String amount, Timestamp time,
                          String fundId,String customedFundId,String comment,String enclosure);

    @Select("SELECT bookkeeping_id FROM bookkeeping WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName} AND bookkeeping_type_id=#{bookKeepingTypeId} ")
    int findBookKeepingId(int uid,String bookKeepingName,int bookKeepingTypeId);
}
