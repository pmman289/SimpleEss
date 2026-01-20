# SimpleEss

A **management plugin for Hytale servers**, providing commonly used player commands and essential quality-of-life features with flexible permission control.

一个用于 **Hytale 服务器的管理插件**，提供常用玩家指令与基础 QoL 功能，并支持灵活的权限控制。

> **Language Support / 语言支持**  
> English / 中文（完整中文界面与消息支持）

---

## Features / 功能特性

### TPA System / TPA 传送系统

#### Player Selector (GUI `/tpa`) / 玩家选择界面（GUI `/tpa`）

![](https://media.forgecdn.net/attachments/description/null/description_2d6e781a-ef14-4abb-92a1-163e52506f21.png)

Select an online player via GUI to send a teleport request.  
通过 GUI 选择在线玩家并发送传送请求。

#### TPA Request Management (`/tpatab`) / TPA 请求管理（`/tpatab`）

![](https://media.forgecdn.net/attachments/description/null/description_7291d06d-dbc6-4b7d-b117-a6266d913d00.png)

Manage incoming teleport requests, including accept and deny actions.  
管理收到的传送请求，可进行同意或拒绝操作。

#### TPA Settings / TPA 设置

![](https://media.forgecdn.net/attachments/description/null/description_68440b7f-5d11-4137-b5b3-50fddb125004.png)

Configure personal TPA-related preferences through a dedicated GUI.  
通过独立 GUI 配置个人 TPA 相关偏好。

* GUI-based `/tpa` request interface
* `/tpatab` for managing incoming teleport requests
* Configurable cooldown with permission-based bypass support

* 基于 GUI 的 `/tpa` 请求界面
* 使用 `/tpatab` 管理传送请求
* 支持冷却时间配置，并可通过权限绕过

---

### Home System / 家系统

* Multiple home support per player
* Set and teleport to homes using `/sethome` and `/home`
* Create additional homes using `/sethome --name=<homename>`
* Permission-based bypass for maximum home limits

* 每位玩家支持多个家
* 使用 `/sethome` 和 `/home` 设置与传送至家
* 通过 `/sethome --name=<homename>` 创建额外的家
* 支持通过权限绕过最大家数量限制

---

### Random Teleport / 随机传送

* Random teleport using `/rtp`
* Default cooldown: **60 seconds**
* Default range: **1000 × 1000 rectangle centered on world spawn**
* Cooldown can be bypassed via permission

* 使用 `/rtp` 进行随机传送
* 默认冷却时间：**60 秒**
* 默认范围：**以世界出生点为中心的 1000 × 1000 矩形区域**
* 可通过权限绕过冷却时间

---

### Teleport & Recovery / 传送与回溯

* `/back` to return to the previous location
* `/backdeath` to return to the last death location in the current world

* `/back` 返回上一个位置
* `/backdeath` 返回当前世界的上一次死亡地点

---

### Communication / 通信

* Private messaging with `/msg`
* 使用 `/msg` 进行私聊

---

### World Management / 世界管理

* Set world respawn point using `/setworldspawn`
* 使用 `/setworldspawn` 设置世界出生点

---

## Commands / 指令说明

Most commonly used player commands are **automatically granted to the Default permission group by default**, allowing players to use them without additional configuration.

大多数玩家常用指令 **默认自动开放给 Default 权限组**，无需额外配置即可使用。

Command access behavior can be further controlled via the `EnablePublicCommandControl` and `PublicCommand` settings in the configuration file.

指令开放行为可通过配置项 `EnablePublicCommandControl` 与 `PublicCommand` 进一步控制。

### Command & Permission Reference / 指令与权限对照表

| Command | Permission Node | Default Access |
| ------ | --------------- | -------------- |
| `/tpa` | `simpleess.command.tpa` | Enabled |
| `/tpatab` | `simpleess.command.tpatab` | Enabled |
| `/rtp` | `simpleess.command.rtp` | Enabled |
| `/sethome` | `simpleess.command.sethome` | Enabled |
| `/home` | `simpleess.command.home` | Enabled |
| `/back` | `simpleess.command.back` | Enabled |
| `/backdeath` | `simpleess.command.backdeath` | Enabled |
| `/msg` | `simpleess.command.msg` | Enabled |
| `/setworldspawn` | `simpleess.command.setworldspawn` | Operator only |
| `/simpleess reload` | `simpleess.command.simpleess.reload` | Operator only |

### Additional Permission Nodes / 额外权限节点

| Function | Permission |
| ------- | ---------- |
| Bypass TPA cooldown | `simpleess.command.tpa.cooldown.bypass` |
| Bypass RTP cooldown | `simpleess.command.rtpa.cooldown.bypass` |
| Bypass home limit | `simpleess.command.sethome.limit.bypass` |

> Note / 说明：
>
> * When `EnablePublicCommandControl` is `false` (default), most commonly used commands are automatically granted to the Default group.
> * 当 `EnablePublicCommandControl` 为 `false`（默认）时，大多数常用指令会自动开放给 Default 权限组。
>
> * When `EnablePublicCommandControl` is `true`, only commands listed in `PublicCommand` will be granted.
> * 当其为 `true` 时，仅 `PublicCommand` 中列出的指令会被开放。
>
> * Operators (OP) always have access to all commands.
> * OP 永远拥有所有指令权限。

---

## Configuration / 配置说明

The plugin provides a flexible configuration file with the following options:

插件提供灵活的配置文件，支持以下内容：

* Automatically add new players to the **Default permission group** (`AutoAddPlayerToDefault`)
* 自动将新玩家加入 **Default 权限组**（`AutoAddPlayerToDefault`）

* Enable or disable each command for **default (non-operator) players**
* 控制普通玩家是否可使用各指令

* Control public command access via `EnablePublicCommandControl` and `PublicCommand`
* 通过 `EnablePublicCommandControl` 与 `PublicCommand` 控制指令公开策略

* Customize the **plugin message prefix**
* 自定义插件消息前缀

* Configure **player join welcome messages** (supports `{player}` placeholder)
* 配置玩家加入欢迎消息（支持 `{player}` 占位符）

* Configure **player join broadcast messages** (supports `{player}` placeholder)
* 配置玩家加入广播消息（支持 `{player}` 占位符）
