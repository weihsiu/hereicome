package hereicome.kvs

import java.net.{ServerSocket, Socket}

object KvsServer extends App {
  val serverSocket = ServerSocket(3000)
  while (true) {
    val socket = serverSocket.accept
    process(socket)
  }
  def process(socket: Socket): Unit = {
    
  }
}