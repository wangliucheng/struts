<!-- 在表单提交的时候传值是这样,name=admin.username
	name=admin.password,然后在action中定义属性admin生成get和set-->
	
	<!-- 也可以实现ModelDriven这个泛型接口-->
public class HomeAction extends BasicAction implements ModelDriven<Admin>{

	private static final long serialVersionUID = 1L;
		
		<!-- 实现该方法 -->
	public Admin getModel() {
		admin = new Admin();
		return admin;
	}
	
}
这样在jsp中就只需要name=username,name=password就行
