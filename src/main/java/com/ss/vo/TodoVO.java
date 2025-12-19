package com.ss.vo;

import java.util.Date;

public class TodoVO {
    private Long no;
    private String content;
    private Date regDate;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "TodoVO{no=" + no + ", content='" + content + "', regDate=" + regDate + "}";
    }
}
