# Thread Pool Assignment - runnable Java solution

This is a complete independent implementation using the class/package layout and original task names of the professor's reference repository:
https://github.com/wagnerjfr/web-http-socket-server-task-manager-docker

The new task is `TaskPrimeCounter`, which counts all prime numbers from 2 through an integer N (maximum 20,000,000).

## Requirements

- JDK 17+ for local compilation (the sources also use Java 8-compatible syntax).
- Python 3 for the optional concurrent test script.
- Docker Desktop for the Docker portion of the assignment.

## Build and run locally

From the project folder:

```sh
javac -encoding UTF-8 -d out src/*.java src/handler/*.java src/singleton/*.java src/task/*.java
java -cp out WebServerSocket
```

Alternative server (only one at a time, both use port 8000):

```sh
java -cp out WebServerHttp
```

For Windows Powershell, run `javac` with the same glob pattern, or compile from inside the `src` directory via:

```powershell
cd src
javac WebServerSocket.java WebServerHttp.java
java WebServerSocket
```

## Test

Open a second terminal:

```sh
curl -X POST http://localhost:8000/ --data 'TaskPrimeCounter&100'
curl -X POST http://localhost:8000/ --data 'TaskFiboRecursive&35'
curl -X POST http://localhost:8000/ --data 'TaskBubbleSort&7,3,1,9'
curl http://localhost:8000/
python test_concurrent.py
```

For Windows PowerShell, use `curl.exe` instead of `curl` if the latter is an alias.

Expected first result: `Prime numbers between 2 and 100: 25`.

The server console logs `[START]` and `[END]` with the worker thread name. Multiple START messages before corresponding END messages are evidence of overlapping task execution.

## Docker

```sh
docker build -t thread-pool-assignment .
docker run --rm -p 8000:8000 --name thread-pool-server thread-pool-assignment
```

In another terminal, repeat the curl commands and `python test_concurrent.py`.

To view logs:

```sh
docker logs thread-pool-server
```

## What to screenshot for the report

1. A successful `TaskPrimeCounter&100` POST response.
2. Multiple `[START]` and `[END]` entries, showing several `pool-1-thread-*` workers.
3. `docker build` successful completion, `docker run`, and an HTTP request to port 8000.

## Important

The assignment explicitly asks to clone and modify the reference repository. Use the accompanying `changes-for-original-repo.zip` overlay in the original cloned repository to preserve the upstream source history rather than presenting this independent implementation as an upstream clone. See `UPSTREAM_INSTRUCTIONS.md`.
