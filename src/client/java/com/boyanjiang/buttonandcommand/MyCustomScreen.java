package com.boyanjiang.buttonandcommand;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.client.input.KeyInput;

public class MyCustomScreen extends Screen {

    private TextFieldWidget commandInput; // 定義輸入框
    private TextFieldWidget commandInput2;
    // 💡 關鍵：使用 static 變數來實現「跨次開啟」的記憶功能
    private static String savedCommand = "";
    private static String savedCommand2 = "";

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
        ButtonManager.addButtonsToScreen(this, this.width / 2 - 50, 40, this::addDrawableChild);

    }

    // 關鍵修正：在 1.21.11 中，不要在 render 裡呼叫 renderBackground！
    @Override
    // 繪製區域
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 直接呼叫 super.render，它會自動處理背景渲染與按鈕繪製
        super.render(context, mouseX, mouseY, delta);

        // 如果你想畫額外的文字：
        // context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width
        // / 2, 20, 0xFFFFFF);
        // 0xFFAA00改用0xFFFFAA00
        // 繪製輸入框用
        // this.commandInput.render(context, mouseX, mouseY, delta);
        // this.commandInput2.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("指令自定義面板"), this.width / 2, 20, 0xFFFFAA00);
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
        // 1. 優先讓輸入框處理（例如打字或刪除文字）
        // 在 1.21.11 中，Widget 也有對應的 KeyInput 處理方法
        // -
        // if (this.commandInput.keyPressed(input)) {
        // return true;
        // }

        // 2. 判斷特殊功能鍵 (例如 Enter 執行指令)
        // 使用 input.matches() 來比對按鍵，這是 1.21.11 的推薦作法
        // 2. 修正：直接存取 input 的 code() 方法並進行比對
        // 注意：在 1.21.11 中，KeyInput 是一個 record，所以使用 input.code()
        // -
        // if (input.getKeycode() == GLFW.GLFW_KEY_ENTER || input.getKeycode() ==
        // GLFW.GLFW_KEY_KP_ENTER) {
        // this.onExecute();
        // return true;
        // }

        // 3. 呼叫 super 確保 ESC 鍵能正常關閉介面
        return super.keyPressed(input);
    }

    // 提取出的執行邏輯
    private void onExecute() {
        String cmd = this.commandInput.getText();
        if (!cmd.isEmpty() && this.client != null && this.client.player != null) {
            // 儲存到記憶體中，下次打開介面時會自動填入
            savedCommand = cmd;

            // 發送指令
            this.client.player.networkHandler.sendChatCommand(cmd);
            System.out.println("Let's go : " + cmd);
            this.close();
        }
    }

    private void onExecute2() {
        String cmd = this.commandInput2.getText();
        if (!cmd.isEmpty() && this.client != null && this.client.player != null) {
            // 儲存到記憶體中，下次打開介面時會自動填入
            savedCommand2 = cmd;

            // 發送指令
            this.client.player.networkHandler.sendChatCommand(cmd);
            System.out.println("Let's go : " + cmd);
            this.close();
        }
    }

}