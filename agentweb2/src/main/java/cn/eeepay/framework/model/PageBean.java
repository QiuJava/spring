package cn.eeepay.framework.model;

/**
 * 分页bean
 * Created by 666666 on 2017/9/25.
 */
public class PageBean {

    private int pageNo;
    private int pageSize;
    public final static int DEFAULT_PAGE_SIZE = 20;
    public final static int DEFAULT_PAGE_NO = 1;

    public PageBean() {
        this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    }

    public PageBean(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo < DEFAULT_PAGE_NO){
            this.pageNo = DEFAULT_PAGE_NO;
        }else {
            this.pageNo = pageNo;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1){
            this.pageSize = DEFAULT_PAGE_SIZE;
        }else{
            this.pageSize = pageSize;
        }
    }
    public int getOffset() {
        return (getPageNo() - 1) * getPageSize();
    }

}
