package com.tsj.Mapper;

import com.tsj.project.TextbookInformation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 教材信息Mapper接口
 */
@Mapper
public interface TextbookInformationMapper {
    
    /**
     * 创建教材信息
     * @param textbook 教材信息对象
     * @return 影响行数
     */
    int createTextbook(TextbookInformation textbook);
    
    /**
     * 根据ID获取教材信息
     * @param textbookId 教材ID
     * @return 教材信息对象
     */
    TextbookInformation getTextbookById(@Param("textbookId") Integer textbookId);
    
    /**
     * 获取所有教材信息
     * @return 教材信息列表
     */
    List<TextbookInformation> getAllTextbooks();
    
    /**
     * 更新教材信息
     * @param textbook 教材信息对象
     * @return 影响行数
     */
    int updateTextbook(TextbookInformation textbook);
    
    /**
     * 删除教材信息
     * @param textbookId 教材ID
     * @return 影响行数
     */
    int deleteTextbook(@Param("textbookId") Integer textbookId);
    
    /**
     * 根据教材名搜索教材
     * @param textbookName 教材名
     * @return 教材信息列表
     */
    List<TextbookInformation> searchTextbooksByName(@Param("textbookName") String textbookName);
    
    /**
     * 根据作者搜索教材
     * @param author 作者名
     * @return 教材信息列表
     */
    List<TextbookInformation> searchTextbooksByAuthor(@Param("author") String author);
    
    /**
     * 根据出版社搜索教材
     * @param publisher 出版社名
     * @return 教材信息列表
     */
    List<TextbookInformation> searchTextbooksByPublisher(@Param("publisher") String publisher);
    
    /**
     * 获取教材总数
     * @return 教材总数
     */
    int getTextbookCount();
    
    /**
     * 分页获取教材信息
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 教材信息列表
     */
    List<TextbookInformation> getTextbooksWithPagination(@Param("offset") int offset, @Param("limit") int limit);
}
