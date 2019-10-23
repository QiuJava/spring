package cn.eeepay.framework.model;

/**
 * Created by 666666 on 2018/3/7.
 */
public class AgentFunctionBean {
    private String functionNumber;
    private String functionName;
    private boolean functionValue;


    public String getFunctionNumber() {
        return functionNumber;
    }

    public AgentFunctionBean setFunctionNumber(String functionNumber) {
        this.functionNumber = functionNumber;
        return this;
    }

    public String getFunctionName() {
        return functionName;
    }

    public AgentFunctionBean setFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public boolean isFunctionValue() {
        return functionValue;
    }

    public AgentFunctionBean setFunctionValue(boolean functionValue) {
        this.functionValue = functionValue;
        return this;
    }
}
