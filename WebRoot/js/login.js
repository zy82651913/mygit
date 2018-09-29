/**
 * @author 张洋
 * @date 2016-9-17
 * @version v1.0
 * 如果用户名，密码框为空提示；如果用户名有数据按回车键焦点聚焦到密码框，如果用户名，密码框都有数据
 * 按回车键点击登录按键。
 * 火狐浏览器不能用focus获取焦点，只能用select
 */

$(document).ready(function(){
	$("#enter").click(function(){
		$("#user").hide();
		$("#pass").hide();
		$("#cw").hide();
		if ($("#username").val() =='') {
			$("#username").focus();
			$("#user").show();
			return false
		}else if($("#password").val()==''){
			$("#password").focus();
			$("#pass").show();
			return false
		}else{
			panduan();
			$("#password").val("")
			$("#username").select()
			return false
		}
	});
});
	


function panduan () {
	$.ajax({
		type:"get",
		url:"ServletLogin",
		async:true,
		data:$("#slick-login").serialize(),
//		dataType:"text",
//		error:function(request){
//			alert("发送请求失败");
//		},
		success:function(msg){
			if (msg=="false"){ 
				$("#cw").show();
			}else if((msg=="true")){
				var a = $("#username").val();
				var url = "index.html?name=" + a
				window.open(url);
			}else if (msg==""){
				alert("没有连接到服务器！！");
			}
				
			
		},
		error:function(XMLHttpRequest,textStatus,errorThrown){
			alert(XMLHttpRequest.status);
			alert(XMLHttpRequest.readyState);
			alert(textStatus);
		},
//		compete:function(XMLHttpRequest,textStatus){
//			this;
//		}
	
	});
}

