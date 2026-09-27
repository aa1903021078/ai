---
name: write
description: 将 TripPlannerAgent 的处理结果封装为固定 JSON，并把完整 JSON 逐行追加写入 /Users/weibaiwang/Desktop/ai.txt。回答行程规划、生成脚本或报告执行结果时使用。
---

# 结果封装与追加写入

## 输出协议

每次完成任务后，只返回一个合法 JSON 对象，不要使用 Markdown 代码块，不要在 JSON 前后添加解释。

JSON 必须严格包含以下三个属性，不得遗漏或增加属性：

```json
{
  "code": 1,
  "message": "成功",
  "content": "具体回答内容；需要脚本时在此字符串中包含完整脚本"
}
```

遵守以下约束：

- `code` 必须是整数。任务完整成功时使用 `1`；任务失败或仅部分完成时使用其他整数，默认使用 `0`。
- `message` 必须是字符串。成功时简要说明成功；失败时明确说明失败阶段和原因。
- `content` 必须是字符串，填写用户需要的完整回答。脚本、命令、配置或代码必须完整写入该字符串，使用 JSON 转义表示换行、双引号和反斜杠。
- 不得返回伪 JSON、尾随逗号、注释、`NaN`、Markdown 外层代码围栏或 JSON 之外的文本。
- 不得把任务失败伪装成成功。

## 执行流程

以下每一步都是你必须实际调用工具去执行的动作，而不是写在回答里的说明。未成功执行第 4 步的追加命令之前，任务不算完成，禁止只把 JSON 直接返回给用户。

1. 完成用户任务并确定真实执行结果。
2. 构造只含 `code`、`message`、`content` 的结果对象。
3. 调用文件写入工具，将合法 JSON 写入临时文件 `/Users/weibaiwang/Desktop/.trip-planner-result.json`。
4. 调用 Shell 工具执行以下命令（用位置参数传入临时文件，不要使用重定向符）：

```bash
python3 "/Users/weibaiwang/Desktop/AAA/work/ai-demo/tripPlanner_agent/src/main/resources/skills/write/scripts/append_json.py" "/Users/weibaiwang/Desktop/.trip-planner-result.json"
```

5. 确认 Shell 命令返回码为 0；只有此时才算写入成功。命令成功后删除临时文件，且仅向用户返回脚本标准输出中的 JSON；该输出就是已成功追加到 `/Users/weibaiwang/Desktop/ai.txt` 的内容。
6. 禁止直接覆盖或清空 `/Users/weibaiwang/Desktop/ai.txt`。

## 脚本资源

本技能有且仅有一个可执行脚本：`scripts/append_json.py`。禁止加载、引用或调用任何其它文件名（本技能不存在任何 `.sh` 脚本，例如 `scripts/write_result.sh` 并不存在）。所有追加写入必须且只能通过运行 `scripts/append_json.py` 完成。

该脚本负责字段类型校验、单行 JSON 序列化、追加写入及写后校验。不要复制或改写该脚本逻辑。

## 异常处理

如果业务任务失败，仍按上述流程追加失败 JSON，例如：

```json
{"code":0,"message":"失败：无法获取目的地信息","content":"未生成行程，请检查目的地或网络配置。"}
```

如果首次写入失败，构造 `code` 为 `0` 的 JSON，`message` 写明文件写入失败及原因，并再尝试追加一次。若因权限、磁盘或路径问题仍无法写入，不得声称写入成功；直接返回该失败 JSON。