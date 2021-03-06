package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import common.Debuggable;

/**
 * Runnable class to create a server that can send and receive messages to a
 * client
 * 
 * @author Nicolas
 *
 */
public class Server extends Debuggable implements Runnable {
	protected int port;
	protected ThreadPoolExecutor executor;
	protected ServerFactory factory;
	protected int numThread = 0;

	public Server(int port, int poolSize) {
		this.port = port;
		this.prefix = "SERVER";
		this.executor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
		this.factory = new ServerFactory();
	}

	@Override
	public void run() {
		try (ServerSocket s = new ServerSocket(this.port)) {
			this.info("server listening on port: " + this.port);

			while (true) {
				Socket sock = s.accept();
				int number = ++this.numThread;
				this.info("new connection: " + number);

				// management of new connection
				Connection client = this.factory.createConn(sock, this.prefix);
				ConnectionManager cm = this.factory.createConnManager(client, Integer.toString(number), this.prefix);
				this.executor.submit(cm);
				
				// the client is managed separately so we can accept a new connection
			}

		} catch (IOException e) {
			this.error(e);
		}
	}
}
