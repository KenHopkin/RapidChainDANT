package Node;

import java.io.IOException;

import Tcp.Server;

/**
 * Class to run a shifumi server
 * @author Nicolas
 *
 */
public class NodeServer extends Server {
	protected RouteTable routeTable;
	
	public NodeServer(int port, int pool) {
		this(port, pool, 1);
	}

	public NodeServer(int port, int pool, int logLevel) {
		super(port, pool, logLevel);
		this.motd = "Welcome to RapidChain server 12.7";
		this.factory = new NodeServerFactory();
		try {
			this.routeTable = new RouteTable();
			Node node = new Node(new Ip("128.78.51.131"));
			this.routeTable.add(node);
			this.routeTable.save();
			this.routeTable.load();
		} catch (IOException e) {
			this.error(e);
		}
	}
}