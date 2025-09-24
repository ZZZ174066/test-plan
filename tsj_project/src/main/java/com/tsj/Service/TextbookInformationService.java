package com.tsj.Service;

import com.tsj.Mapper.TextbookInformationMapper;
import com.tsj.project.TextbookInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 教材信息服务类
 */
@Service
public class TextbookInformationService {
    
    @Autowired
    private TextbookInformationMapper textbookMapper;
    
    /**
     * 创建教材信息
     * @param textbook 教材信息对象
     * @return 创建结果
     */
    public boolean createTextbook(TextbookInformation textbook) {
        try {
            // 验证必填字段
            if (textbook.getTextbookName() == null || textbook.getTextbookName().trim().isEmpty()) {
                throw new IllegalArgumentException("教材名不能为空");
            }
            if (textbook.getTextbookAuthor() == null || textbook.getTextbookAuthor().trim().isEmpty()) {
                throw new IllegalArgumentException("教材作者不能为空");
            }
            if (textbook.getTextbookVersion() == null || textbook.getTextbookVersion().trim().isEmpty()) {
                throw new IllegalArgumentException("教材版本不能为空");
            }
            if (textbook.getTextbookPublisher() == null || textbook.getTextbookPublisher().trim().isEmpty()) {
                throw new IllegalArgumentException("教材出版社不能为空");
            }
            
            
            return textbookMapper.createTextbook(textbook) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据ID获取教材信息
     * @param textbookId 教材ID
     * @return 教材信息对象
     */
    public TextbookInformation getTextbookById(Integer textbookId) {
        try {
            if (textbookId == null || textbookId <= 0) {
                return null;
            }
            return textbookMapper.getTextbookById(textbookId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取所有教材信息
     * @return 教材信息列表
     */
    public List<TextbookInformation> getAllTextbooks() {
        try {
            return textbookMapper.getAllTextbooks();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 更新教材信息
     * @param textbook 教材信息对象
     * @return 更新结果
     */
    public boolean updateTextbook(TextbookInformation textbook) {
        try {
            if (textbook.getTextbookId() == null || textbook.getTextbookId() <= 0) {
                return false;
            }
            
            // 验证必填字段
            if (textbook.getTextbookName() == null || textbook.getTextbookName().trim().isEmpty()) {
                throw new IllegalArgumentException("教材名不能为空");
            }
            if (textbook.getTextbookAuthor() == null || textbook.getTextbookAuthor().trim().isEmpty()) {
                throw new IllegalArgumentException("教材作者不能为空");
            }
            if (textbook.getTextbookVersion() == null || textbook.getTextbookVersion().trim().isEmpty()) {
                throw new IllegalArgumentException("教材版本不能为空");
            }
            if (textbook.getTextbookPublisher() == null || textbook.getTextbookPublisher().trim().isEmpty()) {
                throw new IllegalArgumentException("教材出版社不能为空");
            }
            
            
            return textbookMapper.updateTextbook(textbook) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 删除教材信息
     * @param textbookId 教材ID
     * @return 删除结果
     */
    public boolean deleteTextbook(Integer textbookId) {
        try {
            if (textbookId == null || textbookId <= 0) {
                return false;
            }
            return textbookMapper.deleteTextbook(textbookId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 根据教材名搜索教材
     * @param textbookName 教材名
     * @return 教材信息列表
     */
    public List<TextbookInformation> searchTextbooksByName(String textbookName) {
        try {
            if (textbookName == null || textbookName.trim().isEmpty()) {
                return getAllTextbooks();
            }
            return textbookMapper.searchTextbooksByName("%" + textbookName.trim() + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据作者搜索教材
     * @param author 作者名
     * @return 教材信息列表
     */
    public List<TextbookInformation> searchTextbooksByAuthor(String author) {
        try {
            if (author == null || author.trim().isEmpty()) {
                return getAllTextbooks();
            }
            return textbookMapper.searchTextbooksByAuthor("%" + author.trim() + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据出版社搜索教材
     * @param publisher 出版社名
     * @return 教材信息列表
     */
    public List<TextbookInformation> searchTextbooksByPublisher(String publisher) {
        try {
            if (publisher == null || publisher.trim().isEmpty()) {
                return getAllTextbooks();
            }
            return textbookMapper.searchTextbooksByPublisher("%" + publisher.trim() + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 获取教材总数
     * @return 教材总数
     */
    public int getTextbookCount() {
        try {
            return textbookMapper.getTextbookCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 分页获取教材信息
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @return 教材信息列表
     */
    public List<TextbookInformation> getTextbooksWithPagination(int page, int size) {
        try {
            if (page < 1) page = 1;
            if (size < 1) size = 10;
            
            int offset = (page - 1) * size;
            return textbookMapper.getTextbooksWithPagination(offset, size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
