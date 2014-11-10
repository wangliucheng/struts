<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>


<!-- 解决用户忘记密码 ,通过发邮件找回-->
	@Action("send")
	public String sendMail() {
		accountServices.sendForgetMail(email);
		return SUCCESS;
	}

<!-- 通过数据库查询输入框中email的账号,有了这个账号再真正发送,
	在线程里的属性要写成常量-->
	public void sendForgetMail(final String email) {
		
		Account account = accountDao.findByemail(email);
		
		if(account != null) {
			<!-- 在发送的邮件中,要传入所找回密码的账户的邮箱,和token
				token的作用是唯一性,防止修改url中的邮箱来乱改密码
				-->
			final String token = UUID.randomUUID().toString();
				<!-- 放入缓存中,5分钟后失效 -->
			Cache.put("forget:"+email, 300, token);
			<!-- 发邮件是个过程,不能等发送完在跳转,将发送放入一个线程 -->
			Thread thread = new Thread(new Runnable() {
				
				public void run() {

					
					HtmlEmail mail = new HtmlEmail();
					<!-- 设置主机服务器地址 -->
					mail.setHostName("smtp.126.com");
					mail.setAuthentication("kaishengit", "p@ssw@rd");
					<!-- 是否需要验证 -->
					mail.setTLS(true);
					mail.setCharset("UTF-8");
					
					try {
					<!-- 发件人 -->
						mail.setFrom("kaishengit@126.com");
						<!-- 主题 -->
						mail.setSubject("密码找回邮件");
						<!-- 内容 -->
						mail.setHtmlMsg("点击此链接找回密码：<a href=\"http://localhost/forget/validate.action?email="+email+"&token="+token+"\">重置密码</a>");
						mail.addTo(email);
						mail.send();
						
					} catch (EmailException e) {
						e.printStackTrace();
					}
				}
			});
			
			thread.start();
		}
		
	}

<!-- 缓存写成工具类 -->
public class Cache {

	private static MemcachedClient client = buildeClient();

	private static MemcachedClient buildeClient() {
		try {
			return new MemcachedClient(AddrUtil.getAddresses("127.0.0.1:11211"));
		} catch (IOException e) {
			throw new RuntimeException("MemcachedClient 获取错误", e);
		}
	}
	
	public static void put(String key,int time,Object value) {
		client.add(key, time, value);
	}
	
	public static Object get(String key) {
		return client.get(key);
	}
	
	public static void remove(String key) {
		client.delete(key);
	}
	
	
}

<!-- 点击邮箱里的找回密码 -->

@Action("validate")
	public String callbackToken() {
		account = accountServices.validateMailAndToken(email,token);
		if(account == null) {
			return INPUT;
		}
		return SUCCESS;
	}

	
	
<!-- service中 -->
public Account validateMailAndToken(String email, String token) {
		String cacheToken = (String) Cache.get("forget:"+email);
		if(cacheToken == null) {
			System.out.println("获取不了token");
			return null;
		} else {
			if(token.equals(cacheToken)) {
				Cache.remove("forget:"+email);
				return accountDao.findByemail(email);
			} else {
				System.out.println("两个token不一致");
				return null;
			}
		}
	}

	
</body>
</html>
