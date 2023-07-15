package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao;
	Graph<User, DefaultWeightedEdge> graph;
	List<Archi> archi;
	
	public Model() {
		this.dao = new YelpDao();
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi = new ArrayList<>();
	}
	
	public Graph creaGrafo(int anno, int numRecensioni) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.archi = new ArrayList<>();
		
		Graphs.addAllVertices(this.graph, this.dao.getVertices(numRecensioni));
		
		for(User u : this.graph.vertexSet()) {
			for(User u1 : this.graph.vertexSet()) {
				if(!u.equals(u1)) {
					
					List<Business> b1 = new ArrayList<>(this.dao.getListaLocali(anno, u.getUserId()));
					List<Business> b2 = new ArrayList<>(this.dao.getListaLocali(anno, u1.getUserId()));
					
					List<Business> uguali = new ArrayList<>();
					
					for(Business b : b1) {
						for(Business bus : b2) {
							if(b.getBusinessId().compareTo(bus.getBusinessId())==0) {
								uguali.add(b);
							}
						}
					}
					if(uguali.size()!=0) {
						Graphs.addEdge(this.graph, u, u1, uguali.size());
						this.archi.add(new Archi(u,u1,uguali.size()));
					}
				}
			}
		}
		
		return this.graph;
	}
	
	public List<Archi> pesoMaggiore(User u){
		List<Archi> rest = new ArrayList<>();
		
		int peso = 0;
		
		List<User> getVicini = Graphs.neighborListOf(this.graph, u);
		
		for(User us : getVicini) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(u, us))>= peso) {
				peso = (int) this.graph.getEdgeWeight(this.graph.getEdge(u, us));
			}
		}
		
		for(User us : getVicini) {
			if(this.graph.getEdgeWeight(this.graph.getEdge(u, us))== peso) {
				rest.add(new Archi(u,us,peso));
			}
		}
		
		
		
		return rest;
	}
	
	
}
