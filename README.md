# SimpleEss

SimpleEss 是一个为 **Hytale 服务器** 设计的**管理类插件**，提供了一系列服务器常用指令和基础的玩家体验优化功能，适用于管理员和普通玩家。

> **语言支持：** English / 中文（完整中文界面与消息支持）

## 功能介绍

### TPA 传送系统

#### 玩家选择界面（GUI `/tpa`）
<img src="https://media.forgecdn.net/attachments/description/null/description_2d6e781a-ef14-4abb-92a1-163e52506f21.png" width="300" />

通过图形界面选择在线玩家并发送传送请求。

#### TPA 请求管理面板（`/tpatab`）
<img src="https://media.forgecdn.net/attachments/description/null/description_7291d06d-dbc6-4b7d-b117-a6266d913d00.png" width="300" />

集中管理收到的传送请求，可进行同意或拒绝操作。

#### TPA 设置
<img src="https://media.forgecdn.net/attachments/description/null/description_68440b7f-5d11-4137-b5b3-50fddb125004.png" width="300" />

通过独立的图形界面配置个人的 TPA 相关偏好设置。

- 基于 GUI 的 `/tpa` 传送请求系统
- 使用 `/tpatab` 管理传送请求

---

### Home 家园系统

- 支持每名玩家设置多个 Home
- 使用 `/sethome` 与 `/home` 设置和返回家园
- 使用 `/sethome --name=<homename>` 创建更多命名 Home

---

### 位置返回与死亡回溯

- `/back` 返回到上一个所在位置
- `/backdeath` 返回到**当前世界**中的上一次死亡地点

---

### 玩家通信

- 使用 `/msg` 进行玩家私聊

---

### 世界管理

- 使用 `/setworldspawn` 设置世界重生点

## 指令列表

- `/tpa`
- `/tpatab`
- `/sethome`
- `/home`
- `/back`
- `/backdeath`
- `/msg`
- `/setworldspawn`

## 配置说明

插件提供了灵活的配置文件，支持以下功能：

- 配置各指令是否默认对**普通玩家（非管理员）**开放
- 自定义插件消息前缀
- 配置玩家加入时的**个人欢迎消息**（支持 `{player}` 占位符）
- 配置玩家加入时的**全服广播消息**（支持 `{player}` 占位符）

服务器管理员可以根据自身需求，自由调整插件的权限策略和消息展示方式。
