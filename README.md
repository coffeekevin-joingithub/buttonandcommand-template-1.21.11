# ButtonAndCommand

> 一個為 Minecraft 1.21.11 Fabric 平台開發的快捷指令模組

---
![alt text](_DocAssets/pixel_x60.png)
## 📖 模組簡介

**ButtonAndCommand** 是一個輕量級的客戶端模組，讓你在遊戲中透過圖形介面，快速執行常用的聊天指令，不再需要每次手動輸入。  
適合搭配伺服器插件（如領地、傳送等）使用，大幅提升操作效率。

---

## 👥 開發者

- **BoyanJiang** — 主要開發者
- 本專案為協同開發的學習項目

---

## ✨ 功能特色

- 🖱️ **GUI 快捷指令面板** — 開啟一個畫面，列出 10 組可自訂的指令輸入欄與執行按鈕
- ⌨️ **快捷鍵觸發** — 預設按下 `G` 鍵即可開啟面板（可於遊戲控制設定中修改）
- 💾 **記憶上次輸入** — 關閉面板後再次開啟，指令欄位會保留你上次輸入的內容
- 🗂️ **ModMenu 整合** — 可從 ModMenu 的模組列表直接進入設定畫面
- 🌐 **繁體中文支援** — 介面與快捷鍵名稱皆有繁體中文語言檔

![alt text](_DocAssets/image.png)
---

## 🚀 使用方式

1. 進入遊戲後，按下 **`G`** 鍵（或你自訂的快捷鍵）開啟指令面板
2. 在左側輸入框填入你想執行的指令（**不需要加 `/`**）
3. 按下右側對應的「**執行 A～J**」按鈕，即可送出指令
4. 面板會記憶你輸入的內容，下次開啟時自動還原

---

## 🛠️ 環境需求

| 項目 | 版本需求 |
|------|----------|
| Minecraft | `~1.21.11` |
| Java | `>= 21` |
| Fabric Loader | `>= 0.18.4` |
| Fabric API | 任意版本 |
| ModMenu（選用） | 建議安裝，用於設定頁面整合 |

---

## 📁 專案架構說明

```
src/
├── main/
│   └── java/com/boyanjiang/buttonandcommand/
│       ├── ButtonAndCommand.java       # 模組主入口（伺服端/通用）
│       └── mixin/
│           └── ExampleMixin.java       # 伺服器 Mixin 範本（尚未使用）
│
└── client/
    └── java/com/boyanjiang/
        ├── CommandStorage.java         # 執行期記憶體狀態（指令與標籤陣列）
        └── buttonandcommand/
            ├── BACConfig.java               # 持久化儲存（讀寫 config/buttonandcommand.json）
            ├── ButtonAndCommandClient.java  # 客戶端入口，註冊快捷鍵、啟動時載入設定
            ├── MyCustomScreen.java          # GUI 主畫面，關閉時觸發存檔
            ├── ButtonData.java              # 按鈕資料 Record
            ├── ButtonManager.java           # 按鈕批次管理器（舊版設計，保留中）
            ├── ModMenu.java                 # ModMenu 整合入口
            └── mixin/client/
                └── ExampleClientMixin.java  # 客戶端 Mixin 範本（尚未使用）
```

---

## 📜 授權

本模組採用 **CC0-1.0** 授權，歡迎自由使用與修改。
