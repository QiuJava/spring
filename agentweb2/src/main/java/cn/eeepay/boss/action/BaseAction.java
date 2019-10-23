package cn.eeepay.boss.action;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;
import java.util.Random;

/**
 * Created by Lumia on 2017-02-21.
 */
public class BaseAction {
    /**
     * 返回一个固定格式的消息结构
     * @param status 状态
     * @param title 概要
     * @param body 具体内容
     * @return
     */
    protected Map<String,Object> getMsg(Boolean status,String title,String body){
        Map<String, Object> rt = new HashedMap();
        rt.put("status",status ? "true" : "false");
        rt.put("title",title);
        rt.put("body",body);
        return rt;
    }

    protected Integer getRandom(int len){
        Integer max = ((Double)Math.pow(10,len)).intValue();
        Integer random = new Random().nextInt(max);
        if(random.toString().length() < len){
            random = getRandom(len);
        }
        return random;
    }

    public static void main(String[] args) throws InterruptedException {
        BaseAction ba = new BaseAction();
        for(int i=0;i<100000000;i++){
            System.out.println("正常>>\t"+ba.getRandom(6));
        }
    }

}
