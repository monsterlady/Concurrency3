package nl.saxion.concurrency;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.server.AllDirectives;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.net.InetAddress;


public class Main {

    ActorSystem system;

    public static void main(String[] args) {
        new Main().startUp(args);
    }


    public void startUp(String[] args)  {

        //system = ActorSystem.create(getConfig(args));
        /*String customconfig = getConfig(args);
        Config config = ConfigFactory.parseString(
                customconfig)
                .withFallback(ConfigFactory.load());


        ActorSystem system = ActorSystem.create("TeunKokRuiakngXuAssignment3", config);

//        final ArrayList<Member> memberList = new ArrayList<>();
//        Cluster cluster = Cluster.get(system);
//        for (Member member : cluster.state().getMembers()){
//            if (member.status().equals(MemberStatus.up())){
//                memberList.add(member);
//            }
//        }

        ActorRef broker = system.actorOf(Props.create(Broker.class, system), "broker");

        int nrOfHotels = 10000;
        for (int i = 0; i < nrOfHotels; i++) {
            Hotel h = new Hotel("hotel" + i, 2);
            Broker.getHotelsList().add(h);
            ActorRef hotelManager = system.actorOf(Props.create(HotelManager.class, h), "hotel" + i);
            routers.add(new ActorRefRoutee(hotelManager));
        }
        routerBroker = new Router(new RoundRobinRoutingLogic(), routers);
*/
    }


	/**
	Parses the arguments supplied to the program.
	We expect the first argument to be the host name 
	The second argument to be the port to listen on
	*/
    private Config getConfig(String args[]) {
        String customconfig = "";

        int clusterPort = 0;
        String bindServer = "";
        if (args.length >= 2) {
            clusterPort = Integer.parseInt(args[1]);
        }
        if (args.length >= 1) {
            bindServer = args[0];
        }
        //if clusterPort == 0
        //We are going to use the default config
        //and let AKKA decide wich port to use.

        if (clusterPort > 0)
            customconfig = "\nakka.remote.netty.tcp.port=" + clusterPort ;
		//if bindserver is empty let akka decide the hostname
        if (!bindServer.isEmpty())
            customconfig += "\nakka.remote.netty.tcp.hostname=" + bindServer ;
		
		/* If the environement variable CLUSTER_NAME exists we are operating
		   inside a docker container, and need to behave as if we are in a 
		   NAT network. We need to know our external address: bind-hostname and 
		   bind-port
		*/
        if (System.getenv("CLUSTER_NAME") != null) {
            String localip = "";
            try {
                localip =InetAddress.getLocalHost().getHostAddress().toString();
            } catch (Exception e) {

            }
            customconfig += "\nakka.remote.netty.tcp.bind-hostname=" + localip ;
            customconfig += "\nakka.remote.netty.tcp.bind-port=" + clusterPort ;
            System.out.println("Starting server behing NAT");
        }
        Config config = ConfigFactory.parseString(
                customconfig)
                .withFallback(ConfigFactory.load());
        return config;
    }


}