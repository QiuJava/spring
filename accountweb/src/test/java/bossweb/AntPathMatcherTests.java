package bossweb;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.eeepay.framework.util.AntUrlPathMatcher;
import cn.eeepay.framework.util.UrlMatcher;

public class AntPathMatcherTests {
	private UrlMatcher urlMatcher = new AntUrlPathMatcher();
	@Test
    public void match() {
        // ...
        assertTrue(urlMatcher.pathMatchesUrl("/sysAction/toSysDict.do**","/sysAction/toSysDict.do?p=hello.do"));
        // ...
    }
}
