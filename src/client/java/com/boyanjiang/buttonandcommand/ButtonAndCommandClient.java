package com.boyanjiang.buttonandcommand;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
// import net.minecraft.client.gui.screen.*;
import net.minecraft.text.Text;

public class ButtonAndCommandClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BACConfig.load(); // 讀取配置檔案 BACConfig，初始化 CommandStorage

        // This entrypoint is suitable for setting up client-specific logic, such as
        // rendering.
        // 註冊快捷鍵：名稱、類型、預設按鍵、分類
        KeyBinding openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.mymod.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G, // 預設為 G 鍵
                KeyBinding.Category.MISC));

        // 註冊每一刻(Tick)的監聽，檢查按鍵是否被按下
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                // 當按鍵被按下時，開啟自定義界面
                client.setScreen(new MyCustomScreen(null, Text.literal("我的自定義界面")));
            }
        });
    }
}