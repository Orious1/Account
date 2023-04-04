package com.demo.account.mapper;

import com.demo.account.entity.BasicFund;
import com.demo.account.entity.BookKeeping;
import com.demo.account.entity.CustomFund;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.sql.Timestamp;
import java.util.List;

public interface BookMapper {

    @Results(id="bookMapper" ,value={
            @Result(id = true,column = "bookkeeping_id",property = "bookkeeping_id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "bookkeeping_type_id",property = "bookkeeping_type_id"),
            @Result(column = "bookkeeping_cover",property = "bookkeeping_cover"),
            @Result(column = "bookkeeping_name",property = "bookkeeping_name"),
            @Result(column = "customed_funds_id",property = "customed_funds_id"),
            @Result(column = "extra_member1",property = "extra_member1"),
            @Result(column = "extra_member2",property = "extra_member2")
    })
    @Select("SELECT * FROM bookkeeping WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName}")
    List<BookKeeping> selectByUidAndName(int uid,String bookKeepingName);

    //通过账本类型编号获取该账本应该有的款项编号字符串
    @Select("SELECT bookkeeping_type_funds_id FROM bookkeeping_tpye WHERE bookkeeping_type_id=#{bookkeepingTypeId}")
    String selectBookkeepingTypeList(int bookKeepingTypeId);

    @Select("SELECT bookkeeping_type_name FROM bookkeeping_tpye WHERE bookkeeping_type_id=#{bookkeepingTypeId}")
    String selectBookkeepingTypeName(int bookKeepingTypeId);

    @Select("SELECT fund_name FROM basic_funds WHERE fund_id=#{fundId};")
    String selectBasicFundName(String fundId);

    @Results(id="customFundMapper" ,value={
            @Result(id = true,column = "customed_fund_id",property = "customed_fund_id"),
            @Result(column = "uid",property = "uid"),
            @Result(column = "customed_fund_name",property = "customed_fund_name"),
            @Result(column = "bookkeeping_type_id",property = "bookkeeping_type_id")
    })
    @Select("SELECT * FROM customed_funds WHERE uid=#{uid} AND bookkeeping_type_id=#{bookkeepingTypeId}")
    List<CustomFund> selectCustomFund(int uid,int bookkeepingTypeId);

    //在customed_funds表中插入一条记录
    @Insert("INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)\n" +
            "VALUES(CONCAT(#{type},replace(uuid(), _utf8'-', _utf8'')),#{uid},#{customedFundName},#{bookKeepingTypeId});")
    int insertCFund(String type,int uid,String customedFundName,int bookKeepingTypeId);

    @Update("UPDATE bookkeeping_tpye SET bookkeeping_type_funds_id=#{bookkeepingTypeFundsId} WHERE bookkeeping_type_id=#{bookKeepingTypeId};")
    int updateBookKeepingTypeList(String bookkeepingTypeFundsId, int bookKeepingTypeId);


    @Results(id="basicFundMapper" ,value={
            @Result(id = true,column = "fund_id",property = "fund_id"),
            @Result(column = "fund_name",property = "fund_name"),
            @Result(column = "icon",property = "icon")
    })
    @Select("SELECT * FROM basic_funds")
    List<BasicFund> selectAllBasicFunds();

    @Select("CALL find_bookkeeping_type_id(#{uid},#{bookKeepingName},#{bookkeepingTypeName})")
    @Options(statementType = StatementType.CALLABLE)
    Integer findBookKeepingTypeIdInConditions(int uid, String bookKeepingName, String bookkeepingTypeName);

    @Insert("INSERT INTO payment(uid,bookkeeping_id,account_detail_id,amount,time,fund_id,customed_fund_id,comment,enclosure)\n" +
            "VALUES(#{uid},#{bookKeepingId},#{accountId},#{amount},#{time},#{fundId},#{customedFundId},#{comment},#{enclosure});")
    int insertIntoPayment(int uid, int bookKeepingId, int accountId, String amount, Timestamp time,
                          String fundId,String customedFundId,String comment,String enclosure);

    @Insert("INSERT INTO income(uid,bookkeeping_id,account_detail_id,amount,time,fund_id,customed_fund_id,comment,enclosure)\n" +
            "VALUES(#{uid},#{bookKeepingId},#{accountId},#{amount},#{time},#{fundId},#{customedFundId},#{comment},#{enclosure});")
    int insertIntoIncome(int uid, int bookKeepingId, int accountId, String amount, Timestamp time,
                          String fundId,String customedFundId,String comment,String enclosure);

    @Select("SELECT bookkeeping_id FROM bookkeeping WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName} AND bookkeeping_type_id=#{bookKeepingTypeId} ")
    int findBookKeepingId(int uid,String bookKeepingName,int bookKeepingTypeId);

    @Insert("INSERT INTO bookkeeping(uid,bookkeeping_type_id,bookkeeping_cover,bookkeeping_name,extra_member1,extra_member2)" +
            "values(#{uid},#{bookKeepingTypeId},#{bookKeepingCover},#{bookKeepingName},#{extraMember1},#{extraMember2})")
    int insertIntoBookKeeping(int uid,String bookKeepingName,String bookKeepingCover,Integer extraMember1,Integer extraMember2,int bookKeepingTypeId);

    @Select("SELECT MAX(bookkeeping_type_id) FROM bookkeeping_tpye")
    int generalTypeId();

    @Update("UPDATE bookkeeping SET bookkeeping_name=#{bookKeepingNameNew},bookkeeping_cover=#{bookKeepingCover}," +
            "extra_member1=#{extraMember1},extra_member2=#{extraMember2} WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName}")
    int changeBookKeeping(int uid, String bookKeepingName, String bookKeepingNameNew,String bookKeepingCover, Integer extraMember1, Integer extraMember2);

    @Insert("INSERT INTO bookkeeping_tpye(bookkeeping_type_id,bookkeeping_type_name,bookkeeping_type_funds_id)\n" +
            "VALUES(#{bookkeepingTypeId},#{bookKeepingTypeName},#{template})")
    int insertIntoBookkeepingType(int bookkeepingTypeId,String bookKeepingTypeName,String template);

    @Select("select bookkeeping_name from bookkeeping where uid=#{uid}")
    List<String> selectUserBookkeeping(int uid);

    @Select("SELECT customed_fund_name FROM customed_funds WHERE customed_fund_id=#{customedFundId}")
    String selectCustomedFundName(String customedFundId );

    @Select("SELECT bookkeeping_id FROM bookkeeping WHERE uid=#{uid} AND bookkeeping_name=#{bookKeepingName}")
    int selectBookkeepingId(int uid,String bookKeepingName);

}
