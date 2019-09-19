package com.fabioqmarsiaj.ribbon;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.google.common.collect.Lists;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.LoadBalancerBuilder;
import com.netflix.loadbalancer.LoadBalancerStats;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.reactive.LoadBalancerCommand;
import com.netflix.loadbalancer.reactive.ServerOperation;
import rx.Observable;


public class RibbonLoadBalance {
	
	private final ILoadBalancer loadBalancer;
	
	public RibbonLoadBalance(List<Server> servers) {
		super();
		this.loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(servers);
	}
	
	private String call(final String path) {
	        return LoadBalancerCommand.<String>builder()
	                .withLoadBalancer(loadBalancer)
	                .build()
	                .submit(
                		new ServerOperation<String>() {
                            @Override
                            public Observable<String> call(Server server) {
                                URL url;
                                try {
                                    url = new URL("http://" + server.getHost() + ":" + server.getPort() + path);
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    return Observable.just(conn.toString());
                                } catch (Exception e) {
                                    return Observable.error(e);
                                }
                            }
                        })
	                .toBlocking()
	                .first();
    }
	
	public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }
	
	
	public static void main(String[] args) {
		
		RibbonLoadBalance urlLoadBalancer = new RibbonLoadBalance(Lists.newArrayList(
	                new Server("www.google.com", 80),
	                new Server("www.linkedin.com", 80),
	                new Server("www.yahoo.com", 80)));
	
		for (int i = 0; i < 6; i++) {
		        System.out.println(urlLoadBalancer.call("/"));
		}
		
		System.out.println("======== Load balancer stats ========");

        System.out.println(urlLoadBalancer.getLoadBalancerStats());

	}

}


