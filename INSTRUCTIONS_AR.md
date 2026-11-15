# شرح التنفيذ العملي للوظيفة

المشروع الرسمي: https://github.com/wagnerjfr/web-http-socket-server-task-manager-docker

## فكرة الحل

يعتمد المخدم على `ServerSocket` لتلقي اتصالات HTTP على المنفذ 8000. بعد قبول الاتصال ينشئ كائن `RequestsHandlerSocket` من النوع `Runnable` ويسلمه لمجمع خيوط ثابت يضم 4 خيوط. يقرأ المعالج طلب GET لعرض سجل المهام، أو POST لتنفيذ مهمة محددة.

شكل الطلب: `TaskName&input`، مثل: `TaskPrimeCounter&100`.

توجد مهمة جديدة في `src/task/TaskPrimeCounter.java` لحساب الأعداد الأولية حتى العدد الذي يرسله المستخدم. يربط `TaskExecutor` اسم المهمة بالصف، ثم ينفذها ويحفظ مدتها في سجل آمن للعمليات المتزامنة (`ConcurrentLinkedQueue`).

## أوامر التشغيل

داخل مجلد المشروع:

```sh
javac -encoding UTF-8 -d out src/*.java src/handler/*.java src/singleton/*.java src/task/*.java
java -cp out WebServerSocket
```

داخل نافذة أوامر ثانية:

```sh
curl.exe -X POST http://localhost:8000/ --data "TaskPrimeCounter&100"
curl.exe -X POST http://localhost:8000/ --data "TaskFiboRecursive&35"
curl.exe http://localhost:8000/
python test_concurrent.py
```

الناتج الصحيح للمهمة الجديدة عند N=100: 25 عدداً أولياً.

## دليل التنفيذ المتوازي

عند تشغيل سكربت الاختبار ستظهر رسائل مثل:

```
[START] thread=pool-1-thread-2 task=TaskPrimeCounter input=3500000
[START] thread=pool-1-thread-3 task=TaskPrimeCounter input=2000000
[START] thread=pool-1-thread-4 task=TaskBubbleSort input=9,2,5,1,8,3
```

وجود رسائل بدء من خيوط مختلفة قبل انتهاء المهام يوضح وجود تداخل زمني في تنفيذ الطلبات.

## Docker

```sh
docker build -t thread-pool-assignment .
docker run --rm -p 8000:8000 --name thread-pool-server thread-pool-assignment
```

افتح طرفية ثانية وأعد إرسال طلب HTTP، ثم نفذ `docker logs thread-pool-server` لعرض السجل.

## المطلوب تصويره

- نجاح تنفيذ المهمة الجديدة بواسطة POST.
- سجلات START و END مع أسماء الخيوط المختلفة.
- نجاح البناء والتشغيل داخل Docker، وطلب HTTP من خارج الحاوية.

ملاحظة: تم فحص بناء Java واختبار المخدمين محلياً. بناء Docker يحتاج Docker Desktop على جهازك ولم يتم التحقق منه في بيئة الإعداد الحالية.
