package com.uv.executor;
/*
 * @author liuwei
 * @date 2019/2/19 16:42
 *
 */

import com.uv.config.MapperStatement;
import java.util.List;

public interface Executor {

    <T> List<T> query(MapperStatement statement, Object param);

}
