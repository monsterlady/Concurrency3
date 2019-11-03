package nl.saxion.concurrency;
import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import nl.saxion.concurrency.ActorModuel.Broker;
import nl.saxion.concurrency.Moduel.Hotel;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class Main {
    private final Routers allroutes;
    public static List<Hotel> hotelList = new ArrayList<>();

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("TeunKok & Ruikang Xu Assignment3",getConfig(args));
        ActorRef broker = system.actorOf(Props.create(Broker.class,system),"My broker");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        //#server-bootstrapping

        //ActorRef userRegistryActor = system.actorOf(UserRegistryActor.props(), "userRegistryActor");

        //#http-server
        //In order to access all directives we need an instance where the routes are define.
        Main app = new Main(system,broker);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoute().flow(system, materializer);
        http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 3000), materializer);

        System.out.println("Server online at http://localhost:3000/");
        //#http-server
    }

    public Main(ActorSystem actorSystem,ActorRef broker) {
        allroutes = new Routers(actorSystem,broker);
    }

    /**
	Parses the arguments supplied to the program.
	We expect the first argument to be the host name 
	The second argument to be the port to listen on
	*/
    private static Config getConfig(String args[]) {
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

    protected Route createRoute(){
        return allroutes.allroutes();
    }


}