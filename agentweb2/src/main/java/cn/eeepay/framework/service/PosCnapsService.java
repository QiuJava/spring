package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.PosCnaps;

public interface PosCnapsService {

	List<PosCnaps> query(String bankName ,String cityName);
}
