package test;
import cn.eeepay.framework.service.ActivationCodeService;
import cn.eeepay.framework.service.impl.ActivationCodeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

public class TestActivationCodeService {

    public static void main(String[] args) throws InterruptedException {
        final ActivationCodeService activationCodeService = new ActivationCodeServiceImpl();
        for(int i = 0; i < 3; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    activationCodeService.buildActivationCode(50);
                }
            }).start();
        }

        Thread.sleep(3100);
        activationCodeService.buildActivationCode(50);
    }
}
