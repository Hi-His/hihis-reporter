package com.bsoft.report.controller;

import com.bsoft.report.dao.DbmsOutput;
import com.bsoft.report.dto.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
public class DFController {


    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping("/dfczj")
    @Transactional(readOnly = true)
    public String getdfczj(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";
        try {

            conn.setAutoCommit (false);
            Statement stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput( conn );

            dbmsOutput.enable( 1000000 );

            stmt.execute
                    (   "  declare  " +
                            " amt2 varchar2(32); " +
                            " amt3 varchar(32); " +
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
                            "  " +
                            " " +
                            " " +
                            "  " +
                            "select nvl(sum(amount), 0 )  into amt2 from ( " +
                            "  " +
                            "select t2.SD_PIPYPM_CD, to_char(trunc(nvl(sum(t1.AMT_PIPREPAY * t1.eu_direct), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BIL_PI_PREPAY t1 inner join HI_BIL_PI_PREPAY_PM t2 on t1.ID_PIPREPAY = t2.ID_PIPREPAY and t1.sd_piprepaytp_cd in ('1', '2') " +
                            "where 1=1 " +
                            "  and t1.id_cc =  cur.id_cc " +
                            "and t2.SD_PIPYPM_CD not in ('21', '23') " +
                            "group by t2.SD_PIPYPM_CD " +
                            "  " +
                            "); " +
                            " " +
                            "select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc; " +
                            "select TO_CHAR(cur.DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(cur.DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc; " +
                            " " +
                            " " +
                            "BEGIN " +
                            "select   " +
                            "nvl(sum(t2.amt_pypm-t2.amt_pypm_refund), 0 ) into amt3 " +
                            "  from HI_BIL_MED_CC t1 " +
                            "  join hi_bil_med_cc_total total " +
                            "  on t1.id_bilmedcctotal = total.id_bilmedcctotal " +
                            "  join HI_BIL_CC_PM t2 " +
                            "    on t1.id_cc = t2.id_cc " +
                            " where (t1.sd_vistp_cd = '111' or t1.sd_vistp_cd = '112') " +
                            "and t1.id_cc = cur.id_cc " +
                            "  and t1.id_dep_cc in  ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4') " +
                            "  and t2.SD_BSTP_CD = '1101' " +
                            "  and t2.sd_pipypm_cd not in ('2', '21', '22', '23') " +
                            "  and total.dt_cc_total >= time_begin  " +
                            "  and total.dt_cc_total <= time_end " +
                            "GROUP BY '储值金'; " +
                            "EXCEPTION " +
                            " WHEN NO_DATA_FOUND THEN " +
                            "amt3 := '0'; " +
                            "END; " +
                            " " +
                            "amt4 :=  NVL(amt2, 0) + NVL(amt4, 0); " +
                            " " +
                            " amt5 :=  NVL(amt3, 0) + NVL(amt5, 0); " +
                            " " +
                            "if(amt2 <> amt3) THEN " +
                            " dbms_output.put_line ('结账id：'|| cur.id_cc); " +
                            "dbms_output.put_line ('结账人员：' || name); " +
                            "dbms_output.put_line ('结账人员cd：' || cds); " +
                            "dbms_output.put_line ('结账开始时间：' || char_dt_cc_b); " +
                            "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e); " +
                            "    dbms_output.put_line (name||':'||amt2); " +
                            "dbms_output.put_line ('贷方储值金: '||amt3); " +
                            "  dbms_output.put_line ('   '); " +
                            "end if; " +
                            "  " +
                            "   end loop; " +
                            "   " +
                            " dbms_output.put_line ('个人贷方储值金合计：'||amt4); " +
                            "  dbms_output.put_line ('汇总贷方储值金合计：'||amt5); " +
                            "amt4 :=  NVL(amt4, 0) - NVL(amt5, 0); " +
                            " " +
                            "dbms_output.put_line ('个人 - 汇总：'|| amt4);  " +
                            " " +
                            "  dbms_output.put_line ('请根据结账id修复表【HI_BIL_CC_PM】储值金方式的金额'); " +
                            " END; " +
                            " " +
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


    @PostMapping("/dfghf")
    @Transactional(readOnly = true)
    public String getdfghf(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";
        try {
            conn.setAutoCommit (false);
            Statement stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput( conn );

            dbmsOutput.enable( 1000000 );

            stmt.execute
                    (   "  declare amt2 varchar2(32); " +
                            "  amt3 varchar2(32); " +
                            "  amt4 NUMBER(32,3) := 0; " +
                            "    amt5 NUMBER(32,3) := 0; " +
                            "  name   varchar2(32); " +
                            "  cds varchar2(32); " +
                            "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  char_dt_cc_b varchar2(32); " +
                            "  char_dt_cc_e varchar2(32); " +
                            " begin  " +
                            " " +
                            " " +
                            "  for cur in ( " +
                            "    SELECT " +
                            "  id_cc,DT_CC_B,dt_cc_e,id_emp_cc " +
                            " FROM " +
                            "  HI_BIL_med_cc  " +
                            " WHERE " +
                            "     id_bilmedcctotal in ( " +
                            "   select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total >  time_begin and dt_cc_total <= time_end " +
                            "   )   " +
                            "   ) " +
                            "   loop  " +
                            "    " +
                            "  " +
                            "  " +
                            "select nvl(sum(amount), 0 ) into amt2 from ( " +
                            "  " +
                            "select t2.na, to_char(trunc(nvl(sum(t4.TOTAL_AMT_SRV * t4.EU_DIRECT), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'3',0)) as amount from HI_BD_USDCA t1 " +
                            "left join HI_BD_USDCA_ITM t2 on t1.ID_USDCA = t2.ID_USDCA " +
                            "and (t2.DELETE_FLAG = '0' or t2.DELETE_FLAG is null) " +
                            "left join HI_BD_USDCA_ITM_LK t3 on t2.ID_USDCAITM = t3.ID_USDCAITM " +
                            "and (t3.DELETE_FLAG = '0' or t3.DELETE_FLAG is null) " +
                            "left join HI_BIL_MED_CG_OE t4 on (t3.id_itmlk = t4.ID_SRVCA or t3.id_itmlk = t4.ID_SRV) " +
                            "left join HI_BIL_MED_ST_OE t5 on t4.ID_MEDST_CG = t5.ID_MEDSTOE " +
                            "where 1=1 " +
                            "and t1.ID_USDCA = '611a0ca2a6600473ca1777c7' " +
                            " and t5.sd_medst_cd in ('112') and t4.fg_medcg = 1  and t5.id_cc =  cur.id_cc " +
                            "group by t2.na " +
                            " " +
                            "union all " +
                            "select t2.na, to_char(trunc(nvl(sum(t4.TOTAL_AMT_SRV * t4.EU_DIRECT), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'3',0)) as amount from HI_BD_USDCA t1 " +
                            "left join HI_BD_USDCA_ITM t2 on t1.ID_USDCA = t2.ID_USDCA " +
                            "and (t2.DELETE_FLAG = '0' or t2.DELETE_FLAG is null) " +
                            "left join HI_BD_USDCA_ITM_LK t3 on t2.ID_USDCAITM = t3.ID_USDCAITM " +
                            "and (t3.DELETE_FLAG = '0' or t3.DELETE_FLAG is null) " +
                            "left join HI_BIL_MED_CG_OE t4 on (t3.id_itmlk = t4.ID_SRVCA or t3.id_itmlk = t4.ID_SRV) " +
                            "left join HI_BIL_MED_ST_OE t5 on t4.ID_MEDST_CG = t5.ID_MEDSTOE " +
                            "where 1=1 " +
                            "and t1.ID_USDCA = '611a0ca2a6600473ca1777c7' " +
                            " and t5.sd_medst_cd in ('122') and t4.fg_medcg = 1   and t5.id_cc =  cur.id_cc " +
                            "group by t2.na " +
                            "  " +
                            "); " +
                            " " +
                            "begin " +
                            " " +
                            "select " +
                            "nvl(sum(b.EU_DIRECT * b.total_amt_srv), 0 )  into amt3 " +
                            "from HI_BIL_MED_CC_TOTAL a " +
                            "join HI_BIL_MED_CG_OE b " +
                            "on a.id_bilmedcctotal = b.id_bilmedcctotal " +
                            "join HI_BIL_MED_st_oe st on st.ID_MEDSTOE = b.ID_MEDST_CG " +
                            "and (b.fg_medst_canc_c = '0' or b.fg_medst_canc_c is null) " +
                            " " +
                            "  and st.SD_MEDST_CD  in ('112','122')  " +
                            "and a.id_dep_cc_total in ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4') " +
                            "and a.dt_cc_total >=   time_begin  " +
                            "  and a.dt_cc_total <=  time_end  " +
                            " and st.id_cc = cur.id_cc " +
                            " and a.DELETE_FLAG = '0' " +
                            "group by '挂号收入'; " +
                            "EXCEPTION " +
                            "  WHEN NO_DATA_FOUND THEN " +
                            "   amt3 := '0'; " +
                            "end; " +
                            " " +
                            " " +
                            "  select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc; " +
                            "  select TO_CHAR(DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc; " +
                            "   " +
                            "  amt4 :=  NVL(amt2, 0) + NVL(amt4, 0); " +
                            "  " +
                            "  amt5 :=  NVL(amt3, 0) + NVL(amt5, 0); " +
                            "  " +
                            "  if(amt2 <> amt3) THEN " +
                            "   dbms_output.put_line ('结账id：'|| cur.id_cc); " +
                            "  dbms_output.put_line ('结账人员：' || name); " +
                            "  dbms_output.put_line ('结账人员cd：' || cds); " +
                            "  dbms_output.put_line ('结账开始时间：' || char_dt_cc_b); " +
                            "    dbms_output.put_line ('结账结束时间：' || char_dt_cc_e); " +
                            "    dbms_output.put_line (name||':'||amt2); " +
                            "  dbms_output.put_line ('贷方挂号收入: '||amt3); " +
                            "   dbms_output.put_line ('   '); " +
                            " end if; " +
                            "    " +
                            "   end loop; " +
                            "   " +
                            "       " +
                            "  dbms_output.put_line ('个人贷方挂号合计：'||amt4); " +
                            "      dbms_output.put_line ('汇总贷方挂号合计：'||amt5); " +
                            "  amt4 :=  NVL(amt4, 0) - NVL(amt5, 0); " +
                            "   " +
                            "  dbms_output.put_line ('个人 - 汇总：'|| amt4);  " +
                            "   " +
                            " END; " +
                            " " +
                            "  " +
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


    @PostMapping("/dfsf")
    @Transactional(readOnly = true)
    public String getdfsf(@RequestBody Param param) throws SQLException {


        Connection conn = jdbcTemplate.getDataSource().getConnection();
        String show = "";

        try {
            conn.setAutoCommit (false);
            Statement stmt = conn.createStatement();

            DbmsOutput dbmsOutput = new DbmsOutput( conn );

            dbmsOutput.enable( 1000000 );

            stmt.execute
                    (   "  declare    " +
                            "  amt2 varchar2(32);   " +
                            "  amt3 varchar(32);   " +
                            "    amt4 NUMBER(32,3) := 0;   " +
                            "   amt5 NUMBER(32,3) := 0;   " +
                            "  name   varchar2(32);   " +
                            "  cds varchar2(32);   " +
                            "  time_begin  DATE := to_date('"+param.getStart()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  time_end  date := to_date('"+param.getEnd()+"' ,'yyyy-mm-dd hh24:mi:ss');  " +
                            "  char_dt_cc_b varchar2(32);   " +
                            "  char_dt_cc_e varchar2(32);   " +
                            " begin    " +
                            "   " +
                            "  for cur in (   " +
                            "    SELECT   " +
                            "  id_cc,DT_CC_B,dt_cc_e,id_emp_cc   " +
                            " FROM   " +
                            "  HI_BIL_med_cc    " +
                            " WHERE   " +
                            "     id_bilmedcctotal in (   " +
                            "   select ID_BILMEDCCTOTAL from hi_bil_med_cc_total where dt_cc_total > time_begin and dt_cc_total <= time_end   " +
                            "   )     " +
                            "   )   " +
                            "   loop    " +
                            "      " +
                            "    " +
                            "    " +
                            "select nvl(sum(amount), 0 ) into amt2 from (   " +
                            "    " +
                            "   " +
                            "    " +
                            "select t2.na, to_char(trunc(nvl(sum(t4.TOTAL_AMT_SRV * t4.EU_DIRECT), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BD_USDCA t1   " +
                            "left join HI_BD_USDCA_ITM t2 on t1.ID_USDCA = t2.ID_USDCA   " +
                            "and (t2.DELETE_FLAG = '0' or t2.DELETE_FLAG is null)   " +
                            "left join HI_BD_USDCA_ITM_LK t3 on t2.ID_USDCAITM = t3.ID_USDCAITM   " +
                            "and (t3.DELETE_FLAG = '0' or t3.DELETE_FLAG is null)   " +
                            "left join HI_BIL_MED_CG_OE t4 on (t3.id_itmlk = t4.ID_SRVCA or t3.id_itmlk = t4.ID_SRV)   " +
                            "left join HI_BIL_MED_ST_OE t5 on t4.ID_MEDST_CG = t5.ID_MEDSTOE   " +
                            "where 1=1   " +
                            "and t1.ID_USDCA = '611a0ca2a6600473ca1777c7'   " +
                            " and t5.sd_medst_cd in ('111') and t4.fg_medcg = 1  and t5.id_cc =  cur.id_cc   " +
                            "group by t2.na   " +
                            "   " +
                            "union all   " +
                            "select t2.na, to_char(trunc(nvl(sum(t4.TOTAL_AMT_SRV * t4.EU_DIRECT), 0), 4), RPAD('fm9999999990.',length('fm9999999990.')+'2',0)) as amount from HI_BD_USDCA t1   " +
                            "left join HI_BD_USDCA_ITM t2 on t1.ID_USDCA = t2.ID_USDCA   " +
                            "and (t2.DELETE_FLAG = '0' or t2.DELETE_FLAG is null)   " +
                            "left join HI_BD_USDCA_ITM_LK t3 on t2.ID_USDCAITM = t3.ID_USDCAITM   " +
                            "and (t3.DELETE_FLAG = '0' or t3.DELETE_FLAG is null)   " +
                            "left join HI_BIL_MED_CG_OE t4 on (t3.id_itmlk = t4.ID_SRVCA or t3.id_itmlk = t4.ID_SRV)   " +
                            "left join HI_BIL_MED_ST_OE t5 on t4.ID_MEDST_CG = t5.ID_MEDSTOE   " +
                            "where 1=1   " +
                            "and t1.ID_USDCA = '611a0ca2a6600473ca1777c7'   " +
                            " and t5.sd_medst_cd in ('123') and t4.fg_medcg = 1 and t5.id_cc =  cur.id_cc   " +
                            "group by t2.na   " +
                            "    " +
                            ");   " +
                            "   " +
                            "BEGIN   " +
                            " select   " +
                            " nvl(sum(b.EU_DIRECT * b.total_amt_srv), 0 ) into amt3   " +
                            " from HI_BIL_MED_CC_TOTAL a   " +
                            " join HI_BIL_MED_CG_OE b   " +
                            " on a.id_bilmedcctotal = b.id_bilmedcctotal   " +
                            " join HI_BIL_MED_st_oe st on st.ID_MEDSTOE = b.ID_MEDST_CG   " +
                            " and (b.fg_medst_canc_c = '0' or b.fg_medst_canc_c is null)   " +
                            " and st.SD_MEDST_CD not in ('112','122')   " +
                            " join HI_BD_USDCA_ITM_LK c on b.ID_SRVCA = c.ID_ITMLK and (c.delete_flag = '0' or c.delete_flag is null )   " +
                            " join HI_BD_USDCA_ITM d on c.ID_USDCAITM = d.ID_USDCAITM and d.delete_flag = '0'   " +
                            " join HI_BD_USDCA e on d.ID_USDCA = e.ID_USDCA and e.ID_USDCA = '611a0ca2a6600473ca1777c7'   " +
                            " where  a.id_dep_cc_total in ('6163dda964de2a4f2f57a30c','61bd3c5d64de2a3adc8423dd','610cf468bd06d3454cfa5820','619cb8ff529fe312040e59b4')   " +
                            "    " +
                            "  and st.id_cc = cur.id_cc   " +
                            "  and a.dt_cc_total > time_begin    " +
                            "  and a.dt_cc_total <= time_end    " +
                            " group by '收费收入';   " +
                            "    " +
                            "  EXCEPTION   " +
                            "  WHEN NO_DATA_FOUND THEN   " +
                            "   amt3 := '0';   " +
                            "end;   " +
                            "   " +
                            "   " +

                            "  select na ,cd into name, cds from PRD_BBP.HI_SYS_USER WHERE ID_USER = cur.id_emp_cc;   " +
                            "  select TO_CHAR(DT_CC_B,'yyyy-mm-dd HH24:mi:ss')   , TO_CHAR(DT_CC_e,'yyyy-mm-dd HH24:mi:ss') into char_dt_cc_b, char_dt_cc_e from HI_BIL_MED_CC where id_cc = cur.id_cc;   " +
                            "      " +
                            "   amt4 :=  NVL(amt2, 0) + NVL(amt4, 0);   " +
                            "    " +
                            "       " +
                            "  amt5 :=  NVL(amt3, 0) + NVL(amt5, 0);   " +
                            "     " +
                            "  if(abs(amt2 - amt3) > 0.1) THEN   " +
                            "    dbms_output.put_line ('结账id：'|| cur.id_cc);   " +
                            "   dbms_output.put_line ('结账人员：' || name);   " +
                            "   dbms_output.put_line ('结账人员cd：' || cds);   " +
                            "   dbms_output.put_line ('结账开始时间：' || char_dt_cc_b);   " +
                            "   dbms_output.put_line ('结账结束时间：' || char_dt_cc_e);   " +
                            "   dbms_output.put_line (name||':'||amt2);   " +
                            "   dbms_output.put_line ('贷方收费收入: '||amt3);   " +
                            "   dbms_output.put_line ('   ');   " +
                            " end if;   " +
                            "      " +
                            "   end loop;   " +
                            "     " +
                            "    dbms_output.put_line ('个人贷方收费合计：'||amt4);   " +
                            "   dbms_output.put_line ('个人贷方收费合计：'||amt5);   " +
                            "      " +
                            "   amt4 :=  NVL(amt4, 0) - NVL(amt5, 0);   " +
                            "     " +
                            "    dbms_output.put_line ('个人 - 汇总：'|| amt4);    " +
                            " END;   " +
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
