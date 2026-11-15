# Applying changes to the official reference project

The PDF links to:
https://github.com/wagnerjfr/web-http-socket-server-task-manager-docker

Clone:

```sh
git clone https://github.com/wagnerjfr/web-http-socket-server-task-manager-docker.git
cd web-http-socket-server-task-manager-docker
```

Unzip `changes-for-original-repo.zip` into this directory, choosing overwrite for overlapping files. It provides full replacement files for the web servers, the handlers, TaskExecutor, TasksList, the new TaskPrimeCounter and the Dockerfile, plus the concurrency test. Files not replaced (original tasks and base task types) remain from the cloned original.

The design keeps the original `task.Task` and `task.TaskImpl` APIs, and maintains the 3 original task names. The original source's `TaskBitcoin` may depend on Internet access; use `TaskFiboRecursive` and `TaskBubbleSort` in offline tests.

Build:

```sh
cd src
javac WebServerSocket.java WebServerHttp.java
java WebServerSocket
```

Or use the included Dockerfile in the cloned project's root.

In a second terminal:

```sh
curl -X POST http://localhost:8000/ --data 'TaskPrimeCounter&100'
```
