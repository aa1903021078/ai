#!/usr/bin/env python3
"""校验结果 JSON，并将其以单行格式追加写入指定文件。"""

import argparse
import json
import sys
from pathlib import Path

REQUIRED_KEYS = {"code", "message", "content"}
DEFAULT_OUTPUT = Path("/Users/weibaiwang/Desktop/ai.txt")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="追加 TripPlannerAgent 的 JSON 结果")
    parser.add_argument(
        "input",
        type=Path,
        nargs="?",
        default=None,
        help="输入 JSON 文件路径；省略时从标准输入读取",
    )
    parser.add_argument(
        "--output",
        type=Path,
        default=DEFAULT_OUTPUT,
        help=f"输出文件，默认：{DEFAULT_OUTPUT}",
    )
    return parser.parse_args()


def load_payload(input_path) -> object:
    if input_path is None:
        return json.load(sys.stdin)
    with input_path.open("r", encoding="utf-8") as file:
        return json.load(file)


def validate(payload: object) -> dict:
    if not isinstance(payload, dict):
        raise ValueError("输入必须是 JSON 对象")
    if set(payload) != REQUIRED_KEYS:
        raise ValueError("JSON 必须且只能包含 code、message、content")
    if type(payload["code"]) is not int:
        raise TypeError("code 必须是整数")
    if not isinstance(payload["message"], str):
        raise TypeError("message 必须是字符串")
    if not isinstance(payload["content"], str):
        raise TypeError("content 必须是字符串")
    return payload


def append_and_verify(payload: dict, output: Path) -> str:
    line = json.dumps(payload, ensure_ascii=False, separators=(",", ":"))
    encoded = (line + "\n").encode("utf-8")

    with output.open("ab") as file:
        offset = file.tell()
        file.write(encoded)
        file.flush()

    with output.open("rb") as file:
        file.seek(offset)
        written = file.read(len(encoded))

    if written != encoded or json.loads(written.decode("utf-8")) != payload:
        raise RuntimeError("追加写入后的 JSON 校验失败")
    return line


def main() -> int:
    try:
        args = parse_args()
        payload = validate(load_payload(args.input))
        print(append_and_verify(payload, args.output))
        return 0
    except Exception as exc:
        print(f"写入失败：{exc}", file=sys.stderr)
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
