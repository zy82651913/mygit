<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<title>化验室数据录入系统</title>
		<link rel="shortcut icon" href="favicon.ico" mce_href="favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="jquery-easyui-1.5.3/themes/icon.css">
		
		<script type="text/javascript" src="js/jquery-3.1.0.min.js" charset="utf-8"></script>
		<script type="text/javascript" src="jquery-easyui-1.5.3/jquery.easyui.min.js" charset="utf-8"></script>
		<script type="text/javascript" src="jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
		<!-- <script type="text/javascript" src="js/index.js" charset="utf-8"></script> -->
	<!--解决双滚动条-->
	<style type="text/css">
       	#center-tabs .tabs-panels>.panel>.panel-body {  
	    	overflow: hidden;  
	    } 
    </style>
    <script type="text/javascript">
  //加载完成后运行
    $(document).ready(function(){
    	menu();
    });

    //加载MENU
    function menu(){
    	//获取user名
    	var name="<%=request.getAttribute("name")%>";
    	$.ajax({
    		type:"get",
    		url:"ServletMenu",
    		async:true,
    		data:{name:name},
    		dataType:"json",
    		error:function(XMLHttpRequest,textStatus,errorThrown){
    			alert(XMLHttpRequest.status);
    			alert(XMLHttpRequest.readyState);
    			alert(textStatus);
    		},
    		success:function(data){
    			//console.log(data);//打印服务器返回数据到控制台
    			$.each(data,function(i,val){
    				if(val.ParentID == 0){
    					$('#west-accordion').accordion('add',{
    						title: val.ShowName,
    						selected: false,
    						content: '<div style="padding:5px"><ul id="'+val.NodeName+'"></ul></div>',
    					});
    					ii=val.NodeID;
    					iii=0;
    					$("#"+val.NodeName).tree({  
    							data:data,  
    						    loadFilter: function(data){   
    						        return jsonbl(data);   
    						    },
    						onClick: function(node){//点击数节点触发addTab方法
    							if(node.attributes){//判断url是否存在，存在则创建tabs 
    								addTab(node);
    								}
    							}
    						});  
    				}
    			});
    		},
    	});
    }

    //此处是easyui-tree的json格式  
    var tree = {  
        id:'',  //节点的ID
        text:'',  //节点显示的文字
        state:'',  //节点状态，有两个值  'open' or 'closed', 默认为'open'. 当为‘closed’时说明此节点下有子节点否则此节点为叶子节点
        checked:'',  //指示节点是否被选中。
        attributes:'',  //给一个节点添加的自定义属性。
        children:''  //定义了一些子节点的节点数组。
    } 

    //此处是把后台传过来的json数据转成easyui规定的格式  
    function bl(item){  
        var tree = new Object();
    		tree.id = item.NodeID;  
    	    tree.text = item.ShowName;  
    	    tree.state = 'open';  
    	    tree.checked = 'false';  
    	    tree.attributes = item.NodeUrl; 
        /*if(item.child_menus.length != 0){  
            tree.children = jsonbl(item.child_menus);  
        }else{  
            tree.children = [];  
        }  */
        return tree;  
    }  
    function jsonbl(data){  
    	    var easyTree = [];  
    		
    	    $.each(data,function(index,item){ 
    			if(item.ParentID == ii){ 
    		     	easyTree[iii++] = bl(item);
    			 }  
    	     });  
    	    return easyTree;  
    	}
    //新建Tab业  
    function addTab(node){
    	if ($('#center-tabs').tabs('exists', node.text)){
    		$('#center-tabs').tabs('select', node.text);
    	} else {
    		var content = '<iframe scrolling="auto" frameborder="0"  src="'+node.attributes+'" style="width:100%;height:100%;"></iframe>';
    		$('#center-tabs').tabs('add',{
    			title:node.text,
    			content:content,
    			closable:true
    		});
    	}
    }
    </script>
	</head>
	<body class="easyui-layout">
	<!--页头 -->
	<!-- <div data-options="region:'north'" style="height:50px;"></div> -->
	<!-- 菜单 -->
    <div data-options="region:'west',title:'菜单',split:true" style="width:150px;">
    	<!-- 折叠面板 -->
    	<div id="west-accordion" class="easyui-accordion" data-options="border: false,animate: false,selected:false"></div>
    </div>
    <!-- 内容显示区 -->
    <div  data-options="region:'center',border: false" style="background:#eee;width: auto;height: auto;overflow: hidden;">
    	<!-- 标签页 -->
	    <div id="center-tabs" class="easyui-tabs" style="width:100%;height:100%;">
			
		</div>
    </div>

	</body>
</html>
