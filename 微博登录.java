<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Document</title>
</head>
<body>


<!--
基本流程

1. 引导需要授权的用户到如下地址：
https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI

2. 如果用户同意授权,页面跳转至 YOUR_REGISTERED_REDIRECT_URI/?code=CODE

3. 换取Access Token
https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE
（其中client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET可以使用basic方式加入header中）

返回值
{ "access_token":"SlAV32hkKG", "remind_in ":3600, "expires_in":3600 }

4. 使用获得的OAuth2.0 Access Token调用API
	-->
	
	
	
1.	
<a href="https://api.weibo.com/oauth2/authorize?client_id=2516535588&response_type=code&redirect_uri=http://1.itliucheng.sinaapp.com/account/weibo.action">新浪微博登录</a>	


<!--action 中-->
	@Action("weibo")
	public String weibo(){
		account = accountService.weiboLogin(code);
		return SUCCESS;
	}


	
	<!-- service中,使用了工具类 -->
	public Account weiboLogin(String code) {
		String url = "https://api.weibo.com/oauth2/access_token";
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("client_id","2516535588");
		params.put("client_secret","6e90457ae89ff3c2d4c0eae0f04194e1");
		params.put("grant_type","authorization_code");
		params.put("code",code);
		params.put("redirect_uri","http://1.itliucheng.sinaapp.com");
		
		String json = HttpUtil.sendPostRequest(url, params);
		
		HashMap<String, Object> map = new Gson().fromJson(json, HashMap.class);
		String accessToken = (String) map.get("access_token");
		String uid = (String) map.get("uid");
		
		String userInfoUrl = "https://api.weibo.com/2/users/show.json?access_token="+accessToken+"&uid="+uid;
		
		json = HttpUtil.sendGetRequest(userInfoUrl);
		
		map = new Gson().fromJson(json, HashMap.class);
		
		Account account = new Account();
		account.setUsername(map.get("screen_name").toString());
		account.setPic(map.get("avatar_large").toString());
		account.setWeibotoken(accessToken);
		account.setWeibouid(uid);
		
		accountDao.save(account);
		
		account = accountDao.findByUsername(account.getUsername());
		
		return account;
		
		
	}
	
	
	
	
	<!-- 使用的工具类 -->
	public class HttpUtil {

	private static Logger log = Logger.getLogger(HttpUtil.class);
	
	public static String sendPostRequest(String url,Map<String, String> params) {
		String result = null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for (Entry<String, String> entry : params.entrySet()) {
			list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
		}
		
		CloseableHttpResponse resp = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(list));
			
			resp = client.execute(post);
			
			if(resp.getStatusLine().getStatusCode() == 200) {
				InputStream input = resp.getEntity().getContent();
				result = IOUtils.toString(input,"UTF-8");
				input.close();
			} else {
				log.debug("请求"+url+"错误");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}


	public static String sendGetRequest(String url) {
		String result = null;
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(url);
		
		
		CloseableHttpResponse resp = null;
		try {
			
			resp = client.execute(get);
			
			if(resp.getStatusLine().getStatusCode() == 200) {
				InputStream input = resp.getEntity().getContent();
				result = IOUtils.toString(input,"UTF-8");
				input.close();
			} else {
				log.debug("请求"+url+"错误");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(resp != null) {
					resp.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}





}
	
	
	
	
	
	

</body>
</html>
