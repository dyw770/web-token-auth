package cn.dyw.auth.db.mapper;

import cn.dyw.auth.db.domain.SysApiResource;
import cn.dyw.auth.db.model.ApiResourceDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * api资源表 Mapper 接口
 * </p>
 *
 * @author dyw770
 * @since 2025-04-18
 */
@Mapper
public interface SysApiResourceMapper extends BaseMapper<SysApiResource> {

    /**
     * 查询所有api资源
     *
     * @return api资源
     */
    List<ApiResourceDto> queryAll();
    
    /**
     * 根据id查询api资源
     *
     * @param id api资源id
     * @return api资源
     */
    ApiResourceDto queryById(@Param("id") Integer id);
    
    /**
     * 根据id集合查询api资源
     *
     * @param ids api资源id集合
     * @return api资源
     */
    List<ApiResourceDto> queryByIds(@Param("ids") List<Integer> ids);
}

