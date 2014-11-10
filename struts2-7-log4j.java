<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>
<!-- 在开发中我们需要打印出来一些来判断程序的运行的
	判断用户的使用情况,比如他说他账户订单被无故取消了,我们就可以查看日志判断是谁取消的-->
		
		<!-- 1.添加对jar包的依赖.log4j -->
		<!-- 2.需要配置文件 log4j.properties -->
<!-- 方式1 -->
		<!-- consoleOut这个名字是随意起的,后面的ConsoleAppender表示是往控制台上输出-->
		log4j.appender.consoleOut=org.apache.log4j.ConsoleAppender
		<!-- layout指的是输出的格式,这里采用的是按照表达式 -->
		log4j.appender.consoleOut.layout=org.apache.log4j.PatternLayout
		<!-- 表达式内容
			%m 输出代码中指定的消息
			%p 输出优先级，即DEBUG，INFO，WARN，ERROR，FATAL
			%r 输出自应用启动到输出该log信息耗费的毫秒数
			%c 输出所属的类目，通常就是所在类的全名
			%t 输出产生该日志事件的线程名
			%n 输出一个回车换行符，Windows平台为“/r/n”，Unix平台为“/n”
			%d 输出日志时间点的日期或时间，默认格式为ISO8601，也可以在其后指定格式，比如：%d{yyy MMM dd HH:mm:ss , SSS}
			%l 输出日志事件的发生位置，包括类目名、发生的线程，以及在代码中的行数。-->
		log4j.appender.consoleOut.layout.ConversionPattern=[%p](%d)---%l--%m\r\n
		<!-- 指定输出的起始级别,和名字 -->
		log4j.rootLogger=INFO,consoleOut
		
		
		
		
<!-- 方式2 -->		
<!-- fout这个名字是随意起的,后面的FileAppender表示是往文件上输出-->
		log4j.appender.fout=org.apache.log4j.FileAppender
		log4j.appender.fout.layout=org.apache.log4j.PatternLayout
		log4j.appender.fout.layout.ConversionPattern=[%p](%d)---%l--%m\r\n
		<!-- 指定生成文件型日志的级别 -->
		log4j.appender.fout.Threshold=INFO
		<!-- 指定输出的文件地址 -->
		log4j.appender.fout.file=C:/struts.log
		#log4j.rootLogger=DEBUG,fout
		
 <!-- 方式3 -->
		#log4j.appender.fout=org.apache.log4j.DailyRollingFileAppender
		#log4j.appender.fout.layout=org.apache.log4j.PatternLayout
		#log4j.appender.fout.layout.ConversionPattern=[%p](%d)---%l--%m\r\n
		<!-- 指定生成文件名的格式,后面加上.具体时间 -->
		#log4j.appender.fout.DatePattern='.'yyyy-MM-dd
		<!-- 指定输出的文件地址 -->
		#log4j.appender.fout.file=C:/struts.log
		#log4j.rootLogger=DEBUG,fout
		
		
		
		
		
		
		
		
		
		<!-- 3.在需要使用log4j的action中创建出他的对象,并传入对象的class -->
		private Logger log = Logger.getLogger(HomeAction.class);
		
		
			@Override
		public String execute() {
			
			log.debug("debug message");
			log.info("info message");
			log.warn("warn message");
			log.error("error message");
			log.fatal("fatal message");
			
			return SUCCESS;
		}
</body>
</html>
