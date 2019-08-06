package me.xethh.libs.toolkits.webDto.core.response.general;

import me.xethh.libs.toolkits.webDto.core.response.Response;
import me.xethh.libs.toolkits.webDto.core.response.general.page.PagedObject;

import java.util.List;

public abstract class PagingResponse<Data, Err> extends Response<PagedObject<Data>, Err> {
}
