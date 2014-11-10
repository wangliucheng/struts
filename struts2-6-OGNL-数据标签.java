<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>
<!-- OGNL对象图导航语言,类似于el表达式,strut的底层就是用这个写的
	在导入struts-core的时候会导入ognl.jar-->
<div>

public class Test {

	public static void main(String[] args) throws OgnlException {

	<!-- ognl获取值 -->
		Address address = new Address();
		address.setId(200);
		address.setCity("北京");

		User user = new User();
		user.setAddress(address);
		user.setId(100);
		user.setName("zhangsan");

		System.out.println(Ognl.getValue("address.id", user));
		System.out.println(Ognl.getValue("name", user));




		<!-- 对map操作		-->

		Address address = new Address();
		address.setId(200);
		address.setCity("北京");

		User user = new User();
		user.setAddress(address);
		user.setId(100);
		user.setName("zhangsan");

		Admin admin = new Admin();
		admin.setId(250);
		admin.setUsername("superadmin");
		admin.setPassword("44455666");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("u", user);
		map.put("a", admin);
		<!-- (想要输出的)(上下文)(root) -->
		System.out.println(Ognl.getValue("#root", map, user));
		<!-- 放在map中的admin中的id值 -->
		System.out.println(Ognl.getValue("id", map,admin));
		<!-- map中的u的address的city值 -->
		System.out.println(Ognl.getValue("#u.address.city", map,map));
		<!-- 这里的#root就代表map -->
		System.out.println(Ognl.getValue("#root.u.address.city", map,map));









		<!-- 对list的操作 -->

		Address address1 = new Address();
		address1.setId(100);
		address1.setCity("北京");

		Address address2 = new Address();
		address2.setId(200);
		address2.setCity("南京");


		Address address3 = new Address();
		address3.setId(300);
		address3.setCity("东京");

		List<Address> list = new ArrayList<Address>();
		list.add(address1);
		list.add(address2);
		list.add(address3);

		System.out.println(list.size());
		System.out.println(Ognl.getValue("#root[1].city", list));
		System.out.println(Ognl.getValue("size()", list));
		<!--运行这个方法,并获得返回值输出  -->
		System.out.println(Ognl.getValue("hello()", address1));

	}
}


</div>

<div>

<!-- valuestack -->

public class OgnlAction {

	private String name;
	private String password;
	private List<Address> addressList;
	
	public String execute() {
		ActionContext actionContext = ActionContext.getContext();
		
		
		name = "Rose";
		password = "123123";
		
		User user = new User();
		user.setId(100);
		user.setName("jack");
		<!-- 入栈,首先是把当前action的属性值放进去,然后再放入push的东西 -->
		actionContext.getValueStack().push(user);
		<!-- 放入actionContext 跟入栈是两个不同的位置-->
		actionContext.put("k1", "v1");
		actionContext.put("k2", "v2");
		
		Address address1 = new Address();
		address1.setId(100);
		address1.setCity("北京");
		
		Address address2 = new Address();
		address2.setId(200);
		address2.setCity("南京");
		
		
		Address address3 = new Address();
		address3.setId(300);
		address3.setCity("东京1");
		Address address4 = new Address();
		address4.setId(300);
		address4.setCity("东京2");
		
		addressList = new ArrayList<Address>();
		addressList.add(address1);
		addressList.add(address2);
		addressList.add(address3);
		addressList.add(address4);
		
	
		
		
		
		return "success";
	}
}

<!-- jsp中取值 
要倒入标签
<%@ taglib prefix="s" uri="/struts-tags" %>-->

	<h3>List: <s:property value="addressList"/></h3>
	<h3>List size:<s:property value="addressList.size"/> </h3>
	<h3>List first: <s:property value="addressList[0]"/></h3>
	<h3>city: <s:property value="addressList[0].city"/></h3>
	<h3>city: <s:property value="addressList.{city}[1]"/></h3>
	<h3>city: <s:property value="addressList.{?#this.id == 300}.{city}"/> </h3>
	<h3>city: <s:property value="addressList.{^#this.id == 300}.{city}"/> </h3>
	<h3>city: <s:property value="addressList.{$#this.id == 300}.{city}"/> </h3>
	<h3>list: <s:property value="{'aa','bb','cc'}"/> </h3>
	<h3>map: <s:property value="#{'k1':'v1','k2':'v2' }"/></h3>
	<h3></h3>

	<hr/>
	<!-- 首先是把当前action的属性值放进去,然后再放入push的东西 ,所以出栈的时候
		user是首先出来的,是root[0],然后root[1]指的是action的属性值-->
	<h3>name5: <s:property value="#root[1].name"/></h3>
	<h3>password: <s:property value="password"/> </h3>
	<h3>k1: <s:property value="#request.k1"/> </h3>
	<h3>k2: <s:property value="k2"/></h3>
	<h3>user id : <s:property value="id"/></h3>








</div>



</body>
</html>
