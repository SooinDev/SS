package com.ss.controller;

import com.ss.config.AppConfig;
import com.ss.service.SsService;
import com.ss.vo.TodoVO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class MainController {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {

        System.out.println("시스템 부팅 중... (DB 연결 확인)");
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        SsService ss = context.getBean(SsService.class);

        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");

        System.out.println(ANSI_CYAN + "==========================================");
        System.out.println("   ss (Ver 1.0) - Online");
        System.out.println("   명령어: add [내용], list, delete [번호], exit");
        System.out.println("==========================================" + ANSI_RESET);

        while (true) {
            System.out.print(ANSI_YELLOW + "ss > " + ANSI_RESET);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("시스템을 종료합니다. Bye!");
                break;
            }

            else if (input.startsWith("add ")) {
                String content = input.substring(4);
                try {
                    ss.addTodo(content);
                    System.out.println(ANSI_GREEN + "할 일이 등록되었습니다." + ANSI_RESET);
                } catch (Exception e) {
                    System.out.println("등록 실패: " + e.getMessage());
                }
            }

            else if (input.equalsIgnoreCase("list")) {
                List<TodoVO> list = ss.getTodoList();
                System.out.println("--- To-Do List (" + list.size() + "개) ---");

                if (list.isEmpty()) {
                    System.out.println("(할 일이 없습니다)");
                } else {
                    for (TodoVO todo : list) {
                        String dateStr = sdf.format(todo.getRegDate());
                        System.out.printf("[%d] %s (%s)\n", todo.getNo(), todo.getContent(), dateStr);
                    }
                }
                System.out.println("--------------------------------");
            }

            else if (input.startsWith("delete ")) {
                try {
                    String noStr = input.substring(7).trim();
                    Long no = Long.parseLong(noStr);
                    ss.deleteTodo(no);
                    System.out.println(ANSI_GREEN + no + "번 할 일이 삭제되었습니다." + ANSI_RESET);
                } catch (NumberFormatException e) {
                    System.out.println("번호를 올바르게 입력해주세요. (예: delete 1)");
                } catch (Exception e) {
                    System.out.println("삭제 실패: " + e.getMessage());
                }
            }

            // 알 수 없는 명령어
            else {
                System.out.println("알 수 없는 명령입니다. (add [내용], list, exit)");
            }
        }

        scanner.close();
    }
}