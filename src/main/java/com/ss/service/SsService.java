package com.ss.service;

import com.ss.vo.TodoVO;
import java.util.List;

public interface SsService {
    void addTodo(String content);
    List<TodoVO> getTodoList();
    void deleteTodo(Long no);
}