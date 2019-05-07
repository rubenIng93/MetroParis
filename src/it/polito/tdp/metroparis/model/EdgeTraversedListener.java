package it.polito.tdp.metroparis.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

public class EdgeTraversedListener implements TraversalListener<Fermata, DefaultEdge> {
	
	Map<Fermata, Fermata> back;
	Graph<Fermata, DefaultEdge> grafo;
	
	public EdgeTraversedListener(Map<Fermata, Fermata> back, Graph<Fermata, DefaultEdge> grafo) {
		super();
		this.back = back;
		this.grafo = grafo;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> ev) {
		
		/*
		 * mappa dal vertice figlio come chiave, al vertice padre come valore
		 * 
		 * per un nuovo vertice figlio scoperto, devo avere che:
		 * - il figlio è ancora sconosciuto (non ancora trovato)
		 * -il padre è già stato visitato
		 * 
		 */
		Fermata sourceVertex = grafo.getEdgeSource(ev.getEdge());
		Fermata targetVertex = grafo.getEdgeTarget(ev.getEdge());
		
		/* se il grofo è orientato , allora source == padre, target == figlio
		 * altrimenti potrebbe anche essere il contrario...
		 */
		
		if(!back.containsKey(targetVertex) && back.containsKey(sourceVertex)) {
			back.put(targetVertex, sourceVertex);
		}else if(!back.containsKey(sourceVertex) && back.containsKey(targetVertex)) {
			back.put(sourceVertex, targetVertex);
		}
		
 		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Fermata> arg0) {
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Fermata> arg0) {
	}

}
