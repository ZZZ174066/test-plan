package com.tsj.Mapper;

import com.tsj.project.NotificationInformation;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 通知信息Mapper接口
 */
@Mapper
public interface NotificationInformationMapper {
    
    /**
     * 获取所有通知信息
     * @return 通知信息列表
     */
    List<NotificationInformation> getAllNotifications();
    
    /**
     * 根据ID获取通知信息
     * @param notificationId 通知ID
     * @return 通知信息
     */
    NotificationInformation getNotificationById(@Param("notificationId") Integer notificationId);
    
    /**
     * 根据学科获取通知信息
     * @param course 学科名称
     * @return 通知信息列表
     */
    List<NotificationInformation> getNotificationsByCourse(@Param("course") String course);
    
    /**
     * 插入新通知信息
     * @param notification 通知信息对象
     * @return 影响行数
     */
    int insertNotification(NotificationInformation notification);
    
    /**
     * 更新通知信息
     * @param notification 通知信息对象
     * @return 影响行数
     */
    int updateNotification(NotificationInformation notification);
    
    /**
     * 删除通知信息
     * @param notificationId 通知ID
     * @return 影响行数
     */
    int deleteNotification(@Param("notificationId") Integer notificationId);
    
    /**
     * 获取最近的通知信息（限制数量）
     * @param limit 限制数量
     * @return 通知信息列表
     */
    List<NotificationInformation> getRecentNotifications(@Param("limit") Integer limit);
    
    /**
     * 根据关键词搜索通知信息
     * @param keyword 关键词
     * @return 通知信息列表
     */
    List<NotificationInformation> searchNotifications(@Param("keyword") String keyword);
    
    /**
     * 根据日期范围获取通知信息
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 通知信息列表
     */
    List<NotificationInformation> getNotificationsByDateRange(@Param("startDate") java.time.LocalDate startDate, 
                                                            @Param("endDate") java.time.LocalDate endDate);
    
    /**
     * 获取通知统计信息
     * @return 统计信息Map
     */
    java.util.Map<String, Object> getNotificationStats();
    
    /**
     * 根据学科统计通知数量
     * @return 学科通知统计列表
     */
    List<java.util.Map<String, Object>> getNotificationCountByCourse();
    
    /**
     * 获取今日发布的通知
     * @return 今日通知列表
     */
    List<NotificationInformation> getTodayNotifications();
    
    /**
     * 获取本周发布的通知
     * @return 本周通知列表
     */
    List<NotificationInformation> getWeeklyNotifications();
    
    /**
     * 批量插入通知信息
     * @param notifications 通知信息列表
     * @return 影响行数
     */
    int batchInsertNotifications(@Param("list") List<NotificationInformation> notifications);
    
    /**
     * 批量删除通知信息
     * @param ids 通知ID列表
     * @return 影响行数
     */
    int batchDeleteNotifications(@Param("list") List<Integer> ids);
}
