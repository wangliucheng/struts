<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>
<!-- 导入这个插件,该插件的作用是替换掉struts.xml 原则是没有配置,全是约定-->
	<div>
	<!-- 基本步骤 -->
	<!-- 1.新建HomeAction,里面有个execute方法return success,请求home.action就这样就能执行这个方法 -->
		<!-- 约定:home就是以前配置中的action的name属性值,没有方法名就默认execute
				HomeAction→home.action
				StudentHomeAction→student-home.action-->
				
		<!-- 包名,请求的action必须放在action,actions,strut,struts中
				com.kaishengit.action.HomeAction → /home.action
				com.kaishengit.action.product.HomeAction → /product/home.action
				com.kaishengit.action.product.type.HomeAction → /product/type/home.action-->
				
				
				
	<!-- 2.添加jsp文件.文件名定义为home-success.jsp,就这样就能到达这个jsp 
	该文件必须在一个content文件夹内,但是可以修改这个文件夹--
	<constant name="struts.convention.result.path" value="/WEB-INF/views/"/>
	 
		<!-- /home.action success →/WEB-INF/content/home-success.jsp
			/home.action input → /WEB-INF/content/home-input.jsp
			/product/home.action success → /WEB-INF/content/product/home-success.jsp -->
	
	<!-- 3.对于请求方法和跳转方式要靠注解来实现 -->
		<!-- 在Homeaction中定义另一种方法 
		@Action("main")加上注解,添加main-success.jsp请求locallhost/main.action-->
			@Action("main")
			public String main() {
				return SUCCESS;
			}
		<!-- 跳转 
				对于params,奇数代表键,偶数个代表值-->
			@Action(value="login",results={
				@Result(name="success",type="redirectAction",params={"namespace","/admin","actionName","main"}),
				@Result(name="input",type="redirectAction",params={"namespace","/admin","actionName","home","error","1001"})
			})
			public String login() {
				Admin currAdmin = adminService.login(admin);
				if(currAdmin == null) {
					return INPUT;
				} else {
					putSession("curr_Admin", currAdmin);
					return SUCCESS;
				}
			}
			
	<!-- 4.对于主配置文件,struts.xml的常量设置,拦截器,global-result还是要自己配置 -->
	但是对于过滤器什么的,convention是存在默认包的.所以要修改才行
	<constant name="struts.convention.default.parent.package" value="basePackage"/>
	
	
	
	
	<!-- 5.命名空间 -->
		com.kaishengit.action.product.HomeAction → /product/home.action
		可以com.kaishengit.action.HomeAction→ /product/home.action
		要在HomeAction上注解@namespace("product")
		这时候请求到达的jsp都要在/content/product/xxxx
	
	
	
	
	
	
	
	</div>






	
</body>
</html>
