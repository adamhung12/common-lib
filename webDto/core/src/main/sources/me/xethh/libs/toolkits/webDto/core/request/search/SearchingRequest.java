package me.xethh.libs.toolkits.webDto.core.request.search;

import me.xethh.libs.toolkits.webDto.core.request.Request;

public class SearchingRequest<Search extends SearchingCriteria> extends Request {

    private Search search;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
