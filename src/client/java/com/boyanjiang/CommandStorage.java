package com.boyanjiang;

// 新增對應十顆按鈕的 指令欄位
public class CommandStorage {
    // 預設 10 組指令與按鈕名稱
    public static String[] commands = new String[10];
    public static String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

    static {
        // 初始化預設值
        for (int i = 0; i < 10; i++) {
            commands[i] = "help " + (i + 1);
        }
    }
    
}
