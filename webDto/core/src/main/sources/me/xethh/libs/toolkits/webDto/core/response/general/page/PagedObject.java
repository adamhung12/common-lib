package me.xethh.libs.toolkits.webDto.core.response.general.page;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xethhung
 * @date 5/7/2018
 */
public class PagedObject<Data> {
    private PagedObject(int page, int pageSize, List<Data> data){
        this.page=page;
        this.pageSize=pageSize;
        this.startFrom=(page-1)*pageSize+1;
        this.endTo=Math.min(page*pageSize,data.size());
        this.total=data.size();
        this.data = data.subList((page-1)*pageSize,Math.min(page*pageSize,data.size()));
    }
    private PagedObject(List<Data> data){
        this.page=1;
        this.pageSize=data.size();
        this.startFrom=1;
        this.endTo=data.size();
        this.total=data.size();
        this.data = data;
    }
    private PagedObject(int page, int pageSize, List<Data> data, int startFrom, int endTo, int total){
        this.page=page;
        this.pageSize=pageSize;
        this.startFrom=startFrom;
        this.endTo=endTo;
        this.total=total;
        this.data = data;
    }
    public static <Data> PagedObject get(List<Data> data){
        return new PagedObject(data);
    }
    public static <Data> PagedObject get(int page, int pageSize, List<Data> data){
        return new PagedObject(page, pageSize, data);
    }
    public static <Data> PagedObject get(int page, int pageSize, int startFrom, int endTo, int total, List<Data> data){
        return new PagedObject(page, pageSize, data, startFrom, endTo,total);
    }
    public static <Data> PagedObject get(PageConfig pageConfig, List<Data> data){
        return pageConfig ==null?new PagedObject(data):new PagedObject(pageConfig.getPage(), pageConfig.getPageSize(),data);
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
