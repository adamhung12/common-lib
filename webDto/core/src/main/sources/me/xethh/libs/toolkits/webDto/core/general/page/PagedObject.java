package me.xethh.libs.toolkits.webDto.core.general.page;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xethhung
 * date 5/7/2018
 */
public class PagedObject<Data> {
    private PagedObject(int page, int pageSize, List<Data> data, int total){
        this.page=page;
        this.pageSize=pageSize;
        PageConfig config = config();
        this.startFrom=config.from();
        this.endTo = config.to(total);
        this.total=total;
        if(data.size()>pageSize)
            this.data  = data.subList(0, pageSize);
        else
            this.data = data;
    }
    public PageConfig config(){
        return new PageConfig(page, pageSize);
    }
    public static <Data> PagedObject<Data> get(PageConfig pageConfig, List<Data> data){
        return get(pageConfig, pageConfig.paging(data), data.size());
    }
    public static <Data> PagedObject<Data> get(PageConfig config, List<Data> data, int total){
        return new PagedObject(config.getPage(), config.getPageSize(), data, total);
    }
    private int pageSize;
    private int page;
    private int startFrom=-1;
    private int endTo=-1;
    private int total=-1;
    private Collection<Data> data = new LinkedList<>();

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(int startFrom) {
        this.startFrom = startFrom;
    }

    public int getEndTo() {
        return endTo;
    }

    public void setEndTo(int endTo) {
        this.endTo = endTo;
    }

    public Collection<Data> getData() {
        return data;
    }

    public void setData(Collection<Data> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
