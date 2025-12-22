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

    // --- [디자인] ANSI 색상 코드 ---
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";

    // --- [설정] 날짜 포맷 ---
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static void main(String[] args) throws InterruptedException {
        // 1. 화면 초기화 및 로딩 연출
        clearScreen();
        showLoading("SYSTEM BOOT SEQUENCE");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SsService ssService = context.getBean(SsService.class);
        Scanner scanner = new Scanner(System.in);

        showLoading("CONNECTING TO DATABASE");
        clearScreen();
        printBanner();

        // 2. 메인 루프
        while (true) {
            printPrompt();
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] cmd = input.split(" ", 2); // 명령어와 내용 분리

            try {
                // Java 7/8에서는 switch문에 소문자 변환을 직접 해주는 것이 안전함
                String command = cmd[0].toLowerCase();

                if ("exit".equals(command)) {
                    showLoading("SHUTTING DOWN SYSTEM");
                    System.out.println(RED + "\n[SYSTEM] Disconnected." + RESET);
                    System.exit(0);
                } else if ("add".equals(command)) {
                    if (cmd.length < 2) {
                        printError("내용을 입력해주세요. (예: add 회의 준비)");
                        continue;
                    }
                    ssService.addTodo(cmd[1]);
                    printSuccess("DATA SAVED SUCCESSFULLY");
                } else if ("list".equals(command)) {
                    List<TodoVO> list = ssService.getTodoList();
                    printTable(list);
                } else if ("delete".equals(command)) {
                    if (cmd.length < 2) {
                        printError("삭제할 번호를 입력해주세요. (예: delete 1)");
                        continue;
                    }
                    long no = Long.parseLong(cmd[1]);
                    ssService.deleteTodo(no);
                    printSuccess("TARGET [" + no + "] REMOVED");
                } else if ("help".equals(command)) {
                    printHelp();
                } else if ("cls".equals(command) || "clear".equals(command)) {
                    clearScreen();
                    printBanner();
                } else {
                    printError("UNKNOWN COMMAND. Type 'help' for info.");
                }

            } catch (NumberFormatException e) {
                printError("Invalid Number Format.");
            } catch (Exception e) {
                printError("System Error: " + e.getMessage());
            }
        }
    }

    // --- [UI] 로딩 애니메이션 ---
    private static void showLoading(String msg) throws InterruptedException {
        System.out.print(CYAN + "▒ " + msg + " " + RESET);
        for (int i = 0; i < 20; i++) {
            System.out.print(GREEN + "█" + RESET);
            Thread.sleep(30); // 0.03초 딜레이
        }
        System.out.println(GREEN + " [OK]" + RESET);
        Thread.sleep(300);
    }

    // --- [UI] 배너 출력 ---
    private static void printBanner() {
        System.out.println(CYAN + BOLD);
        System.out.println("   _____  _____      _______   _______ _______ ______ __  __ ");
        System.out.println("  / ____|/ ____|    / ____\\ \\ / / ____|__   __|  ____|  \\/  |");
        System.out.println(" | (___ | (________| (___  \\ V / (___    | |  | |__  | \\  / |");
        System.out.println("  \\___ \\ \\___ \\_____\\___ \\  > < \\___ \\   | |  |  __| | |\\/| |");
        System.out.println("  ____) |____) |    ____) |/ . \\____) |  | |  | |____| |  | |");
        System.out.println(" |_____/|_____/    |_____//_/ \\_\\_____/   |_|  |______|_|  |_|");
        System.out.println(RESET);
        System.out.println(YELLOW + "    :: SS-OS v1.0 :: " + RESET + "                     " + PURPLE + "Status: ONLINE" + RESET);
        System.out.println(CYAN + "   ==========================================================" + RESET);
    }

    // --- [UI] 프롬프트 출력 ---
    private static void printPrompt() {
        System.out.print(BOLD + "\n┌──(" + PURPLE + "ADMIN" + RESET + BOLD + ")─[" + BLUE + "~" + RESET + BOLD + "]\n" +
                "└─" + CYAN + " ▶ " + RESET);
    }

    // --- [UI] 테이블(목록) 출력 ---
    private static void printTable(List<TodoVO> list) {
        String line = repeatString("-", 60); // [수정] Java 8 호환 방식

        System.out.println("\n" + CYAN + "   ID    | DATE              | CONTENT" + RESET);
        System.out.println(CYAN + "   " + line + RESET);

        if (list.isEmpty()) {
            System.out.println("   (No Data Available)");
        } else {
            for (TodoVO vo : list) {
                // 글자수 정렬 포맷팅
                System.out.printf("   %-5d | %-17s | %s\n",
                        vo.getNo(),
                        sdf.format(vo.getRegDate()),
                        vo.getContent());
            }
        }
        System.out.println(CYAN + "   " + line + RESET);
        System.out.println("   Total: " + list.size() + " items found.");
    }

    // --- [UI] 도움말 ---
    private static void printHelp() {
        System.out.println(YELLOW + "\n   [ COMMAND LIST ]" + RESET);
        System.out.println("   * add [content]  : Create new task");
        System.out.println("   * list           : Show all tasks");
        System.out.println("   * delete [id]    : Remove task by ID");
        System.out.println("   * clear          : Clear screen");
        System.out.println("   * exit           : Power off");
    }

    // --- [UI] 메시지 헬퍼 ---
    private static void printSuccess(String msg) {
        System.out.println(GREEN + "   ✔ " + msg + RESET);
    }

    private static void printError(String msg) {
        System.out.println(RED + "   ✖ ERROR: " + msg + RESET);
    }

    // --- [UI] 화면 지우기 (ANSI 코드) ---
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // --- [추가] Java 8 호환을 위한 문자열 반복 메소드 ---
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}