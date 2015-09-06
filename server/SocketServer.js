/**
 * User: nightink
 * Date: 13-1-13
 * Time: 上午3:27
 * socket 服务器
 */

var net = require('net');
var chatServer = net.createServer();
var clientList = [];

// 绑定 net 连接事件
chatServer.on('connection', function(client) {
  // client.remoteAddress 客户端地址 client.remotePost 客户端端口
  // client.name = client.remoteAddress + ':' + client.remotePort;
  client.name = 'java基友' + client.remotePort;
  broadcast('hi,' + client.name + ' join!\r\n', client);
  client.write('hi,' + client.name + '!\r\n');
  clientList.push(client);

  // 客户端讲数据广播发送给其他客户端
  client.on('data', function(data) {
    console.log('客户端发送数据');
    broadcast(client.name + ' say:' + data + '\r\n', client);
  });

  // 用户下线操作
  client.on('end', function() {
    broadcast('hi,' + client.name + ' quit!\r\n', client);
    clientList.splice(clientList.indexOf(client), 1);
  });

  console.log(clientList.length);
  console.log(client.toString());
});

// 客户端数据的发送,用户的下线进行操作
function broadcast(message, client) {
  // 保存已经下线的用户数组
  var cleanup = [];

  for (var i=0, len=clientList.length; i < len; i++) {
    if (client != clientList[i]) {
      if (clientList[i].writable) {
        clientList[i].write(message);
        console.log(message);
      } else {
        // 把下线用户放入下线用户数组中
        cleanup.push(clientList[i]);
        clientList[i].destroy();
      }
    }
  }

  // 针对下线用户从在线用户中清除
  for (var i= 0, len=cleanup.length; i< len; i++) {
    clientList.splice(clientList.indexOf(cleanup[i]), 1);
  }

}

chatServer.listen(7779, function(err) {
  if (err) {
    console.log(err);
    process.exit(2);
  }

  console.log('start 7779');
});
