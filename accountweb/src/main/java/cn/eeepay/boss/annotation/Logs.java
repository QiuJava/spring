package cn.eeepay.boss.annotation;
import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
/** 
 * 自定义注解 
 * @author zouruijin
 * zrj@eeepay.cn rjzou@qq.com
 * 2016年11月30日15:40:46
 */  
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface Logs {  
    String description() default "";  
}