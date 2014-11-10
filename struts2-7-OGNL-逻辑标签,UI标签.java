<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>
<!-- 逻辑标签 -->
<div>
	public class IndexAction extends BasicAction{
		
		private static final long serialVersionUID = 1L;

		private String name;
		private List<String> messages;
		private List<ProductType> typeList; 
		
		public String execute() {
			name = "tom";
			
			messages = new ArrayList<String>();
			messages.add("m1");
			messages.add("m2");
			messages.add("m3");
			messages.add("m4");
			
			typeList = new ProductService().findAllType();
			
			return SUCCESS;
		}
	}	
		
		<!-- jsp中 
		要导入<%@ taglib prefix="s" uri="/struts-tags" %>
		-->
		<s:if test="name == 'Tom'">
			<h1>Hello,TOM</h1>
		</s:if>
		<s:elseif test="name == 'tom'">
			<h1>xixi,Tom</h1>
		</s:elseif>
		<s:else>
			<h1>other</h1>
		</s:else>
		
		<ul>
			<s:iterator value="messages" var="ms" status="st">
				<li>${ms} : ${st.even}</li>
			</s:iterator>
		</ul>


</div>


<div>

<!-- UI标签,用作生成表单 了解即可-->
		<s:form action="/product/new.action" theme="simple">
			<s:textfield label="账号" name="product.name" id="pname"/>
			<s:password label="密码" name="product.pwd"/>
			<s:radio label="性别" list="#{'m':'男','w':'女' }" name="xxx"/>
			<s:checkbox label="体育" name="fav" value="ty"/>
			<s:checkbox label="音乐" name="fav" value="ty"/>
			<s:textarea></s:textarea>
			<s:select list="typeList" listKey="id" listValue="typename"></s:select>
			<s:submit></s:submit>
			<s:reset></s:reset>
		</s:form>





</div>









	
</body>
</html>
