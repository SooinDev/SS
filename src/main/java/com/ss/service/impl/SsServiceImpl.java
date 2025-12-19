package com.ss.service.impl;

import com.ss.mapper.TodoMapper;
import com.ss.service.SsService;
import com.ss.vo.TodoVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SsServiceImpl implements SsService {

    private final TodoMapper todoMapper;

    public SsServiceImpl(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Override
    @Transactional
    public void addTodo(String content) {
        TodoVO todo = new TodoVO();
        todo.setContent(content);
        todoMapper.insertTodo(todo);
    }

    @Override
    public List<TodoVO> getTodoList() {
        return todoMapper.selectList();
    }

    @Override
    @Transactional
    public void deleteTodo(Long no) {
        todoMapper.deleteTodo(no);
    }
}