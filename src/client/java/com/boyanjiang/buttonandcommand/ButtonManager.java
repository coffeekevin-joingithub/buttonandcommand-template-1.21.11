package com.boyanjiang.buttonandcommand;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.*;


    public class ButtonManager {
    // 1. 集中管理這 10 個按鈕的資料

    public static final List<ButtonData> COMMAND_LIST = List.of(
        new ButtonData("傳送泡芙村莊", "ce"),
        new ButtonData("設為白天", "time set day"),
        new ButtonData("清除天氣", "weather clear"),
        new ButtonData("生存模式", "gamemode survival"),
        new ButtonData("創造模式", "gamemode creative"),
        new ButtonData("傳送地標A", "res tp a"),
        new ButtonData("傳送地標B", "res tp b"),
        new ButtonData("開啟領地清單", "res list"),
        new ButtonData("ce.bt2", "ce.bt2"),
        new ButtonData("查看領地資訊資訊", "res info")
    );

    /**
     * 批次將按鈕加入到 Screen 中
     */
    public static void addButtonsToScreen(Screen screen, int startX, int startY, Consumer<ClickableWidget> adder) {
        int yOffset = 0;
        for (ButtonData data : COMMAND_LIST) {
            // 建立按鈕
            ButtonWidget btn = ButtonWidget.builder(Text.literal(data.label()), button -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null) {
                    client.player.networkHandler.sendChatCommand(data.command());
                    screen.close(); // 執行後關閉
                }
            }).dimensions(startX, startY + yOffset, 100, 20).build();

            // 透過回呼函數加入到 Screen
            adder.accept(btn);
            
            // 每排 5 個按鈕換行，或單純向下延伸
            yOffset += 22; 
        }
    }
}
    
