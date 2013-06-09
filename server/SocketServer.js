/**
 * User: Nightink
 * Date: 13-1-13
 * Time: 上午3:27
 * socket服务器
 */

var net = require('net'),
    chatServer = net.createServer(),
    clientList = [];

//绑定net 连接事件
chatServer.on('connection', function(client) {
    //client.remoteAddress 客户端地址   client.remotePost 客户端端口
    //client.name = client.remoteAddress + ':' + client.remotePort;
    client.name = 'java基友' + client.remotePort;
    broadcast('hi,' + client.name + ' join!\r\n', client);
    client.write('hi,' + client.name + '!\r\n');
    clientList.push(client);

    client.on('data', function(data) {      //客户端讲数据广播发送给其他客户端
        console.log('客户端发送数据');
        broadcast(client.name + ' say:' + data + '\r\n', client);
    });

    client.on('end', function() {   //用户下线操作
        broadcast('hi,' + client.name + ' quit!\r\n', client);
        clientList.splice(clientList.indexOf(client), 1);
    });

    console.log(clientList.length);
    console.log(client.toString());

});

function broadcast(message, client) {       //客户端数据的发送,用户的下线进行操作
    var cleanup = [];   //保存已经下线的用户数组

    //console.log('向客户端返送数据');

    for(var i=0, len=clientList.length; i < len; i++) {
        if(client != clientList[i]) {
            if(clientList[i].writable) {
                clientList[i].write(message);
                console.log(message);
            } else {
                cleanup.push(clientList[i]);    //把下线用户放入下线用户数组中
                clientList[i].destroy();
            }
        }
    }

    for(var i= 0, len=cleanup.length; i< len; i++) {    //针对下线用户从在线用户中清除
        clientList.splice(clientList.indexOf(cleanup[i]), 1);
    }

}

chatServer.listen(7779);