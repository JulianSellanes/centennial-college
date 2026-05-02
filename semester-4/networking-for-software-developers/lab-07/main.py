from __future__ import annotations
import argparse
import random
import threading
import time
from pathlib import Path
from typing import Callable, Generator, Iterable

# Julian Sellanes (301494667)

# Configuration defaults
DEFAULT_NUM_FILES = 25
DEFAULT_LINES_PER_FILE = 2000
DEFAULT_DATA_DIR = "data"
SLEEP_SECONDS = 0.002

# Timing decorator
def timed(label: str | None = None) -> Callable:
    # Decorator that prints elapsed time (ms) for the wrapped function.
    def decorator(func: Callable) -> Callable:
        def wrapper(*args, **kwargs):
            start = time.perf_counter()
            result = func(*args, **kwargs)
            end = time.perf_counter()
            elapsed_ms = (end - start) * 1000
            print(f"{label or func.__name__}: {elapsed_ms:.2f} ms")
            return result
        return wrapper
    return decorator

# Filename generator
def filename_generator(folder: Path, num_files: int) -> Iterable[Path]:
    # Generator expression that yields: folder/temp01.txt, folder/temp02.txt, ..., without storing filenames in a list.
    width = max(2, len(str(num_files)))
    return (folder / f"temp{i:0{width}d}.txt" for i in range(1, num_files + 1))

# Creating data files
def create_one_file(filepath: Path, lines_per_file: int) -> None:
    # Create ONE file containing random 3-digit integers, one per line.
    with filepath.open("w", encoding="utf-8") as f:
        for _ in range(lines_per_file):
            f.write(f"{random.randint(100, 999)}\n")

@timed("create")
def create_all_files(num_files: int, lines_per_file: int, folder: Path) -> None:
    # Create ALL required files in the target folder.
    folder.mkdir(parents=True, exist_ok=True)

    for fp in filename_generator(folder, num_files):
        create_one_file(fp, lines_per_file)

    print(f"Created {num_files} files in '{folder.as_posix()}'")

# Processing files
def process_one_file(filepath: Path) -> int:
    #Read ONE file and return the sum of its integers.
    total = 0
    with filepath.open("r", encoding="utf-8") as f:
        for line in f:
            total += int(line.strip())

    time.sleep(SLEEP_SECONDS)
    return total


@timed("serial")
def process_serial(num_files: int, folder: Path) -> int:
    #Process files one-by-one (sequential).
    total = 0
    for fp in filename_generator(folder, num_files):
        if not fp.exists():
            raise FileNotFoundError(
                f"Missing file: {fp}. Run create mode first (or check folder/--files)."
            )
        total += process_one_file(fp)

    print(f"Serial total: {total}")
    return total


@timed("threaded")
def process_threaded(num_files: int, folder: Path) -> int:
    #Process files using one thread per file.
    total = 0
    total_lock = threading.Lock()
    errors: list[BaseException] = []
    threads: list[threading.Thread] = []

    def worker(fp: Path) -> None:
        nonlocal total
        try:
            if not fp.exists():
                raise FileNotFoundError(
                    f"Missing file: {fp}. Run create mode first (or check folder/--files)."
                )
            subtotal = process_one_file(fp)
            with total_lock:
                total += subtotal
        except BaseException as e:
            with total_lock:
                errors.append(e)

    for fp in filename_generator(folder, num_files):
        t = threading.Thread(target=worker, args=(fp,), daemon=False)
        threads.append(t)
        t.start()

    for t in threads:
        t.join()

    if errors:
        raise errors[0]

    print(f"Threaded total: {total}")
    return total

# Command-line interface
def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Create and process many text files (serial vs threaded)."
    )

    # Mode
    parser.add_argument(
        "mode",
        choices=["create", "serial", "threaded"],
        help="Execution mode: create, serial, or threaded",
    )

    parser.add_argument(
        "-n", "--files",
        type=int,
        default=DEFAULT_NUM_FILES,
        help=f"Number of files (default: {DEFAULT_NUM_FILES})",
    )

    parser.add_argument(
        "-l", "--lines",
        type=int,
        default=DEFAULT_LINES_PER_FILE,
        help=f"Lines per file (only used in create mode; default: {DEFAULT_LINES_PER_FILE})",
    )

    parser.add_argument(
        "-d", "--dir",
        type=str,
        default=DEFAULT_DATA_DIR,
        help=f"Folder for data files (default: {DEFAULT_DATA_DIR})",
    )

    return parser.parse_args()

def main() -> None:
    args = parse_args()
    folder = Path(args.dir)

    if args.files <= 0:
        raise ValueError("--files must be > 0")

    if args.mode == "create":
        if args.lines <= 0:
            raise ValueError("--lines must be > 0 in create mode")
        create_all_files(args.files, args.lines, folder)

    elif args.mode == "serial":
        process_serial(args.files, folder)

    elif args.mode == "threaded":
        process_threaded(args.files, folder)

if __name__ == "__main__":
    main()