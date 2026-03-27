package com.boyanjiang.buttonandcommand;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

import com.boyanjiang.CommandStorage;

public class BACConfig {

    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("buttonandcommand.json");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // 對應 JSON 的資料結構
    private static class ConfigData {
        String[] commands = new String[10];
        String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
    }

    // 讀取 JSON → 存進 CommandStorage
    public static void load() {
        File file = CONFIG_PATH.toFile();

        if (!file.exists()) {
            save(); // 第一次啟動，建立預設檔案
            return;
        }

        try (Reader reader = new FileReader(file)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            if (data != null) {
                for (int i = 0; i < 10; i++) {
                    if (data.commands[i] != null)
                        CommandStorage.commands[i] = data.commands[i];
                    if (data.labels[i] != null)
                        CommandStorage.labels[i] = data.labels[i];
                }
            }
        } catch (IOException e) {
            System.err.println("[ButtonAndCommand] ConfigData 讀取失敗：" + e.getMessage());
        }
    }

    // 把 CommandStorage 的內容寫進 JSON
    public static void save() {
        ConfigData data = new ConfigData();
        data.commands = CommandStorage.commands.clone();
        data.labels = CommandStorage.labels.clone();

        try (Writer writer = new FileWriter(CONFIG_PATH.toFile())) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("[ButtonAndCommand] ConfigData 儲存失敗：" + e.getMessage());
        }
    }
}