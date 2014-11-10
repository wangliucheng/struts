<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>
<!--  -->


1---------------------------------------------------------

	<!--给struts.xml配置 ,不用在程序中写死  -->
		<interceptor-ref name="login">
			<param name="namespace">/admin</param>
			<param name="actionNames">home,login</param>
			<param name="sessionName">curr_Admin</param>
		</interceptor-ref>
	<!-- 这时候就要在LoginInterceptor中修改 -->	
		private List<String> actionNameList = new ArrayList<String>();
		private String namespace;
		private String actionNames;
		private String sessionName;
		
		@Override
		public String intercept(ActionInvocation invocation) throws Exception {
			ActionProxy proxy = invocation.getProxy();
			String namespace = proxy.getNamespace();
			String actionName = proxy.getActionName();
			
			if(this.namespace.equals(namespace) && actionNameList.contains(actionName)) {
				return invocation.invoke();
			} else {
				ActionContext actionContext = invocation.getInvocationContext();
				Map<String, Object> session = actionContext.getSession();
				
				Object currAdmin = session.get(sessionName);
				if(currAdmin == null) {
					return Action.LOGIN;
				} else {
					return invocation.invoke();
				}
			}
		}

	<!--要生成他们的get和set 其中actionNames的要修改成-->
		public void setActionNames(String actionNames) {
			if(actionNames.indexOf(",") != -1) {
				String[] array = actionNames.split(",");
				for(String name : array) {
					actionNameList.add(name);
				}
			} else {
				actionNameList.add(actionNames);
			}
		}


2-------------------------------------------------------
<!-- 配置文件越来越大,导致修改不便,查看不容易-->

<!--每个模块的放在一个.xml中,例如struts-admin.xml-->

	<package name="AdminPackage" namespace="/admin" extends="basePackage">
			
	</package>

<!--struts.xml成了主配置文件,里面是一些公共的配置,还有对其他.xml的导入-->

	<struts>
	
	<!-- 更改Struts默认后缀 -->
	<constant name="struts.action.extension" value="action"/>
	<!--<constant name="struts.multipart.maxSize" value="10485760"/>是修改默认文件上传大小限制-->
	<constant name="struts.multipart.maxSize" value="10485760"/>
<!--导入其他xml配置-->	
	<include file="struts-admin.xml"/>
	<include file="struts-product.xml"/>
	<include file="struts-file.xml"/>
	<include file="struts-json.xml"/>
	
	<package name="basePackage" extends="struts-default" abstract="true">
		<interceptors>
			<interceptor name="myTimer" class="com.kaishengit.interceptor.TimerInterceptor"/>
			<interceptor name="login" class="com.kaishengit.interceptor.LoginInterceptor"/>
			<interceptor-stack name="myStack">
				<interceptor-ref name="defaultStack"/>
				<interceptor-ref name="login">
					<param name="namespace">/admin</param>
					<param name="actionNames">home,login</param>
					<param name="sessionName">curr_Admin</param>
				</interceptor-ref>
			</interceptor-stack>
		</interceptors>
		
		<default-interceptor-ref name="myStack"/>
		
		<global-results>
			<result name="login" type="redirectAction">
				<param name="actionName">home</param>
				<param name="namespace">/admin</param>
				<param name="error">1002</param>
			</result>
		</global-results>
	</package>
</struts>


3-----------------------------------------------------
<!--文件上传-->
<!--jsp中-->
<a href="/file/new.action">文件上传案例</a>
<!--fileaction中-->
@Override
	public String execute() throws Exception {
		return SUCCESS;
	}
<!--配置xml,记得在struts.xml中导入这个xml,
	<constant name="struts.multipart.maxSize" value="10485760"/>是修改默认文件上传大小限制-->
<struts>

	<package name="filePackage" extends="basePackage" namespace="/file">
		<!--没有method默认调用execute-->
		<action name="new" class="com.kaishengit.action.FileAction">
			<result>/WEB-INF/views/file/upload.jsp</result>
		</action>
		
		<action name="upload" class="com.kaishengit.action.FileAction" method="upload">
			<result type="redirectAction">
				<param name="actionName">new</param>
				<param name="namespace">/file</param>
			</result>
		</action>
		
		<action name="download" class="com.kaishengit.action.FileAction" method="download">
			<result type="stream">
				<param name="contentType">${downloadType}</param>
				<!-- 
				<param name="contentLength"></param>
				 -->
				<param name="contentDisposition">attachment;filename="${downloadName}"</param>
				<param name="inputName">downloadFile</param>
				<param name="bufferSize">2048</param>
				<param name="allowCaching">true</param>
				<param name="contentCharSet">UTF-8</param>
			</result>
		</action>
	</package>

<!--跳转到/WEB-INF/views/file/upload.jsp这个jsp中的form表单 -->
<!--action中即使没有doget和dopost方式也要写post提交,但在搜索的时候的form表单中是用get提交
	文件上传的时候必须要写enctype="multipart/form-data" 否则用strut2会报错说没有什么input视图
	要切记-->
	<form action="/file/upload.action" method="post" enctype="multipart/form-data">
		
		<input type="text" name="title">
		<input type="file" name="pic">
		<input type="file" name="pic">
		<input type="file" name="pic">
		<button>保存</button>
	</form>


<!--通过配置项来到upload方法中-->
	public String upload() {
		<!--private File pic;等
			记得生成get和set
			-->
		//System.out.println("Title:" + title);
		<!--这个时候获得的是上传缓存的名字,看不懂-->
		//System.out.println("file:" + pic.getName());
		<!--这里才是获得文件名字,是使用了约定
			private String picFileName
			表单file名字+FileName;生成get set-->
		//System.out.println("file:" + picFileName);
		<!--这里才是获得文件类型,是使用了约定
			private String picContentType
			表单file名字+ContentType;生成get set-->
		//System.out.println("type:" + picContentType);
		
		
		
		try {
			<!--单文件上传-->
			IOUtils.copy(new FileInputStream(pic), new FileOutputStream(new File("C:/upload",fileName)));
			
			<!--多文件上传时
			
				<input type="file" name="pic">
				<input type="file" name="pic">
				<input type="file" name="pic">
				用数组或者集合的形式接收
				private List<File> pic;
				private List<String> picFileName;
				private List<String> picContentType;-->
			for(int i = 0;i < pic.size();i++) {
				File file = pic.get(i);
				String fileName = picFileName.get(i);
				IOUtils.copy(new FileInputStream(file), new FileOutputStream(new File("C:/upload",fileName)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SUCCESS;
	}






4-----------------------------------------------------
------------------------------------------------------
<!-- 文件下载-->
<!--方法一:-->
<!--jsp中-->
<a href="/file/download.action">下载文件</a>
<!--Fileaction中 
	获取response-->
public String download(){
	 HttpServletResponse response = ServletActionContext.getResponse();
	 response.setContentType("application/pdf");
	 OutputStream out = response.getOutputStream();
	 InputStream in = new FileInputStream(new File("c:/xx.pdf"));
	 IOUtils.copy(in,out);
	 out.flush();
	 out.close();
	 in.close();
	 
	 return NONE;
}
<!--可以写在basicAction中,不用每次都写一遍,然后在Fileaction中传入路径即可-->

public void downloadFile(String filePath) throws Exception{
	 HttpServletResponse response = ServletActionContext.getResponse();
	 response.setContentType("application/pdf");
	 OutputStream out = response.getOutputStream();
	 InputStream in = new FileInputStream(new File(filePath));
	 IOUtils.copy(in,out);
	 out.flush();
	 out.close();
	 in.close();
	}

<!--但是我们下载的格式不是固定的,可以定义一个map集合,根据后缀名判断
	在basic中-->
	public static Map<String,String> mimeType = new HashMap<String, String>();
	static {
		mimeType.put(".pdf", "application/pdf");
		mimeType.put(".json", "application/json");
		mimeType.put(".doc","application/msword");
		mimeType.put(".jpg","image/jpeg");
		mimeType.put(".jpeg","image/jpeg");
		mimeType.put(".jpe","image/jpeg");
		mimeType.put(".xls","application/vnd.ms-excel");
		mimeType.put(".zip","application/zip");
		mimeType.put(".wps","application/vnd.ms-works");
		mimeType.put(".gif","image/gif");
		mimeType.put(".mp3","audio/mpeg");
		//application/octet-stream
	}

	public void downloadFile(String filePath,String downloadFileName) throws Exception{
		HttpServletResponse response = getResponse();
		
		
		downloadFileName = new String(downloadFileName.getBytes("UTF-8"),"ISO8859-1");
		response.addHeader("contentDisposition", "attachment;filename=\""+downloadFileName+"\"");
		
		String fileType = filePath.substring(filePath.lastIndexOf("."));
		if(mimeType.containsKey(fileType)) {
			response.setContentType(mimeType.get(fileType));
		} else {
		<!--没有的话就认为是2进制-->
			response.setContentType("application/octet-stream");
		}
		
		OutputStream out = response.getOutputStream();
		InputStream in = new FileInputStream(new File(filePath));
		
		IOUtils.copy(in, out);
		out.flush();
		out.close();
		in.close();
	}
	

<!--方法二:-->
<!--Fileaction中-->
	public String download() throws Exception {
		downloadType = "application/pdf";
		downloadName = new String("缓存.pdf".getBytes("UTF-8"),"ISO8859-1");
		<!--return了一个success.需要在xml中配置一个返回值是stream-->
		return SUCCESS;
	}
	
	public InputStream getDownloadFile() throws Exception {
		return new FileInputStream("C:/upload/065-cache.pdf");
	}
	
<!--struts-file.xml中-->
		<action name="download" class="com.kaishengit.action.FileAction" method="download">
			<result type="stream">
				<param name="contentType">${downloadType}</param>
				<!-- 进度条
				<param name="contentLength"></param>
				 -->
				<!--下载框--><param name="contentDisposition">attachment;filename="${downloadName}"</param>
				<!--给一个输入流,需要在FileAction中配置一个方法获取输入流,返回值是InputStream
				方法名为get开头,param里面的参数为去掉get后首字母小写的结果-->
				<param name="inputName">downloadFile</param>
				<param name="bufferSize">2048</param>
				<param name="allowCaching">true</param>
				<param name="contentCharSet">UTF-8</param>
			</result>
		</action>





5-----------------------------------------------
-----------------------------------------------------
<!--方法1:使用常用的返回方式-->
<!--返回json,首先在pom里添加对gson的导入-->
public class JSONAction extends BasicAction{

	private static final long serialVersionUID = 1L;
	@Override
	public String execute() throws Exception {
		
		<!--在basic中提供renderJSON
	public void renderJSON(Object obj) throws Exception {
		HttpServletResponse response = getResponse();
		response.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
	
		-->
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "success");
		map.put("result", "OK");
		
		renderJSON(map);
		return NONE;
	}


	
} 

<!--struts-json中-->
	<package name="jsonPackage" extends="basePackage,json-default" namespace="/json">
		<action name="product" class="com.kaishengit.action.JSONAction">
			
		</action>
	</package>





<!--方法2:使用插件返回
	导入struts-json-plugin,必须和struts-core的版本一致-->


<!--返回json,首先在pom里添加对gson的导入-->
public class JSONAction extends BasicAction{

	private static final long serialVersionUID = 1L;

	private Map<String, Object> map;
	
	@Override
	public String execute() throws Exception {
		
		<!--在basic中提供renderJSON
	public void renderJSON(Object obj) throws Exception {
		HttpServletResponse response = getResponse();
		response.setContentType("application/json;charset=UTF-8");
		
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}
	
		-->
		
		map = new HashMap<String, Object>();
		map.put("id", 1001);
		map.put("name", "Jerry");
		map.put("address", "中国河南省");
		
		return SUCCESS;
	}

	//get set

	public Map<String, Object> getMap() {
		return map;
	}
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
} 

<!--struts-json中
extends="basePackage,json-default"多继承-->
	<package name="jsonPackage" extends="basePackage,json-default" namespace="/json">
		<action name="product" class="com.kaishengit.action.JSONAction">
		<!--插件中定义了新的返回值json-->
			<result name="success" type="json">
			<!--root默认是一个action,会把里面所有的属性都转换成json--
			修改成map后只转化里面的map-->
				<param name="root">map</param>
				<param name="enableGZIP">true</param>压缩,传输快
				<param name="noCache">true</param>不缓存
				<param name="excludeNullProperties">true</param>排除没有值的属性,不生成json
				<param name="contentType">application/JSON</param>
				<param name="encoding">UTF-8</param>
			</result>
		</action>
	</package>

















	

</body>
</html>	
	
