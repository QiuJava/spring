package cn.eeepay.framework.service.bill.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.UserRigthMapper;
import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.UserRigth;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.UserRigthService;
@Service("userRigthService")
@Transactional
public class UserRigthServiceImpl implements UserRigthService {
	@Resource
	public UserRigthMapper userRigthMapper;
	@Resource
	public ShiroRigthService shiroRigthService;


	@Override
	public int insertUserRigth(Integer userId, Integer rigthId) throws Exception {
		return userRigthMapper.insertUserRigth(userId, rigthId);
	}
	@Override
	public int deleteUserRigth(Integer userId, Integer rigthId) throws Exception {
		return userRigthMapper.deleteUserRigth(userId, rigthId);
	}
	@Override
	public int saveAllUserRigth(List<Map<String, String>> list) throws Exception {
		int i = 0;
		if (!list.isEmpty()) {
			String[] rightCodeArray = null;
			for (Map<String, String> item : list) {
				Integer uId = Integer.parseInt(item.get("userId"));
				Integer parentId = Integer.parseInt(item.get("parentId"));
				rightCodeArray = item.get("rightCode").split(",");
				i = i + this.saveUserRigth(uId, parentId, rightCodeArray);
			}
		}
		return i;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int saveUserRigth(Integer uId,Integer parentId, String[] rigthCodeArray) throws Exception {
		int i = 0;
		List<String> selectCheckBoxs = new ArrayList<>();
		List<String> unSelectCheckBoxs = new ArrayList<>();//没有勾选的checkbox
		List<ShiroRigth> shiroRigths = shiroRigthService.findShiroRigthByParentId(parentId);
		List<String> rigthCodeParentList = new ArrayList<>();//parentId对应的权限编码
		for (ShiroRigth shiroRigth : shiroRigths) {
			rigthCodeParentList.add(shiroRigth.getRigthCode());
		}
		rigthCodeParentList = new ArrayList(new HashSet(rigthCodeParentList));//去掉重复
		
		for (ShiroRigth shiroRigth : shiroRigths) {//清空parentId下原有的
			int n = this.deleteUserRigth(uId, shiroRigth.getId());
			i += n;
		}
		for (ShiroRigth shiroRigth : shiroRigths) {
			for (int j = 0; j < rigthCodeArray.length; j++) {
				String _rigthCode = rigthCodeArray[j];
				if (shiroRigth.getRigthCode().equals(_rigthCode)) {
					selectCheckBoxs.add(shiroRigth.getRigthCode());
					break;
				}
			}
		}
		selectCheckBoxs = new ArrayList(new HashSet(selectCheckBoxs));//去掉重复的
		unSelectCheckBoxs = (List<String>) ListUtils.subtract(rigthCodeParentList, selectCheckBoxs);//相减
		List<ShiroRigth> userRigths = shiroRigthService.findUserWithRolesPrivilegeRigthByParentId(uId,parentId);//通过角色授权的用户权限
		List<String> rigthCodeUserList = new ArrayList<>();//parentId对应的权限编码
		for (ShiroRigth shiroRigth : userRigths) {
			rigthCodeUserList.add(shiroRigth.getRigthCode());
		}
		rigthCodeUserList = new ArrayList(new HashSet(rigthCodeUserList));//去掉重复
		
//		userRigths = new ArrayList(new HashSet(userRigths));//去掉重复的
		List<String> srs1 =  ListUtils.subtract(selectCheckBoxs,rigthCodeUserList);//打勾的 减去 角色授权的 (要插入的)
		List<String> srs2 =  ListUtils.intersection(unSelectCheckBoxs,rigthCodeUserList);//没有打勾的 和角色授权的交集 (要插入的)
		List<String> srs = ListUtils.union(srs1, srs2);
		srs = new ArrayList(new HashSet(srs));//去掉重复的
		for (String sr : srs) {
			List<ShiroRigth> shiroRigthList = shiroRigthService.findShiroRigthByRigthCode(sr);
			int n = 0;
			for (ShiroRigth shiroRigth : shiroRigthList) {
				n += this.insertUserRigth(uId, shiroRigth.getId());
			}
			i += n;
		}
		return i;
	}
	@Override
	public List<UserRigth> findUserRigthByUserId(Integer userId) throws Exception {
		return userRigthMapper.findUserRigthByUserId(userId);
	}
	@Override
	public int deleteUserRigthByRigthId(Integer rigthId) throws Exception {
		return userRigthMapper.deleteUserRigthByRigthId(rigthId);
	}
	@Override
	public int deleteUserRigthByUserId(Integer userId) throws Exception {
		return userRigthMapper.deleteUserRigthByUserId(userId);
	}

}
