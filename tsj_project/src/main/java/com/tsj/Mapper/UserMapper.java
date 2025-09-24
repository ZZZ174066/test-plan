package com.tsj.Mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.tsj.project.User;
import java.util.List;
@Mapper
public interface UserMapper {
    List<User> getUser();
    void insertUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User getUser2(Long id);
    
    // 添加新方法
    void updateOnlineStatus(@Param("id") Long id, @Param("status") Integer status);
    User getUserByUsername(String username);
}
