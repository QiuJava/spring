package cn.eeepay.boss.action;

import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.TestService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by 666666 on 2017/12/29.
 */
@RestController
@RequestMapping("/test")
public class TestAction {

    @Resource
    private TestService testService;

    @RequestMapping("/insertRead/{val}")
    public ResponseBean insertRead(@PathVariable String val){
        return new ResponseBean(testService.insertRead(val));
    }

    @RequestMapping("/insertWrite/{val}")
    public ResponseBean insertWrite(@PathVariable String val){
        return new ResponseBean(testService.insertWrite(val));
    }

    @RequestMapping("/queryRead/{id}")
    public ResponseBean queryRead(@PathVariable String id){
        return new ResponseBean(testService.queryRead(id));
    }

    @RequestMapping("/queryWrite/{id}")
    public ResponseBean queryWrite(@PathVariable String id){
        return new ResponseBean(testService.queryWrite(id));
    }

    @RequestMapping("/queryReadInsertWrite/{id}")
    public ResponseBean queryReadInsertWrite(@PathVariable String id){
        return new ResponseBean(testService.queryReadInsertWrite(id));
    }

    @RequestMapping("/queryWriteInsertRead/{id}")
    public ResponseBean queryWriteInsertRead(@PathVariable String id){
        return new ResponseBean(testService.queryWriteInsertRead(id));
    }
    @RequestMapping("/insertWriteInsertRead/{val}")
    public ResponseBean insertWriteInsertRead(@PathVariable String val){
        return new ResponseBean(testService.insertWriteInsertRead(val));
    }
}
