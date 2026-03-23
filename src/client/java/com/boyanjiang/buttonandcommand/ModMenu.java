package com.boyanjiang.buttonandcommand;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.text.Text;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // screen 代表 ModMenu 的列表畫面
        return screen -> new MyCustomScreen(screen, Text.literal("指令設定頁面"));
    }
}