package com.platform.lxp.admin.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/4 19:37
 * @Description: 日期注解自动填充接口
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时的填充逻辑
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("触发自动填充：插入操作...");
        // 支持Date类型
        if (metaObject.hasSetter("createTime")) {
            if (metaObject.getSetterType("createTime").equals(Date.class)) {
                this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
            } else if (metaObject.getSetterType("createTime").equals(LocalDateTime.class)) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            }
        }
        // 同时填充updateTime
        if (metaObject.hasSetter("updateTime")) {
            if (metaObject.getSetterType("updateTime").equals(Date.class)) {
                this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
            } else if (metaObject.getSetterType("updateTime").equals(LocalDateTime.class)) {
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        }
    }

    /**
     * 更新时的填充逻辑 (如果你也有 updateTime 字段)
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("触发自动填充：更新操作...");
        if (metaObject.hasSetter("updateTime")) {
            if (metaObject.getSetterType("updateTime").equals(Date.class)) {
                this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
            } else if (metaObject.getSetterType("updateTime").equals(LocalDateTime.class)) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
            }
        }
    }
}
