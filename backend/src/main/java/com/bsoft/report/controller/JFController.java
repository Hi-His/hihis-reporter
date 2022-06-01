package com.bsoft.report.controller;

import com.bsoft.report.dao.DbmsOutput;

import com.bsoft.report.dto.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: 何胜豪
 * @Title: TODO
 * @Package: com.bsoft.rooler.controller
 * @Description:
 * @date : 2021/11/27 23:15
 */
@RestController
@RequestMapping("/report")
public class JFController {




    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping("/ar")
    @Transactional(readOnly = true)
    public String getAr(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show;
        try {
            conn.setAutoCommit (false);
            Statement stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput( conn );

            dbmsOutput.enable( 1000000 );

            stmt.execute
                    (   "  declare amt1 varchar2(32);  " +
                            "  amt3 varchar2(32);  " +
                            "   amt4 NUMBER(32,3) := 0;  " +
                            "  amt5 NUMBER(32,3) := 0;  " +
                            "  name   varchar2(32);  " +
                            "  cds varchar2(32);  " +
                            "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  char_dt_cc_b varchar2(32);  " +
                            "  char_dt_cc_e varchar2(32);  " +
                            " begin   " +
                            "  " +
                            "  " +
                            "  for cur in (  " +
                            "    SELECT  " +
                            "  id_cc,DT_CC_B,dt_cc_e,id_emp_cc  " +
                            " FROM  " +
                            "  HI_BIL_med_cc   " +
                            " WHERE  " +
                            "     id_bilmedcctotal in (  " +
                            "   select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end  " +
                            "   )    " +
                            "   )  " +
                            "   loop   " +
                            "     " +
                            "  select  nvl(sum(amount), 0 ) into amt1 from (  " +
                            "   " +
                            "select t1.ID_HIPL_MAIN, to_char(trunc(nvl(sum(t1.amt_medst_hipl), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1 left join HI_BD_HIPL t2 on t1.ID_HIPL_MAIN = t2.ID_HIPL  " +
                            "where 1=1  " +
                            "and t2.sd_gepltp_cd != '91'  " +
                            "and t1.amt_medst_hipl > 0  " +
                            " and t1.dt_medst between cur.dt_cc_b and cur.dt_cc_e  " +
                            " and t1.sd_medst_cd in (111, 112) and t1.id_cc =  cur.id_cc  " +
                            "   " +
                            " group by t1.ID_HIPL_MAIN  " +
                            "   " +
                            "union all  " +
                            "   " +
                            "select t1.ID_HIPL_MAIN, to_char(trunc(nvl(sum(t1.amt_medst_hipl*-1), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1  left join HI_BD_HIPL t2 on t1.ID_HIPL_MAIN = t2.ID_HIPL  " +
                            "where 1=1  " +
                            "and t2.sd_gepltp_cd != '91'  " +
                            "and t1.amt_medst_hipl > 0  " +
                            " and t1.sd_medst_cd in ('122', '123') and t1.id_cc =  cur.id_cc  " +
                            "   " +
                            " group by t1.ID_HIPL_MAIN  " +
                            "  " +
                            "   " +
                            ");  " +
                            "  " +
                            "begin  " +
                            "  " +
                            "select    " +
                            "     nvl(sum(t2.amt_ar), 0 )   into amt3   " +
                            "        " +
                            "   from HI_BIL_MED_CC t1  " +
                            "  join PRD_BBP.hi_sys_user ur on ur.id_user = t1.ID_EMP_CC  " +
                            "   join hi_bil_med_cc_total total  " +
                            "     on t1.id_bilmedcctotal = total.id_bilmedcctotal  " +
                            "   join HI_BIL_CC_AR t2  " +
                            "     on t1.id_cc = t2.id_cc  " +
                            "  where (t1.sd_vistp_cd = '111' or t1.sd_vistp_cd = '112' )  " +
                            "   and t1.id_cc = cur.id_cc  " +
                            "  and t1.id_org = '610d4db624f65b5600f660f5'  " +
                            "  and t1.id_dep_cc in  ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4')  " +
                            " and total.DT_CC_TOTAL >  time_begin   " +
                            " and total.DT_CC_TOTAL <= time_end   " +
                            " GROUP BY '储值金';  " +
                            "EXCEPTION  " +
                            "  WHEN NO_DATA_FOUND THEN  " +
                            "   amt3 := '0';  " +
                            "  " +
                            "end;  " +
                            "   " +
                            "  " +
                            "  " +
                            "  select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc;  " +
                            "  select TO_CHAR(cur.DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(cur.DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc;  " +
                            "   " +
                            " amt4 :=  NVL(amt1, 0) + NVL(amt4, 0);  " +
                            "   amt5 :=  NVL(amt3, 0) + NVL(amt5, 0);  " +
                            "   " +
                            "  if(amt1 <> amt3) THEN  " +
                            "   dbms_output.put_line ('结账id：'|| cur.id_cc);  " +
                            "  dbms_output.put_line ('结账人员：' || name);  " +
                            "  dbms_output.put_line ('结账人员cd：' || cds);  " +
                            "  dbms_output.put_line ('结账开始时间：' || char_dt_cc_b);  " +
                            "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e);  " +
                            "    dbms_output.put_line (name||' 个人借方医保:'||amt1);  " +
                            "  dbms_output.put_line ('汇总借方医保: '||amt3);  " +
                            "  dbms_output.put_line ('   ');  " +
                            " end if;  " +
                            "     " +
                            "   end loop;  " +
                            "    " +
                            "  dbms_output.put_line ('个人借方医保合计：'||amt4);  " +
                            "  dbms_output.put_line ('汇总借方医保合计：'||amt5);  " +
                            "  amt4 :=  NVL(amt4, 0) - NVL(amt5, 0);  " +
                            "    " +
                            "  dbms_output.put_line ('个人 - 汇总：'|| amt4);   " +
                            "    " +
                            "   dbms_output.put_line ('请修改下表：HI_BIL_CC_AR ！！！');  " +
                            " END;  " +
                            "  " +
                            "   " +
                            " " );
            stmt.close();

              show = dbmsOutput.show();

            dbmsOutput.close();
        }catch(Exception e){
            throw e;
        }finally {
            conn.close();
        }

       return  show;
    }


    @PostMapping("/jfxj")
    @Transactional(readOnly = true)
    public String getjfxj(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";
      try {
          conn.setAutoCommit (false);
          Statement stmt = conn.createStatement();

          DbmsOutput dbmsOutput = new DbmsOutput( conn );

          dbmsOutput.enable( 1000000 );

          stmt.execute
                  (   " declare amt1 varchar2(32);  " +
                          "   amt3 varchar2(32);  " +
                          "  amt4 NUMBER(32,3) := 0;  " +
                          "   amt5 NUMBER(32,3) := 0;  " +
                          "  name   varchar2(32);  " +
                          "  cds varchar2(32);  " +
                          "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                          "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                          "  char_dt_cc_b varchar2(32);  " +
                          "  char_dt_cc_e varchar2(32);  " +
                          " begin   " +
                          "  " +
                          "  " +
                          "  for cur in (  " +
                          "    SELECT  " +
                          "  id_cc,DT_CC_B,dt_cc_e,id_emp_cc  " +
                          " FROM  " +
                          "  HI_BIL_med_cc   " +
                          " WHERE  " +
                          "     id_bilmedcctotal in (  " +
                          "   select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end  " +
                          "   )    " +
                          "   )  " +
                          "   loop   " +
                          "     " +
                          "  select sum(amount) into amt1 from (  " +
                          "   " +
                          "   " +
                          "  " +
                          "select nvl(sum(t1.EU_DIRECT * t1.AMT_PIPREPAY),0) as amount from HI_BIL_PI_PREPAY t1  " +
                          "inner join HI_BIL_PI_PREPAY_PM t2 on t1.ID_PIPREPAY = t2.ID_PIPREPAY and t1.sd_piprepaytp_cd in ('1', '2')  " +
                          "where 1=1  " +
                          "  " +
                          "  and t1.id_cc =  cur.id_cc  " +
                          " and t2.SD_PIPYPM_CD = '1'  " +
                          " group by t2.sd_pipypm_cd  " +
                          " " +
                          "union all  " +
                          "select nvl(sum(t1.EU_DIRECT * t2.AMT_PYPM),0) as amount from HI_BIL_MED_PIPY_OE t1  " +
                          "inner join HI_BIL_MED_PIPY_OE_PM t2 on t1.ID_MEDPIPYOE = t2.ID_MEDPIPYOE  " +
                          "where 1=1  " +
                          "and t2.SD_PIPYPM_CD not in ('2', '9')  " +
                          " and t1.id_cc =  cur.id_cc  " +
                          " and t2.SD_PIPYPM_CD = '1'  " +
                          "group by t2.SD_PIPYPM_CD  " +
                          "  " +
                          "   " +
                          ");  " +
                          "  " +
                          "begin  " +
                          "select   sum(t2.amt_pypm-t2.amt_pypm_refund) into amt3     " +
                          "  from HI_BIL_MED_CC t1  " +
                          "  join hi_bil_med_cc_total total  " +
                          "  on t1.id_bilmedcctotal = total.id_bilmedcctotal  " +
                          "  join HI_BIL_CC_PM t2  " +
                          "    on t1.id_cc = t2.id_cc  " +
                          " where (t1.sd_vistp_cd = '111' or t1.sd_vistp_cd = '112' or t1.sd_vistp_cd is null)  " +
                          "   and t1.id_cc = cur.id_cc  " +
                          "  and t1.id_org = '610d4db624f65b5600f660f5'  " +
                          "   and t1.id_dep_cc in  ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4')   " +
                          "    and t2.sd_pipypm_cd  = '1'   " +
                          "  and t2.sd_pipypm_cd <> '2'   " +
                          "  and t2.sd_pipypm_cd <> '9'   " +
                          "  and t2.sd_pipypm_cd <> '21'   " +
                          "  and t2.sd_pipypm_cd <> '23'   " +
                          " and total.dt_cc_total > time_begin   " +
                          " and total.dt_cc_total <= time_end  " +
                          " group by '现金';  " +
                          "  " +
                          "EXCEPTION  " +
                          "  WHEN NO_DATA_FOUND THEN  " +
                          "   amt3 := '0';  " +
                          "   " +
                          "end;  " +
                          "   " +
                          "  select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc;  " +
                          "  select TO_CHAR(cur.DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(cur.DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc;  " +
                          "   " +
                          "  amt4 :=  NVL(amt1, 0) + NVL(amt4, 0);  " +
                          "  amt5 :=  NVL(amt3, 0) + NVL(amt5, 0);  " +
                          "    " +
                          "  if(amt1 <> amt3) THEN  " +
                          "   dbms_output.put_line ('结账id：'|| cur.id_cc);  " +
                          "  dbms_output.put_line ('结账人员：' || name);  " +
                          "  dbms_output.put_line ('结账人员cd：' || cds);  " +
                          "  dbms_output.put_line ('结账开始时间：' || char_dt_cc_b);  " +
                          "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e);  " +
                          "    dbms_output.put_line (name||' 个人借方现金:'||amt1);  " +
                          "  dbms_output.put_line ('汇总借方现金: '||amt3);  " +
                          "  dbms_output.put_line ('   ');  " +
                          " end if;  " +
                          "   " +
                          "   end loop;  " +
                          "   dbms_output.put_line ('个人借方现金合计：'||amt4);  " +
                          "  dbms_output.put_line ('汇总借方现金合计：'||amt5);  " +
                          "  amt4 :=  NVL(amt4, 0) - NVL(amt5, 0);  " +
                          "    " +
                          "  dbms_output.put_line ('个人 - 汇总：'|| amt4);   " +
                          "    " +
                          "    " +
                          "  dbms_output.put_line ('请根据结账id修复表【HI_BIL_CC_PM】现金方式的金额');  " +
                          " END;  " +
                          "  " +
                          "   " +
                          " " );
          stmt.close();

            show = dbmsOutput.show();

          dbmsOutput.close();
      }catch(Exception e){
          throw e;
      }finally {
          conn.close();
      }

        return  show;
    }


    @PostMapping("/jfjhzf")
    @Transactional(readOnly = true)
    public String getjfjhzh(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";
     try {
         conn.setAutoCommit (false);
         Statement stmt = conn.createStatement();

         DbmsOutput dbmsOutput = new DbmsOutput( conn );

         dbmsOutput.enable( 1000000 );

         stmt.execute
                 (   "declare amt1 NUMBER(32,3);  " +
                         "  amt3 NUMBER(32,3);  " +
                         "amt4 NUMBER(32,3) := 0;  " +
                         " amt5 NUMBER(32,3) := 0;  " +
                         " name   varchar2(32);  " +
                         " cds varchar2(32);  " +
                         "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                         "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                         " char_dt_cc_b varchar2(32);  " +
                         " char_dt_cc_e varchar2(32);  " +
                         "begin   " +
                         "  " +
                         "  for cur in (  " +
                         "    SELECT  " +
                         "id_cc,DT_CC_B,dt_cc_e,id_emp_cc  " +
                         "FROM  " +
                         "HI_BIL_med_cc   " +
                         "WHERE  " +
                         "   id_bilmedcctotal in (  " +
                         "select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end  " +
                         " )    " +
                         "   )  " +
                         "   loop   " +
                         "    " +
                         " select nvl(sum(amount), 0 )into amt1 from (  " +
                         "   " +
                         "   " +
                         "  " +
                         "select nvl(sum(t1.EU_DIRECT * t1.AMT_PIPREPAY),0) as amount from HI_BIL_PI_PREPAY t1  " +
                         "inner join HI_BIL_PI_PREPAY_PM t2 on t1.ID_PIPREPAY = t2.ID_PIPREPAY and t1.sd_piprepaytp_cd in ('1', '2')  " +
                         "where 1=1  " +
                         "  and t1.id_cc =  cur.id_cc  " +
                         "and t2.SD_PIPYPM_CD = '10'  " +
                         " group by t2.sd_pipypm_cd  " +
                         "union all  " +
                         "select nvl(sum(t1.EU_DIRECT * t2.AMT_PYPM),0) as amount from HI_BIL_MED_PIPY_OE t1  " +
                         "inner join HI_BIL_MED_PIPY_OE_PM t2 on t1.ID_MEDPIPYOE = t2.ID_MEDPIPYOE  " +
                         "where 1=1  " +
                         "and t2.SD_PIPYPM_CD not in ('2', '9')  " +
                         " and t1.id_cc =  cur.id_cc  " +
                         " and t2.SD_PIPYPM_CD = '10'  " +
                         "group by t2.SD_PIPYPM_CD  " +
                         ");  " +
                         "  " +
                         "begin  " +
                         "select   sum(t2.amt_pypm-t2.amt_pypm_refund) into amt3     " +
                         "  from HI_BIL_MED_CC t1  " +
                         "  join hi_bil_med_cc_total total  " +
                         "  on t1.id_bilmedcctotal = total.id_bilmedcctotal  " +
                         "  join HI_BIL_CC_PM t2  " +
                         "    on t1.id_cc = t2.id_cc  " +
                         " where (t1.sd_vistp_cd = '111' or t1.sd_vistp_cd = '112' or t1.sd_vistp_cd is null)  " +
                         "  and t1.id_cc = cur.id_cc  " +
                         "  and t1.id_org = '610d4db624f65b5600f660f5'  " +
                         "   and t1.id_dep_cc in  ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4')   " +
                         "   and t2.sd_pipypm_cd  = '10'   " +
                         "  and t2.sd_pipypm_cd <> '2'   " +
                         "  and t2.sd_pipypm_cd <> '9'   " +
                         "  and t2.sd_pipypm_cd <> '21'   " +
                         "  and t2.sd_pipypm_cd <> '23'  " +
                         " and total.dt_cc_total > time_begin   " +
                         " and total.dt_cc_total <= time_end  " +
                         " group by '聚合支付';  " +
                         "  " +
                         "EXCEPTION  " +
                         " WHEN NO_DATA_FOUND THEN  " +
                         "amt3 := '0';  " +
                         "  " +
                         "end;  " +
                         "   " +
                         "select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc;  " +
                         "select TO_CHAR(cur.DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(cur.DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc;  " +
                         "  " +
                         "amt4 :=  NVL(amt1, 0) + NVL(amt4, 0);  " +
                         " amt5 :=  NVL(amt3, 0) + NVL(amt5, 0);  " +
                         "   " +
                         " if(amt1 <> amt3) THEN  " +
                         " dbms_output.put_line ('结账id：'|| cur.id_cc);  " +
                         "dbms_output.put_line ('结账人员：' || name);  " +
                         "dbms_output.put_line ('结账人员cd：' || cds);  " +
                         "dbms_output.put_line ('结账开始时间：' || char_dt_cc_b);  " +
                         "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e);  " +
                         "    dbms_output.put_line (name||' 个人借方聚合支付:'||amt1);  " +
                         "dbms_output.put_line ('汇总借方聚合支付: '||amt3);  " +
                         "dbms_output.put_line ('   ');  " +
                         "end if;  " +
                         "    " +
                         " end loop;  " +
                         "   " +
                         "   dbms_output.put_line ('个人借方聚合支付合计：'||amt4);  " +
                         "dbms_output.put_line ('汇总借方聚合支付合计：'||amt5);  " +
                         "amt4 :=  NVL(amt4, 0) - NVL(amt5, 0);  " +
                         "  " +
                         "dbms_output.put_line ('个人 - 汇总：'|| amt4);   " +
                         "  " +
                         " dbms_output.put_line ('请根据结账id修复表【HI_BIL_CC_PM】聚合支付方式的金额');  " +
                         "   " +
                         "  " +
                         " END;  " +
                         " " );
         stmt.close();

           show = dbmsOutput.show();

         dbmsOutput.close();
     }catch(Exception e){
         throw e;
     }finally {
         conn.close();
     }

        return  show;
    }

    @PostMapping("/jfczj")
    @Transactional(readOnly = true)
    public String getjfczj(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show ="";
       try {
           conn.setAutoCommit (false);
           Statement stmt = conn.createStatement();

           DbmsOutput dbmsOutput = new DbmsOutput( conn );

           dbmsOutput.enable( 1000000 );

           stmt.execute
                   (   "declare amt1 varchar2(32); " +
                           " amt3 varchar2(32); " +
                           " amt4 NUMBER(32,3) := 0; " +
                           " amt5 NUMBER(32,3) := 0; " +
                           " name   varchar2(32); " +
                           " cds varchar2(32); " +
                           "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                           "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                           " char_dt_cc_b varchar2(32); " +
                           " char_dt_cc_e varchar2(32); " +
                           "begin  " +
                           " " +
                           "  for cur in ( " +
                           "    SELECT " +
                           "id_cc,DT_CC_B,dt_cc_e,id_emp_cc " +
                           "FROM " +
                           "HI_BIL_med_cc  " +
                           "WHERE " +
                           "   id_bilmedcctotal in ( " +
                           "select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end " +
                           ")   " +
                           "   ) " +
                           "   loop  " +
                           "   " +
                           "select nvl(sum(amount), 0) into amt1 from ( " +
                           "  " +
                           " " +
                           " " +
                           "select sum(t1.EU_DIRECT * t2.AMT_PYPM) as amount from HI_BIL_MED_PIPY_OE t1 " +
                           "inner join HI_BIL_MED_PIPY_OE_PM t2 on t1.ID_MEDPIPYOE = t2.ID_MEDPIPYOE " +
                           "where 1=1 " +
                           "and t2.SD_PIPYPM_CD = '2' " +
                           "  and t1.id_cc =  cur.id_cc " +
                           "group by '储值金' " +
                           " " +
                           "); " +
                           " " +
                           " " +
                           " " +
                           "begin " +
                           " " +
                           "select   nvl(sum(t2.amt_pypm-t2.amt_pypm_refund ),0) into amt3   " +
                           "  from HI_BIL_MED_CC t1 " +
                           " join PRD_BBP.hi_sys_user ur on ur.id_user = t1.ID_EMP_CC " +
                           "  join hi_bil_med_cc_total total " +
                           "  on t1.id_bilmedcctotal = total.id_bilmedcctotal " +
                           "  join HI_BIL_CC_PM t2 " +
                           "    on t1.id_cc = t2.id_cc " +
                           " where (t1.sd_vistp_cd = '111' or t1.sd_vistp_cd = '112') " +
                           " and t1.id_cc = cur.id_cc " +
                           "  and t1.id_org = '610d4db624f65b5600f660f5' " +
                           "  and t1.id_dep_cc in  ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4') " +
                           "  and t2.sd_pipypm_cd = '2'  " +
                           " and total.dt_cc_total > time_begin " +
                           " and total.dt_cc_total <= time_end " +
                           "GROUP BY '储值金'; " +
                           "  " +
                           " EXCEPTION " +
                           " WHEN NO_DATA_FOUND THEN " +
                           "amt3 := '0'; " +
                           " " +
                           "END; " +
                           "select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc; " +
                           "select TO_CHAR(DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc; " +
                           " " +
                           "amt4 :=  NVL(amt1, 0) + NVL(amt4, 0); " +
                           "amt5 :=  NVL(amt3, 0) + NVL(amt5, 0); " +
                           " " +
                           " " +
                           "  if(amt1 <> amt3) THEN " +
                           " dbms_output.put_line ('结账id：'|| cur.id_cc); " +
                           "dbms_output.put_line ('结账人员：' || name); " +
                           "dbms_output.put_line ('结账人员cd：' || cds); " +
                           "dbms_output.put_line ('结账开始时间：' || char_dt_cc_b); " +
                           "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e); " +
                           "    dbms_output.put_line (name||' 个人借方储值金:'||amt1); " +
                           "dbms_output.put_line ('汇总借方储值金: '||amt3); " +
                           "dbms_output.put_line ('   '); " +
                           "end if; " +
                           " " +
                           "  " +
                           "   end loop; " +
                           "  dbms_output.put_line ('个人借方储值金合计金额：'||amt4); " +
                           "dbms_output.put_line ('汇总借方储值金合计金额：'||amt5); " +
                           "amt4 :=  NVL(amt4, 0) - NVL(amt5, 0); " +
                           " " +
                           "dbms_output.put_line ('个人 - 汇总：'|| amt4);  " +
                           " " +
                           " dbms_output.put_line ('请修改下表：HI_BIL_CC_PM ！！！'); " +
                           "  " +
                           " END; " +
                           " " );
           stmt.close();

             show = dbmsOutput.show();

           dbmsOutput.close();
       }catch(Exception e){
           throw e;
       }finally {
           conn.close();
       }

        return  show;
    }


    @PostMapping("/jfjmje")
    @Transactional(readOnly = true)
    public String getjfjmje(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";
        try {

            conn.setAutoCommit (false);
            Statement stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput( conn );

            dbmsOutput.enable( 1000000 );

            stmt.execute
                    (   "declare amt1 NUMBER(32):= 0; " +
                            " amt3 NUMBER(32) := 0 ; " +
                            " name   varchar2(32); " +
                            " cds varchar2(32); " +
                            "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            " char_dt_cc_b varchar2(32); " +
                            " char_dt_cc_e varchar2(32); " +
                            "begin  " +
                            " " +
                            "  for cur in ( " +
                            "    SELECT " +
                            "id_cc,DT_CC_B,dt_cc_e,id_emp_cc " +
                            "FROM " +
                            "HI_BIL_med_cc  " +
                            "WHERE " +
                            "   id_bilmedcctotal in ( " +
                            "select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end " +
                            ")   " +
                            "   ) " +
                            "   loop  " +
                            "   " +
                            "select sum(amount) into amt1 from ( " +
                            "  " +
                            " " +
                            " " +
                            "  " +
                            "select t.sd_pipypm_cd, sum(t.amount) amount from ( " +
                            "select '减免金额' as sd_pipypm_cd, to_char(trunc(nvl(sum(t2.amt_deduc), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1 " +
                            "    left join HI_BIL_MED_CG_OE t2 on t1.ID_MEDSTOE = t2.ID_MEDST_CG " +
                            "where 1=1 " +
                            "and t1.sd_medst_cd in ('111', '112') " +
                            " and t1.dt_medst between cur.dt_cc_b and cur.dt_cc_e and t1.id_cc =  cur.id_cc " +
                            " group by '减免金额' " +
                            "union all " +
                            " select '减免金额' as sd_pipypm_cd, to_char(trunc(nvl(sum(t2.amt_deduc*-1), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1 " +
                            "    left join HI_BIL_MED_CG_OE t2 on t1.ID_MEDSTOE = t2.ID_MEDST_CG " +
                            "where 1=1 " +
                            "and t1.sd_medst_cd in ('122', '123') " +
                            " and t1.dt_medst between cur.dt_cc_b and cur.dt_cc_e and t1.id_cc =  cur.id_cc " +
                            " group by '减免金额' " +
                            " ) t group by t.SD_PIPYPM_CD " +
                            " " +
                            " " +
                            "union all " +
                            " " +
                            "  " +
                            "select t.sd_pipypm_cd, sum(t.amount) amount from ( " +
                            "select '减免金额' as sd_pipypm_cd, to_char(trunc(nvl(sum(t3.amt_pypm), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1 " +
                            "    left join HI_BIL_MED_PIPY_OE t2 on t1.ID_MEDSTOE = t2.ID_MEDSTOE " +
                            "    left join HI_BIL_MED_PIPY_OE_PM t3 on t2.ID_MEDPIPYOE = t3.ID_MEDPIPYOE " +
                            "where 1=1 " +
                            "and t1.sd_medst_cd in ('111', '112') " +
                            " and t1.dt_medst between cur.dt_cc_b and cur.dt_cc_e and t1.id_cc =  cur.id_cc " +
                            " and t3.SD_PIPYPM_CD = '9' " +
                            " group by '减免金额' " +
                            "union all " +
                            " select '减免金额' as sd_pipypm_cd, to_char(trunc(nvl(sum(t3.amt_pypm*-1), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_MED_ST_OE t1 " +
                            "    left join HI_BIL_MED_PIPY_OE t2 on t1.ID_MEDSTOE = t2.ID_MEDSTOE " +
                            "    left join HI_BIL_MED_PIPY_OE_PM t3 on t2.ID_MEDPIPYOE = t3.ID_MEDPIPYOE " +
                            "where 1=1 " +
                            "and t1.sd_medst_cd in ('122', '123') " +
                            " and t1.dt_medst between cur.dt_cc_b and cur.dt_cc_e and t1.id_cc =  cur.id_cc " +
                            " and t3.SD_PIPYPM_CD = '9' " +
                            " group by '减免金额' " +
                            " ) t group by t.SD_PIPYPM_CD " +
                            "  " +
                            "  " +
                            "); " +
                            "  " +
                            "  " +
                            " " +
                            " " +
                            "select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc; " +
                            "select TO_CHAR(DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc; " +
                            "   " +
                            "amt3 :=  NVL(amt1, 0) + NVL(amt3, 0); " +
                            "  " +
                            " " +
                            "  " +
                            "  " +
                            "  " +
                            "   end loop; " +
                            " dbms_output.put_line ('个人借方减免: '||amt3); " +
                            "  " +
                            " dbms_output.put_line ('若对不上，使用下面的语句进行修复: ' ); " +
                            "  " +
                            " " +
                            " END; " +
                            " " );
            stmt.close();

              show = dbmsOutput.show();

            dbmsOutput.close();
        }catch(Exception e){
            throw e;
        }finally {
            conn.close();
        }

        return  show;
    }
}
