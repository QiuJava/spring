package cn.eeepay.framework.service;

/**
 * Created by 666666 on 2017/12/29.
 */
public interface TestService {
    
    String queryRead(String id);

    boolean insertRead(String val);

    boolean insertWrite(String val);

    String queryWrite(String id);

    boolean queryReadInsertWrite(String id);

    boolean queryWriteInsertRead(String id);

    boolean insertWriteInsertRead(String val);
}
