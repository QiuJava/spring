package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.UserRigth;

public interface UserRigthService {
//	int insertUserRigth(UserRigth userRigth) throws Exception;
	int insertUserRigth(Integer userId,Integer rigthId) throws Exception;
//	int deleteUserRigth(UserRigth userRigth) throws Exception;
	int deleteUserRigth(Integer userId,Integer rigthId) throws Exception;
	int saveAllUserRigth(List<Map<String, String>> list) throws Exception;
	int saveUserRigth(Integer uId,Integer parentId, String[] rigthCodeArray) throws Exception;
	List<UserRigth> findUserRigthByUserId(Integer userId) throws Exception;
	int deleteUserRigthByRigthId(Integer rigthId) throws Exception;
	int deleteUserRigthByUserId(Integer userId) throws Exception;
}
