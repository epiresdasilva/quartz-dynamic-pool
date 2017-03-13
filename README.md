# Dynamic Pool Size for Quartz

Implementation using Executor Service to manage quartz worker threads and his pool size of execution.

Provide the interface ``DynamicThreadPool`` to do the resizing of the thread pool size.

## Requirements

* Maven 3.3.x
* Java 7+

## How to use

1) Clone the project:

```
git clone https://github.com/epiresdasilva/quartz-dynamic-pool.git
```

2) Install in your local maven repository:

```
mvn clean install
```

3) Add the dependency to your project:

```
<dependency>
	<groupId>br.com.evandropires</groupId>
	<artifactId>quartz-dynamic-pool</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

4) Add the thread pool class and thread pool count property in your Quartz configuration:

```
org.quartz.threadPool.class=br.com.evandropires.quartz.impl.ExecutorServiceThreadPool
org.quartz.threadPool.threadCount=10
```

or

```
properties.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS,
	    	ExecutorServiceThreadPool.class.getName());
properties.setProperty("org.quartz.threadPool.threadCount",
    		"10");
```

Done!