insert into user VALUES(1,"x","x", 20021114093723);

INSERT INTO bookkeeping(bookkeeping_id,uid,bookkeeping_type_id,bookkeeping_cover,bookkeeping_name,bookkeeping_period,bookkeeping_create_date,bookkeeping_end_date,customed_funds_id,extra_member1,extra_member2) VALUES(1,1,1,"红包","我的账簿1","一个月",20021114093723,20031114093723,"CO1",2,3);
INSERT INTO bookkeeping(bookkeeping_id,uid,bookkeeping_type_id,bookkeeping_cover,bookkeeping_name,bookkeeping_period,bookkeeping_create_date,bookkeeping_end_date,customed_funds_id,extra_member1,extra_member2) VALUES(2,1,3,"红包","我的账簿2","一个月",20021114093723,20031114093723,"CO1",2,3);
INSERT INTO bookkeeping(bookkeeping_id,uid,bookkeeping_type_id,bookkeeping_cover,bookkeeping_name,bookkeeping_period,bookkeeping_create_date,bookkeeping_end_date,customed_funds_id,extra_member1,extra_member2) VALUES(3,1,2,"红包","我的账簿1","一个月",20021114093723,20031114093723,"CO1",2,3);

INSERT INTO payment(payment_id,uid,bookkeeping_id,account_id,amount,time,fund_id,customed_fund_id,comment,enclosure)
VALUES(1,1,1,1,"100元",20021114093723,"BO1",NULL,"无","无");

INSERT INTO income(income_id,uid,bookkeeping_id,account_id,amount,time,fund_id,customed_fund_id,comment,enclosure)
VALUES(1,1,1,1,"100元",20021114093723,"BI17",NULL,"无","无");

#账本类型表
INSERT INTO bookkeeping_tpye(bookkeeping_type_id,bookkeeping_type_name,bookkeeping_type_funds_id)
VALUES(1,"日常开销","BO1-BO2-BO3-BO4-BO5-BO6-BO7-BO8-BO9-BO10-BO11-BO12-BO13-BI17-BI18-BI19");
INSERT INTO bookkeeping_tpye(bookkeeping_type_id,bookkeeping_type_name,bookkeeping_type_funds_id)
VALUES(2,"人情往来","BO8-BO9-BO10-BO11-BO12-BO13-BO14-BO15-BI20-BI21");
INSERT INTO bookkeeping_tpye(bookkeeping_type_id,bookkeeping_type_name,bookkeeping_type_funds_id)
VALUES(3,"家庭账本","BO3-BO4-BO5-BO6-BO7-BI17-BI23-BI26-BI27");

#款项类型表
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO1","餐饮");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO2","购物");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO3","日用");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO4","交通");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO5","蔬菜");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO6","水果");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO7","零食");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO8","运动");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO9","娱乐");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO10","通讯");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO11","服饰");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO12","快递");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO13","家庭");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO14","社交");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO15","旅行");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BO16","住房");
#---------以上是支出款项类型-------
#---------以下是收入款项类型-------
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI17","工资");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI18","红包");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI19","礼金");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI20","租金");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI21","分红");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI22","理财");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI23","年终奖");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI24","借入");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI25","收款");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI26","生活费");
INSERT INTO basic_funds(fund_id,fund_name)
VALUES("BI27","其它");

#用户自定义款项表
INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)
VALUES("CO1",1,"彩票",1);
INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)
VALUES("CI1",1,"遗产",2);

INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)
VALUES(CONCAT("CI",replace(uuid(), _utf8'-', _utf8'')),1,"遗产",2);
INSERT INTO customed_funds(customed_fund_id,uid,customed_fund_name,bookkeeping_type_id)
VALUES(CONCAT("CO",replace(uuid(), _utf8'-', _utf8'')),1,"测试",1);

SELECT bookkeeping_type_funds_id FROM bookkeeping_tpye WHERE bookkeeping_type_id=1;
SELECT fund_name FROM basic_funds WHERE fund_id="BO1";

SELECT * FROM customed_funds WHERE uid=1 AND bookkeeping_type_id=2;

UPDATE bookkeeping_tpye SET bookkeeping_type_funds_id="BO3-BO4-BO5-BO6-BO7-BI17-BI23-BI26-BI26" WHERE bookkeeping_type_id=3;
SELECT * FROM basic_funds;

CREATE PROCEDURE find_bookkeeping_type_id(IN uid INT,IN bookkeeping_name VARCHAR(255),IN bookkeeping_type_name VARCHAR(255))
BEGIN
	SELECT bookkeeping_tpye.bookkeeping_type_id
	#INTO bookkeeping_tpye_id
	FROM bookkeeping_tpye WHERE bookkeeping_tpye.bookkeeping_type_name=bookkeeping_type_name AND bookkeeping_tpye.bookkeeping_type_id IN
	(SELECT bookkeeping.bookkeeping_type_id
	FROM bookkeeping WHERE bookkeeping.uid=uid AND bookkeeping.bookkeeping_name=bookkeeping_name);
END;

#CALL find_bookkeeping_type_id(1,'我的账簿1','人情往来',@id);
#SELECT @id;
CALL find_bookkeeping_type_id(1,'我的账簿1','人情往来');

SELECT bookkeeping_id FROM bookkeeping WHERE uid=1 AND bookkeeping_name='我的账簿1' AND bookkeeping_type_id=2;

SELECT MAX(bookkeeping_type_id) FROM bookkeeping_tpye;

UPDATE bookkeeping SET bookkeeping_cover="天空",bookkeeping_period="2个月",bookkeeping_create_date=20021114093723,bookkeeping_end_date=20021114093723,extra_member1=NULL,extra_member2=2 WHERE uid=1 AND bookkeeping_name="我的账簿4";