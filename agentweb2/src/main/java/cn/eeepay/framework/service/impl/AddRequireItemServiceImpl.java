package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AddRequireItemDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.service.AddRequireItemService;
import cn.eeepay.framework.util.ALiYunOssUtil;
import cn.eeepay.framework.util.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("addRequireItemService")
public class AddRequireItemServiceImpl implements AddRequireItemService {

	@Resource
	private AddRequireItemDao addRequireItemDao;
	
	@Resource
	private SeqService seqService;
	
	@Override
	public List<AddRequireItem> selectByName(Page<AddRequireItem> page,String itemName) {
		return addRequireItemDao.selectByName(page,itemName);
	}

	@Override
	public AddRequireItem selectById(String id) {
		// TODO Auto-generated method stub
		AddRequireItem item = addRequireItemDao.selectById(id);
		if(item != null && item.getPhotoAddress()!=null){
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, item.getPhotoAddress(), new Date(64063065600000L));
			item.setPhotoAddressUrl(url);
		}
		return item;
	}

	@Override
	public int insertOrUpDate(AddRequireItem item) {
		// TODO Auto-generated method stub
		if(item==null){
			return 0;
		}
		if(!"3".equals(item.getExampleType())){
			item.setExample(null);
		}else{
			item.setPhotoAddressUrl(null);
		}
		if(item.getItemId()!=null){
			return addRequireItemDao.update(item);
		}else{
		//	item.setItemId(seqService.createKey(Constants.REQUIRE_SEQ,"%01d"));
			return addRequireItemDao.insert(item);
		}
	}
	@Override
	public AddRequireItem selectByMeriId(String meriId) {
		return addRequireItemDao.selectByMeriId(meriId);
	}

	@Override
	public AddRequireItem selectRequireName(String requireId) {
		return addRequireItemDao.selectRequireName(requireId);
	}

	@Override
	public List<AddRequireItem> selectAllRequireName() {
		return addRequireItemDao.selectAllRequireName();
	}

	@Override
	public List<AddRequireItem> queryItemNameList(Page<TerminalInfo> page) {
		return addRequireItemDao.queryItemNameList(page);
	}
	
	@Override
	public List<AddRequireItem> getRequireItemByParams(String agent_no, String bp_id) {
		List<AddRequireItem> itemList = addRequireItemDao.getRequireItemByParams(agent_no,bp_id);		
		for(AddRequireItem addRequireItem :itemList) {
			String photo = addRequireItem.getPhoto();
			Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() + 3600 * 1000);
			String urlStr = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, photo, expiresDate);
			addRequireItem.setPhoto(urlStr);
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, addRequireItem.getPhotoAddress(), new Date(64063065600000L));
			addRequireItem.setPhotoAddressUrl(url);
		}
		return itemList;
	}

	@Override
	public List<AddRequireItem> getRequireItemByType(String type) {
		List<Integer> intoList = new ArrayList<>();
		intoList.add(9);
		intoList.add(10);
		intoList.add(11);
		intoList.add(24);
		intoList.add(12);
		intoList.add(25);
		intoList.add(26);
		intoList.add(13);
		intoList.add(14);
		intoList.add(27);
		
		if ("2".equals(type)) {
			intoList.add(23);
			intoList.add(28);
			intoList.add(29);
		}
		List<AddRequireItem> list = addRequireItemDao.findRequireItemByNameList(intoList);
		for(AddRequireItem addRequireItem :list) {
			String photo = addRequireItem.getPhoto();
			Date expiresDate = new Date(Calendar.getInstance().getTime().getTime() * 3600 * 1000);
			String urlStr = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, photo, expiresDate);
			addRequireItem.setPhoto(urlStr);
			String url = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, addRequireItem.getPhotoAddress(), new Date(64063065600000L));
			addRequireItem.setPhotoAddressUrl(url);
		}
		
		return list;
	}

}
