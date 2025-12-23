package com.ss.controller;

import com.ss.config.AppConfig;
import com.ss.service.SsService;
import com.ss.vo.TodoVO;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

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

    public static void main(String[] args) throws Exception {
        // 0. UTF-8 인코딩 설정 (한글 최적화)
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("console.encoding", "UTF-8");

        // System.out을 UTF-8로 재설정
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("UTF-8 인코딩을 지원하지 않습니다.");
        }

        // JLine 터미널 및 LineReader 생성 (한글 백스페이스 문제 해결)
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .encoding("UTF-8")
                .build();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        // 1. 화면 초기화 및 로딩 연출
        clearScreen();
        showLoading("SYSTEM BOOT SEQUENCE");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SsService ssService = context.getBean(SsService.class);

        showLoading("CONNECTING TO DATABASE");
        clearScreen();
        printBanner();

        // 2. 메인 루프
        while (true) {
            String input = null;
            try {
                // JLine으로 입력 받기 (한글 백스페이스 정상 작동)
                input = lineReader.readLine(BOLD + "\n┌──(" + PURPLE + "ADMIN" + RESET + BOLD +
                        ")─[" + BLUE + "~" + RESET + BOLD + "]\n" +
                        "└─" + CYAN + " ▶ " + RESET);
            } catch (org.jline.reader.UserInterruptException e) {
                // Ctrl+C 처리
                continue;
            } catch (org.jline.reader.EndOfFileException e) {
                // Ctrl+D 처리
                break;
            }

            // 입력 정리: trim + 불필요한 공백 제거
            if (input != null) {
                input = input.trim().replaceAll("\\s+", " ");
            }

            if (input == null || input.isEmpty()) continue;

            String[] cmd = input.split(" ", 2); // 명령어와 내용 분리

            try {
                // Java 7/8에서는 switch문에 소문자 변환을 직접 해주는 것이 안전함
                String command = cmd[0].toLowerCase();

                if ("exit".equals(command)) {
                    showLoading("SHUTTING DOWN SYSTEM");
                    System.out.println(RED + "\n[SYSTEM] Disconnected." + RESET);
                    terminal.close();
                    System.exit(0);
                } else if ("add".equals(command)) {
                    if (cmd.length < 2) {
                        printError("내용을 입력해주세요. (예: add 회의 준비)");
                        continue;
                    }
                    String content = cmd[1].trim(); // 내용 앞뒤 공백 제거
                    ssService.addTodo(content);
                    printSuccess("DATA SAVED SUCCESSFULLY");
                    clearInputLine(); // 입력 라인 정리
                } else if ("list".equals(command)) {
                    List<TodoVO> list = ssService.getTodoList();
                    printTable(list);
                    clearInputLine(); // 입력 라인 정리
                } else if ("delete".equals(command)) {
                    if (cmd.length < 2) {
                        printError("삭제할 번호를 입력해주세요. (예: delete 1)");
                        continue;
                    }
                    long no = Long.parseLong(cmd[1].trim());
                    ssService.deleteTodo(no);
                    printSuccess("TARGET [" + no + "] REMOVED");
                    clearInputLine(); // 입력 라인 정리
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
        System.out.flush(); // 버퍼 비우기
        System.out.print(BOLD + "\n┌──(" + PURPLE + "ADMIN" + RESET + BOLD + ")─[" + BLUE + "~" + RESET + BOLD + "]\n" +
                "└─" + CYAN + " ▶ " + RESET);
        System.out.flush(); // 즉시 출력
    }

    // --- [UI] 테이블(목록) 출력 (한글 폭 고려) ---
    private static void printTable(List<TodoVO> list) {
        String line = repeatString("-", 70);

        System.out.println("\n" + CYAN + "   ID    | DATE              | CONTENT" + RESET);
        System.out.println(CYAN + "   " + line + RESET);

        if (list.isEmpty()) {
            System.out.println("   (No Data Available)");
        } else {
            for (TodoVO vo : list) {
                String dateStr = sdf.format(vo.getRegDate());
                String content = vo.getContent();

                // 한글 포함 시 정렬을 위한 패딩 계산
                String paddedContent = padRight(content, 40);

                System.out.printf("   %-5d | %-17s | %s\n",
                        vo.getNo(),
                        dateStr,
                        paddedContent);
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

    // --- [UI] 입력 라인 정리 (한글 입력 후 잔상 제거) ---
    private static void clearInputLine() {
        // 현재 줄 지우기 + 커서 위치 재설정
        System.out.print("\r\033[K");
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

    // --- [한글 최적화] 문자열의 실제 표시 폭 계산 (한글=2, 영문=1) ---
    private static int getDisplayWidth(String str) {
        if (str == null) return 0;
        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            // 한글, 한자, 일본어 등 전각 문자는 폭 2
            if (ch >= 0x1100 && ch <= 0x11FF ||   // 한글 자모
                    ch >= 0x3130 && ch <= 0x318F ||   // 한글 호환 자모
                    ch >= 0xAC00 && ch <= 0xD7AF ||   // 한글 음절
                    ch >= 0x3040 && ch <= 0x309F ||   // 히라가나
                    ch >= 0x30A0 && ch <= 0x30FF ||   // 가타카나
                    ch >= 0x4E00 && ch <= 0x9FFF ||   // CJK 통합 한자
                    ch >= 0xFF00 && ch <= 0xFFEF) {   // 전각 기호
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }

    // --- [한글 최적화] 문자열을 지정한 표시 폭에 맞춰 오른쪽 패딩 ---
    private static String padRight(String str, int displayWidth) {
        if (str == null) str = "";
        int currentWidth = getDisplayWidth(str);
        int padding = displayWidth - currentWidth;

        if (padding <= 0) {
            // 너무 긴 경우 잘라내기
            return truncateByWidth(str, displayWidth);
        }

        return str + repeatString(" ", padding);
    }

    // --- [한글 최적화] 문자열을 표시 폭에 맞춰 자르기 ---
    private static String truncateByWidth(String str, int maxWidth) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        int currentWidth = 0;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            int charWidth = (ch >= 0x1100 && ch <= 0x11FF ||
                    ch >= 0x3130 && ch <= 0x318F ||
                    ch >= 0xAC00 && ch <= 0xD7AF ||
                    ch >= 0x3040 && ch <= 0x309F ||
                    ch >= 0x30A0 && ch <= 0x30FF ||
                    ch >= 0x4E00 && ch <= 0x9FFF ||
                    ch >= 0xFF00 && ch <= 0xFFEF) ? 2 : 1;

            if (currentWidth + charWidth > maxWidth) {
                sb.append(".."); // 잘렸음을 표시
                break;
            }

            sb.append(ch);
            currentWidth += charWidth;
        }

        return sb.toString();
    }
}