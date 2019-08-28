package me.xethh.libs.toolkits.webDto.core;

import me.xethh.libs.toolkits.webDto.core.general.page.PageConfig;
import me.xethh.libs.toolkits.webDto.core.general.page.PagedObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PageConfigTest {

    @Test
    public void baseTest()
    {
        PageConfig pc = new PageConfig(10, 1000);
        assertEquals(9001, pc.from());
        assertEquals(10000, pc.to());
        assertEquals(10, pc.getPage());
        assertEquals(1000, pc.getPageSize());

        pc = new PageConfig();
        try{
            pc.from();
            fail();
        }
        catch (PageConfig.PageConfigException ignored){
        }
        try{
            pc.to();
            fail();
        }
        catch (PageConfig.PageConfigException ignored){
        }
        assertEquals(0, pc.getPage());
        assertEquals(0, pc.getPageSize());

        pc = PageConfig.get().page(2).pageSize(222);
        assertEquals(223, pc.from());
        assertEquals(444, pc.to());
        assertEquals(430, pc.to(430));
        assertEquals(2, pc.getPage());
        assertEquals(222, pc.getPageSize());

        PageConfig.PageConfigValidation valid = null;
        valid = PageConfig.get().page(1).pageSize(200).valid();
        assertTrue(valid.isValid());

        valid = PageConfig.get().page(-1).pageSize(200).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(0).pageSize(200).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(-1).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(0).valid();
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(200).valid(-1);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(1).pageSize(200).valid(0);
        assertTrue(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(199);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(200);
        assertFalse(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(201);
        assertTrue(valid.isValid());

        valid = PageConfig.get().page(2).pageSize(200).valid(401);
        assertTrue(valid.isValid());
    }

    @Test
    public void testSplit(){
        PageConfig pageConfig = PageConfig.get(2, 3);
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> subList = pageConfig.paging(list);
        assertEquals((Integer)4,subList.get(0));
        assertEquals((Integer)5,subList.get(1));
        assertEquals((Integer)6,subList.get(2));

        PagedObject pagedObject = PagedObject.get(pageConfig,subList,list.size());
        assertEquals(4,pagedObject.getStartFrom());
        assertEquals(6, pagedObject.getEndTo());
        assertEquals(8, pagedObject.getTotal());
        assertEquals(subList,pagedObject.getData());

        pageConfig = PageConfig.get(1,10);
        list = Arrays.asList(10);
        List<Integer> data = pageConfig.paging(list);
        assertEquals(1, data.size());
        assertEquals((Integer)10,data.get(0));
    }
}