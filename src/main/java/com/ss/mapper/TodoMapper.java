package com.ss.mapper;

import com.ss.vo.TodoVO;
import java.util.List;

public interface TodoMapper {
    void insertTodo(TodoVO todo);
    List<TodoVO> selectList();
    void deleteTodo(Long no);
}
