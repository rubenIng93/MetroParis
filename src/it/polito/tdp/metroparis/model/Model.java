package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private class EdgeTraversedGraphListener implements TraversalListener<Fermata, DefaultEdge>{

		@Override
		public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		}

		@Override
		public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
			
			Fermata sourceVertex = grafo.getEdgeSource(ev.getEdge());
			Fermata targetVertex = grafo.getEdgeTarget(ev.getEdge());
			
			
			if(!backVisit.containsKey(targetVertex) && backVisit.containsKey(sourceVertex)) {
				backVisit.put(targetVertex, sourceVertex);
			}else if(!backVisit.containsKey(sourceVertex) && backVisit.containsKey(targetVertex)) {
				backVisit.put(sourceVertex, targetVertex);
			}
			
			
		}

		@Override
		public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {			
		}

		@Override
		public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {			
		}
		
	}
	
	private Graph <Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;
	private Map<Integer, Fermata> fermateIdMap;
	private Map<Fermata, Fermata> backVisit;
	
	public List<Fermata> getAllFermate() {
		MetroDAO dao = new MetroDAO();
		return dao.getAllFermate();
	}
	
	
	public void creaGrafo() {
		//crea l'oggetto grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		//aggiungi i vertici
		MetroDAO dao = new MetroDAO();
		this.fermate = dao.getAllFermate();
		Graphs.addAllVertices(this.grafo, this.fermate);
		
		//crea idMap
		this.fermateIdMap = new HashMap<>();
		for(Fermata f : this.fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		//aggiungi gli archi
		/*for(Fermata partenza : this.grafo.vertexSet()) {
			for(Fermata arrivo : this.grafo.vertexSet()) {
				
				if(dao.esisteConnessione(partenza, arrivo)) {
					this.grafo.addEdge(partenza, arrivo);
				}
				
			}
		}*/
		
		//aggiungi archi (opzione 2)
		for(Fermata partenza : this.grafo.vertexSet()){
			List<Fermata> arrivi = dao.stazioniArrivo(partenza, fermateIdMap);
			
			for(Fermata arrivo : arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		
		//aggiungi archi (opzione 3) query con stazione arrivo e partenza
		
		
		
		
	}
	
	public List<Fermata> fermateRaggiungibili(Fermata source){
		
		backVisit = new HashMap<>();
		
		List<Fermata> risultato = new ArrayList<>();
		GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, source);//oppure con depth first
		
		it.addTraversalListener(new Model.EdgeTraversedGraphListener());//si può anche istanziare la classe direttamente qui
		
		backVisit.put(source, null);
		
		while(it.hasNext()) {
			risultato.add(it.next());
		}
		
		// System.out.println(backVisit);
		
		return risultato;
	}
	
	public List<Fermata> percorsoFinoA(Fermata target){
		
		if(!backVisit.containsKey(target)) {
			//il target non è raggiungibile dalla source
			return null;
		}
		List<Fermata> percorso = new LinkedList<>();
		
		Fermata f = target;
		
		while(f != null) {
			percorso.add(0, f);
			f = backVisit.get(f);
		}
		
		return percorso;
		
		
	}

	public Graph<Fermata, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Fermata> getFermate() {
		return fermate;
	}
	
	

}
