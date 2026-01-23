# SimpleEss

SimpleEss is a **GUI-oriented management plugin for Hytale servers**, designed to provide commonly used server commands through **intuitive visual interfaces**, reducing command complexity while maintaining a clean and flexible permission system for both administrators and regular players.

> **Language Support:** English / 中文（完整中文界面与消息支持）

***

## Features

### Kit System (GUI-based)

#### Visual Kit Selection (`/kit`)

![image](https://media.forgecdn.net/attachments/description/1437364/description_6d6435c2-41a7-47d7-acb1-a144f3cd2103.png)

Open a **visual kit selection interface** using `/kit`, allowing players to browse and claim available kits without memorizing commands.

* Fully GUI-based kit claiming experience
* Kits are easy to configure via configuration files
* Each kit supports **individual permission checks**
* Kits can also be restricted by **permission groups**
* Clear and user-friendly feedback messages

***

### TPA System

#### Player Selector (GUI `/tpa`)

![](https://media.forgecdn.net/attachments/description/null/description_2d6e781a-ef14-4abb-92a1-163e52506f21.png)

Select an online player via GUI to send a teleport request.

#### TPA Request Management (GUI `/tpatab`)

![](https://media.forgecdn.net/attachments/description/null/description_7291d06d-dbc6-4b7d-b117-a6266d913d00.png)

Manage incoming teleport requests, including accept and deny actions.

#### TPA Settings

![](https://media.forgecdn.net/attachments/description/null/description_68440b7f-5d11-4137-b5b3-50fddb125004.png)

Configure personal TPA-related preferences through a dedicated GUI.

* GUI-based `/tpa` request interface
* GUI-based request management via `/tpatab`
* Configurable cooldown with permission-based bypass support

***

### Home System

* Multiple home support per player
* Set and teleport to homes using `/sethome` and `/home`
* Home names can be entered **directly** without optional parameter syntax
* Permission-based bypass for maximum home limits

***

### Random Teleport

* Random teleport using `/rtp`
* Default cooldown: **60 seconds**
* Default teleport range: a **1000 × 1000 rectangular area** centered on the world spawn point
* Cooldown can be bypassed via permission

***

### Teleport & Recovery

* `/back` to return to the previous location
* `/backdeath` to return to the last death location in the current world

***

### Flight

* Toggle flight mode using `/fly`
* No fall damage while flying
* Permission-controlled access

***

### Communication

* Private messaging with `/msg`

***

### World Management

* Set world respawn point using `/setworldspawn`

***

## Commands & Permissions

Most commonly used player commands are **automatically granted to the Default permission group by default**, allowing players to use them without additional permission configuration.

Command access behavior can be further controlled via the `EnablePublicCommandControl` and `PublicCommand` settings.

---

# SimpleEss（中文说明）

SimpleEss 是一个 **以 GUI 可视化交互为核心的 Hytale 服务器管理插件**，通过直观的界面减少指令记忆成本，同时为管理员提供清晰、灵活的权限控制体系，适合生存服与综合服务器使用。

***

## 功能特性

### Kit 礼包系统（GUI）

#### 可视化 Kit 领取界面（`/kit`）

<!-- Kit GUI 图片放置位置 -->
<!-- ![](KIT_GUI_IMAGE_URL) -->

使用 `/kit` 打开 **可视化 Kit 领取界面**，玩家可以直观浏览并领取可用礼包，无需记忆复杂指令。

* 完全基于 GUI 的礼包领取体验
* Kit 可在配置文件中方便地进行配置
* 支持为每个 Kit 设置独立的权限节点
* 支持按权限组控制 Kit 的可领取范围
* 提供清晰友好的领取提示信息

***

### TPA 传送系统

#### 玩家选择界面（GUI `/tpa`）

通过 GUI 选择在线玩家并发送传送请求。

#### 传送请求管理（GUI `/tpatab`）

通过可视化界面管理收到的传送请求，可执行同意或拒绝操作。

#### TPA 设置界面

通过独立 GUI 配置个人 TPA 相关偏好。

* 基于 GUI 的 `/tpa` 请求界面
* 使用 GUI 管理传送请求
* 支持冷却时间与权限绕过配置

***

### 家系统

* 每位玩家支持多个家
* 使用 `/sethome` 与 `/home` 设置和传送
* 家名称参数可直接输入，无需可选参数格式
* 支持通过权限绕过最大家数量限制

***

### 随机传送

* 使用 `/rtp` 进行随机传送
* 默认冷却时间：**60 秒**
* 默认范围：以世界出生点为中心的 **1000 × 1000 矩形区域**
* 支持通过权限绕过冷却时间

***

### 传送与回溯

* `/back` 返回上一个位置
* `/backdeath` 返回当前世界的上一次死亡地点

***

### 飞行

* 使用 `/fly` 切换飞行模式
* 飞行状态下不会受到摔落伤害
* 受权限系统控制

***

### 通信

* 使用 `/msg` 进行私聊

***

### 世界管理

* 使用 `/setworldspawn` 设置世界出生点

***

## 配置说明

插件提供灵活的配置文件，支持以下内容：

* 自动将新玩家加入 **Default 权限组**（`AutoAddPlayerToDefault`）
* 控制普通玩家可用的指令
* 通过 `EnablePublicCommandControl` 与 `PublicCommand` 精细化管理指令开放策略
* 自定义插件消息前缀
* 配置玩家加入欢迎消息与广播消息（支持 `{player}` 占位符）  
