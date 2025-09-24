package com.tsj.Service;
import com.tsj.Mapper.UserMapper;
import com.tsj.project.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    public List<User> getUser(){
        return userMapper.getUser();
    }
    
    public void insertUser(User user){
        // 设置默认值
        if (user.getAvatar() == null) {
            user.setAvatar("/src/assets/user/user.jpg");
        }
        if (user.getOnlineStatus() == null) {
            user.setOnlineStatus(0); // 默认离线
        }
        userMapper.insertUser(user);
    }
    
    public void deleteUser(Long id){
        userMapper.deleteUser(id);
    }
    
    public void updateUser(User user){
        userMapper.updateUser(user);
    }
    
    public User getUser2(Long id){
        return userMapper.getUser2(id);
    }
    
    // 添加新方法
    public void updateOnlineStatus(Long id, Integer status) {
        userMapper.updateOnlineStatus(id, status);
    }
    
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
    
    // 用户登录时更新在线状态
    public User login(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // 更新在线状态为在线
            updateOnlineStatus(user.getId(), 1);
            user.setOnlineStatus(1);
            return user;
        }
        return null;
    }
    
    // 用户登出时更新在线状态
    public void logout(Long userId) {
        if (userId != null) {
            updateOnlineStatus(userId, 0);
        }
    }
}