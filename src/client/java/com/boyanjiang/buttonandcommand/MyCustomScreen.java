package com.boyanjiang.buttonandcommand;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.boyanjiang.CommandStorage;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen.*;

public class MyCustomScreen extends Screen {

    // private TextFieldWidget commandInput; // 定義輸入框
    // private TextFieldWidget commandInput2;
    // // 💡 關鍵：使用 static 變數來實現「跨次開啟」的記憶功能
    // private static String savedCommand = "";
    // private static String savedCommand2 = "";

    private final List<TextFieldWidget> inputs = new ArrayList<>();

    private final Screen parent; // 💡 關鍵：儲存上一個畫面

    // 修改建構子，接收 parent 參數
    public MyCustomScreen(Screen parent, Text title) {
        super(title);
        this.parent = parent;
    }

    @Override
    // 按鈕區域
    protected void init() {
        // 呼叫管理器，一次性加入所有按鈕
        // 使用 this::addDrawableChild 作為引導，將按鈕註冊進去
        // -
        // ButtonManager.addButtonsToScreen(this, this.width / 2 - 50, 40,
        // this::addDrawableChild);
        this.inputs.clear();
        int startY = 30; // 起始高度
        int spacing = 22; // 每行間距

        for (int i = 0; i < 10; i++) {
            final int index = i;

            // 1. 建立輸入框 (左側)
            TextFieldWidget textField = new TextFieldWidget(this.textRenderer,
                    this.width / 2 - 110, startY + (i * spacing), 150, 20, Text.of(""));
            textField.setMaxLength(128);
            textField.setText(CommandStorage.commands[i]); // 載入記憶
            this.addSelectableChild(textField);
            this.inputs.add(textField);

            // 2. 建立執行按鈕 (右側)
            this.addDrawableChild(ButtonWidget.builder(Text.literal("執行 " + CommandStorage.labels[i]), button -> {
                // 儲存當前輸入框的內容並執行
                String cmd = textField.getText();
                CommandStorage.commands[index] = cmd; // 更新記憶
                if (this.client != null && this.client.player != null) {
                    this.client.player.networkHandler.sendChatCommand(cmd);
                }
            }).dimensions(this.width / 2 + 45, startY + (i * spacing), 60, 20).build());
        }

    }

    // 關鍵修正：在 1.21.11 中，不要在 render 裡呼叫 renderBackground！
    @Override
    // 繪製區域
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 直接呼叫 super.render，它會自動處理背景渲染與按鈕繪製
        super.render(context, mouseX, mouseY, delta);

        // 2. 渲染所有輸入框
        for (TextFieldWidget input : inputs) {
            input.render(context, mouseX, mouseY, delta);
        }
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFAA00);

        // context.drawCenteredTextWithShadow(this.textRenderer,
        // Text.literal("指令自定義面板"), this.width / 2, 20, 0xFFFFAA00);

        // 如果你想畫額外的文字：
        // context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width
        // / 2, 20, 0xFFFFFF);
        // 0xFFAA00改用0xFFFFAA00
        // 繪製輸入框用
        // this.commandInput.render(context, mouseX, mouseY, delta);
        // this.commandInput2.render(context, mouseX, mouseY, delta);
        // context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("輸入指令 (不加
        // /):"), 60, 45, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        // 💡 關鍵：如果有 parent 就回到 parent (ModMenu)，沒有就關閉 (遊戲中)
        this.client.setScreen(this.parent);
    }

    // 1.21.11 必須覆寫這個方法來控制背景是否變暗/模糊
    @Override
    // 關閉背景區域
    public boolean shouldPause() {
        return true;
    }

    @Override
    public boolean keyPressed(KeyInput input) {

        // 2. 讓 10 個輸入框優先處理（確保能打字、刪除）
        for (TextFieldWidget textField : this.inputs) {
            // TextFieldWidget 依然需要原生 int 參數
            if (textField.keyPressed(input)) {
                return true;
            }
        }

        // 3. 呼叫 super 確保 ESC 鍵能正常關閉介面
        return super.keyPressed(input);
    }

    // 滑鼠點擊監聽事件
    /**
     * 提取出來的統一儲存邏輯，方便多處呼叫
     */
    private void saveAllFields() {
        for (int i = 0; i < inputs.size(); i++) {
            String currentText = inputs.get(i).getText();
            // 同步到你的靜態儲存類別
            CommandStorage.commands[i] = currentText;
        }
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        // 1. 先執行原生的點擊邏輯（這會處理輸入框的焦點切換）
        boolean processed = super.mouseClicked(click, doubled);

        // 2. 立即掃描所有輸入框並同步到記憶體
        saveAllFields();

        return processed;
    }


}