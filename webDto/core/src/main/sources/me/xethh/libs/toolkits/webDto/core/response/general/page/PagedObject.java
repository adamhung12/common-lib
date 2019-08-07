package me.xethh.libs.toolkits.webDto.core.response.general.page;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xethhung
 * date 5/7/2018
 */
public class PagedObject<Data> {
    private PagedObject(long page, long pageSize, List<Data> data){
        PageConfig config = new PageConfig(page, pageSize);
        this.page=config.getPage();
        this.pageSize=config.getPageSize();
        this.startFrom=config.from();
        this.endTo = config.to(data.size());
        this.total=data.size();
        this.data = data.subList((int)startFrom,(int)endTo);
    }
    public static <Data> PagedObject get(long page, long pageSize, List<Data> data){
        return new PagedObject(page, pageSize, data);
    }
    private long pageSize;
    private long page;
    private long startFrom=-1;
    private long endTo=-1;
    private long total=-1;
    private Collection<Data> data = new LinkedList<>();

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(long startFrom) {
        this.startFrom = startFrom;
    }

    public long getEndTo() {
        return endTo;
    }

    public void setEndTo(long endTo) {
        this.endTo = endTo;
    }

    public Collection<Data> getData() {
        return data;
    }

    public void setData(Collection<Data> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
