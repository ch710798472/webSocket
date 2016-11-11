    var websocket = null;
    var user_id = null;
    var user_name = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8080/websocket");
    }
    else {
        alert('Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("error");
    };

    //连接成功建立的回调方法
    websocket.onopen = function (event) {
        showChatMessage("#chatHistory","<p>tips: 链接服务器成功!</br><p>","chatCenter");
        var json = {
            'type' : "userName",
            'userName' : user_name
        };
        websocket.send(JSON.stringify(json));
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var json = JSON.parse(event.data);
        if(json.type == "onLineCount"){
            countOnlineNumber(json.message);
        }
        if(json.type == "msg"){
            setChatMessage(json.message,json.userName);
        }
        if(json.type == "close"){
            countOnlineNumber(json.message);
            showChatMessage("#chatHistory","<p>tips: "+ json.userName + " 无情的离开了! </br><p>","chatCenter");
        }
        if(json.type == "open"){
            countOnlineNumber(json.message);

        }
        if(json.type == "userId"){
            showId(json.userId);
        }
        if(json.type == "userName"){
            showChatMessage("#chatHistory","<p>tips: "+ json.userName + " 悄悄的来了! </br><p>","chatCenter");
            showName(user_name);
        }
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        //setMessageInnerHTML("系统消息--> 断开与服务器的链接！");
        showChatMessage("#chatHistory","<p>tips: 断开与服务器的链接!</br><p>","chatCenter");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //显示在线人数
    function countOnlineNumber(innerHTML) {
        document.getElementById('count_num').innerHTML = "当前在线人数:"+innerHTML + '<br/>';
    }

    //显示个人ID
    function showId(innerHTML) {
        document.getElementById('user_id').innerHTML = "你的id：" + innerHTML + '<br/>';
    }

    //显示个人昵称
    function showName(innerHTML) {
        document.getElementById('user_name').innerHTML = "你的昵称：" + innerHTML + '<br/>';
    }

    //关闭连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function send() {
            var message = document.getElementById('text').value;
            var json = {
                'type' : "msg",
                'message' : message,
                'userName' : user_name
            };
            websocket.send(JSON.stringify(json));
            //websocket.send(message);
    }

    //录入用户名
    function fillName() {
        var name=prompt("Please enter your name","")
           if (name!=null && name!="")
           {
              user_name = name;
           }
    }

    //显示消息记录
    function setChatMessage(message,name) {
        if(name == user_name){
            var msg = "<p> " + name + ":</br>&nbsp;&nbsp;&nbsp;&nbsp;" + message + "</br></p>";
            showChatMessage("#chatHistory",msg,"chatLeft");
        }else{
            var msg = "<p> 来自< " + name + " >的消息:&nbsp;&nbsp;&nbsp;&nbsp;</br>" + message + "&nbsp;&nbsp;&nbsp;&nbsp;</br></p>";
            showChatMessage("#chatHistory",msg,"chatRight");
        }
    }
    //div添加元素
    function showChatMessage(divId,message,msgClass){
        var $msg = $(message);
        $msg.addClass(msgClass);
        $(divId).append($msg);
    }
    //关闭浏览器页面
    window.onbeforeunload = function beforeClose() {
        if(window.confirm('你确定要离开吗？')){
            return true;
        }else{
            return false;
        }
    }
    //输入框限制
    function textdown(e) {
        textevent = e;
        if (textevent.keyCode == 8) {
            return;
        }
        if (document.getElementById('text').value.length >= 50) {
            alert("大侠，手下留情，此处限字50")
            if (!document.all) {
                textevent.preventDefault();
            } else {
                textevent.returnValue = false;
            }
        }
    }
    function textup() {
        var s = document.getElementById('text').value;
        //判断ID为text的文本区域字数是否超过50个
        if (s.length > 50) {
            document.getElementById('text').value = s.substring(0, 100);
        }
    }