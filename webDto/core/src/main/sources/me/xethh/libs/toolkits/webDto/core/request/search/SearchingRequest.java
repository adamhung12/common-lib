package me.xethh.libs.toolkits.webDto.core.request.search;

import me.xethh.libs.toolkits.webDto.core.WebBaseEntity;

public class SearchingRequest<Search extends SearchingCriteria> extends WebBaseEntity {

    private Search search;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
