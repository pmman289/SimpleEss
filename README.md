# SimpleEss

SimpleEss is a **management plugin for Hytale servers**, providing commonly used player commands and essential quality-of-life features with a clean permission system for both administrators and regular players.

> **Language Support:** English / 中文（完整中文界面与消息支持）

---

## Features

### TPA System

#### Player Selector (GUI `/tpa`)

![](https://media.forgecdn.net/attachments/description/null/description_2d6e781a-ef14-4abb-92a1-163e52506f21.png)

Select an online player via GUI to send a teleport request.

#### TPA Request Management (`/tpatab`)

![](https://media.forgecdn.net/attachments/description/null/description_7291d06d-dbc6-4b7d-b117-a6266d913d00.png)

Manage incoming teleport requests, including accept and deny actions.

#### TPA Settings

![](https://media.forgecdn.net/attachments/description/null/description_68440b7f-5d11-4137-b5b3-50fddb125004.png)

Configure personal TPA-related preferences through a dedicated GUI.

* GUI-based `/tpa` request interface
* `/tpatab` for managing incoming teleport requests
* Configurable cooldown with permission-based bypass support

---

### Home System

* Multiple home support per player
* Set and teleport to homes using `/sethome` and `/home`
* Home names can be entered **directly** without optional parameter syntax
* Permission-based bypass for maximum home limits

---

### Random Teleport

* Random teleport using `/rtp`
* Default cooldown: **60 seconds**
* Default teleport range: a **1000 × 1000 rectangular area** centered on the world spawn point
* Cooldown can be bypassed via permission

---

### Teleport & Recovery

* `/back` to return to the previous location
* `/backdeath` to return to the last death location in the current world

---

### Flight

* Toggle flight mode using `/fly`
* Permission-controlled access

---

### Communication

* Private messaging with `/msg`

---

### World Management

* Set world respawn point using `/setworldspawn`

---

## Commands

Most commonly used player commands are **automatically granted to the Default permission group by default**, allowing players to use them without additional permission configuration.

Command access behavior can be further controlled via the `EnablePublicCommandControl` and `PublicCommand` settings in the configuration file.

### Command & Permission Reference

| Command | Permission Node | Default Access |
| ------ | --------------- | -------------- |
| `/tpa` | `simpleess.command.tpa` | Enabled |
| `/tpatab` | `simpleess.command.tpatab` | Enabled |
| `/rtp` | `simpleess.command.rtp` | Enabled |
| `/sethome` | `simpleess.command.sethome` | Enabled |
| `/home` | `simpleess.command.home` | Enabled |
| `/back` | `simpleess.command.back` | Enabled |
| `/backdeath` | `simpleess.command.backdeath` | Enabled |
| `/fly` | `simpleess.command.fly` | Operator only |
| `/msg` | `simpleess.command.msg` | Enabled |
| `/setworldspawn` | `simpleess.command.setworldspawn` | Operator only |
| `/simpleess reload` | `simpleess.command.simpleess.reload` | Operator only |

### Additional Permission Nodes

| Function | Permission |
| ------- | ---------- |
| Bypass TPA cooldown | `simpleess.command.tpa.cooldown.bypass` |
| Bypass RTP cooldown | `simpleess.command.rtpa.cooldown.bypass` |
| Bypass home limit | `simpleess.command.sethome.limit.bypass` |

> Note:
>
> * When `EnablePublicCommandControl` is set to `false` (default), most commonly used commands are automatically granted to the Default permission group.
> * When `EnablePublicCommandControl` is set to `true`, only commands listed in `PublicCommand` will be granted to the Default permission group.
> * Operators (OP) always have access to all commands.

---

## Configuration

The plugin provides a flexible configuration file with the following options:

* Automatically add new players to the **Default permission group** (`AutoAddPlayerToDefault`)
* Enable or disable each command for **default (non-operator) players**
* Control public command access via `EnablePublicCommandControl` and `PublicCommand`
* Customize the **plugin message prefix**
* Configure **player join welcome messages** (supports `{player}` as a placeholder)
* Configure **player join broadcast messages** (supports `{player}` as a placeholder)

---

# SimpleEss

SimpleEss 是一个 **Hytale 服务器管理插件**，为管理员与普通玩家提供常用指令与基础 QoL 功能，并配备清晰、可控的权限体系。

---

## 功能特性

### TPA 传送系统

#### 玩家选择界面（GUI `/tpa`）

通过 GUI 选择在线玩家并发送传送请求。

#### TPA 请求管理（`/tpatab`）

用于管理收到的传送请求，可执行同意或拒绝操作。

#### TPA 设置

通过独立 GUI 配置个人 TPA 相关偏好。

* 基于 GUI 的 `/tpa` 请求界面
* 使用 `/tpatab` 管理传送请求
* 支持冷却时间配置与权限绕过

---

### 家系统

* 每位玩家支持多个家
* 使用 `/sethome` 与 `/home` 设置和传送
* 家名称参数可直接输入，无需可选参数格式
* 支持通过权限绕过最大家数量限制

---

### 随机传送

* 使用 `/rtp` 进行随机传送
* 默认冷却时间：**60 秒**
* 默认范围：以世界出生点为中心的 **1000 × 1000 矩形区域**
* 支持通过权限绕过冷却时间

---

### 传送与回溯

* `/back` 返回上一个位置
* `/backdeath` 返回当前世界的上一次死亡地点

---

### 飞行

* 使用 `/fly` 切换飞行模式
* 受权限系统控制

---

### 通信

* 使用 `/msg` 进行私聊

---

### 世界管理

* 使用 `/setworldspawn` 设置世界出生点

---

## 配置说明

插件提供灵活的配置文件，支持以下内容：

* 自动将新玩家加入 **Default 权限组**（`AutoAddPlayerToDefault`）
* 控制普通玩家可用的指令
* 通过 `EnablePublicCommandControl` 与 `PublicCommand` 精细化管理指令开放策略
* 自定义插件消息前缀
* 配置玩家加入欢迎消息与广播消息（支持 `{player}` 占位符）
