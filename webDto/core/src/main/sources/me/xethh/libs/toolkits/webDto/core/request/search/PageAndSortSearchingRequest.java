package me.xethh.libs.toolkits.webDto.core.request.search;

import me.xethh.libs.toolkits.webDto.core.general.page.PageConfig;
import me.xethh.libs.toolkits.webDto.core.general.sort.Sorting;

import java.util.List;

public abstract class PageAndSortSearchingRequest<Data extends SearchingCriteria> extends SearchingRequest<Data> {
    private PageConfig page;
    private List<Sorting> sort;

    public PageConfig getPage() {
        return page;
    }

    public void setPage(PageConfig page) {
        this.page = page;
    }

    public List<Sorting> getSort() {
        return sort;
    }

    public void setSort(List<Sorting> sort) {
        this.sort = sort;
    }
}
