"""Concurrency smoke test. Uses only the Python standard library."""
from concurrent.futures import ThreadPoolExecutor, as_completed
from urllib.request import Request, urlopen
import time

URL = "http://localhost:8000/"
TASKS = [
    "TaskPrimeCounter&2000000",
    "TaskPrimeCounter&2500000",
    "TaskPrimeCounter&3000000",
    "TaskPrimeCounter&3500000",
    "TaskFiboRecursive&35",
    "TaskBubbleSort&9,2,5,1,8,3",
]


def send_task(task):
    start = time.perf_counter()
    request = Request(URL, data=task.encode("utf-8"), method="POST")
    with urlopen(request, timeout=120) as response:
        text = response.read().decode("utf-8").strip()
    return task, text, time.perf_counter() - start


if __name__ == "__main__":
    print(f"Sending {len(TASKS)} concurrent HTTP requests to {URL}")
    with ThreadPoolExecutor(max_workers=len(TASKS)) as workers:
        jobs = [workers.submit(send_task, task) for task in TASKS]
        for future in as_completed(jobs):
            task, answer, duration = future.result()
            print(f"{task}: {answer} (client time: {duration:.3f}s)")
    print("\nTask history:")
    with urlopen(URL) as response:
        print(response.read().decode("utf-8"))
