package cn.eeepay.framework.dao;

import cn.eeepay.framework.util.ReadOnlyDataSource;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by 666666 on 2017/12/29.
 */
@WriteReadDataSource
public interface TestWriteDao {
    @Insert("insert into t_test(name)values(#{val})")
    int insert(@Param("val") String val);

    @Select("select name from t_test where id = #{id}")
    String query(@Param("id") String id);
}
