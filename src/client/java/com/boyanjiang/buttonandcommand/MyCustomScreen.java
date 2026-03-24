package com.boyanjiang.buttonandcommand;

// import org.lwjgl.glfw.GLFW;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
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
        // 嘗試新增 新增欄位 定義指令 綁定按鈕
        // 1. 建立輸入框 (TextFieldWidget)
        // 參數：字體渲染器, x, y, 寬, 高, 預設文字
        this.commandInput = new TextFieldWidget(this.textRenderer, 20, 60, 100, 20, Text.literal("輸入指令..."));
        this.commandInput.setMaxLength(128); // 設定最大字數
        this.commandInput.setText(savedCommand); // 記憶savedCommand
        this.addSelectableChild(this.commandInput); // 讓它可以被選中輸入

        this.commandInput2 = new TextFieldWidget(this.textRenderer, 140, 60, 100, 20, Text.literal("輸入指令..."));
        this.commandInput2.setMaxLength(128); // 設定最大字數
        this.commandInput2.setText(savedCommand2); // 記憶savedCommand2
        this.addSelectableChild(this.commandInput2); // 讓它可以被選中輸入

        // 2. 建立綁定按鈕
        this.addDrawableChild(ButtonWidget.builder(Text.literal("確認發送指令"), button -> {
            // 2. 建立「執行按鈕」 (取代原本的 Enter 邏輯)
            this.onExecute();
        }).dimensions(20, 90, 100, 20).build());

        // 2. 建立綁定按鈕
        this.addDrawableChild(ButtonWidget.builder(Text.literal("確認發送指令2"), button -> {
            // 2. 建立「執行按鈕」 (取代原本的 Enter 邏輯)
            this.onExecute2();
        }).dimensions(140, 90, 100, 20).build());

        // 1.21.11 的按鈕添加方式與以前相同
        // 繪製可以直接傳送到ce.bt的功能按鈕
        this.addDrawableChild(ButtonWidget.builder(Text.literal("傳送到ce.bt"), button -> {
            // 檢測是否在客戶端 檢測玩家是否在線
            if (this.client != null && this.client.player != null) {
                // 修改這裡：從 networkHandler 取得 chatSender 並發送指令
                this.client.player.networkHandler.sendChatCommand("res tp ce.bt");
                // 系統終端上顯示www
                System.out.println("www");
                // 關閉介面
                this.close();
            }
            // 在螢幕垂直正中心-50(按鈕長度) ,上方往下60, 按鈕長200, 高20 繪製按鈕
            // 改成螢幕左側數過來20
        }).dimensions(this.width / 2 - 50, 150, 100, 20).build());

        // 繪製關閉選單的按鈕
        // 💡 修正關閉按鈕邏輯：回到 parent 而不是直接關閉
        this.addDrawableChild(ButtonWidget.builder(Text.literal("關閉並返回"),
                button -> this.close())
                // 改成螢幕左側數過來20
                .dimensions(this.width / 2 - 50, 170, 100, 20).build());
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
        this.commandInput.render(context, mouseX, mouseY, delta);
        this.commandInput2.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("指令自定義面板"), this.width / 2, 20, 0xFFFFAA00);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("輸入指令 (不加 /):"), 60, 45, 0xFFFFFFFF);
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
        if (this.commandInput.keyPressed(input)) {
            return true;
        }

        // 2. 判斷特殊功能鍵 (例如 Enter 執行指令)
        // 使用 input.matches() 來比對按鍵，這是 1.21.11 的推薦作法
        // 2. 修正：直接存取 input 的 code() 方法並進行比對
        // 注意：在 1.21.11 中，KeyInput 是一個 record，所以使用 input.code()
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