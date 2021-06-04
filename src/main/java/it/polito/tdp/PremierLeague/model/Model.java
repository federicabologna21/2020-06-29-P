package it.polito.tdp.PremierLeague.model;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleWeightedGraph<Match, DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer, Match> idMap;
	private List<Match> percorsoMigliore; 
	double pesoMax;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<Integer, Match>();
		dao.listAllMatches(idMap);
	}
	
	public void creaGrafo(int min, int mese) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici 
		Graphs.addAllVertices(grafo, dao.getVertici(mese, idMap));
		
		// aggiungo gli archi 
		for (Adiacenza a: dao.getAdiacenze(min, idMap)) {
			if (this.grafo.containsVertex(a.getM1()) && this.grafo.containsVertex(a.getM2())) {
				DefaultWeightedEdge e = this.grafo.getEdge(a.getM1(), a.getM2());
				
				if (e == null) {
					Graphs.addEdgeWithVertices(grafo, a.getM1(), a.getM2(), a.getPeso());
					
				}
			}
		}
	}
	
	public int getNumeroVertici() {
		if (this.grafo!= null) {
			return this.grafo.vertexSet().size();
		}
		return 0;
	}
	public int getNumeroArchi() {
		if (this.grafo!= null) {
			return this.grafo.edgeSet().size();
		}
		return 0;
	}
	
	public List<CoppieMigliori> getCoppieMigliori(){
		LinkedList<CoppieMigliori> result = new LinkedList<CoppieMigliori>();
		double pesoMax = 0;
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e)>pesoMax) {
				result.clear();
				pesoMax = this.grafo.getEdgeWeight(e);
				Match m1 = this.grafo.getEdgeSource(e);
				Match m2 = this.grafo.getEdgeTarget(e);
				CoppieMigliori coppia = new CoppieMigliori (m1, m2, pesoMax);
				result.add(coppia);
			}
			else if (this.grafo.getEdgeWeight(e) == pesoMax) {
				Match m1 = this.grafo.getEdgeSource(e);
				Match m2 = this.grafo.getEdgeTarget(e);
				CoppieMigliori coppia = new CoppieMigliori (m1, m2, pesoMax);
				result.add(coppia);
			}
		}
		return result;
	}
	
	public Set<Match> getVerticiTendina(){
		return this.grafo.vertexSet();
	}
	
	public List<Match> trovaPercorso(Match sorgente, Match destinazione){
		
		this.percorsoMigliore = new ArrayList<>();
		List<Match> parziale = new ArrayList<>();
		
		parziale.add(sorgente);
		cerca(destinazione, parziale);
		pesoMax =0;
		return this.percorsoMigliore;
		
	}

	private void cerca(Match destinazione, List<Match> parziale) {
		// caso terminale
		if (parziale.get(parziale.size()-1).equals(destinazione)) {
			
				double peso = this.calcolaPeso(parziale);
				if(peso>pesoMax) {
					pesoMax = peso;
				this.percorsoMigliore = new LinkedList<>(parziale);
				}
			
			return ; 
		}
		// altrimenti...
		
 	for (Match vicino : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			
			
			Match m1 = parziale.get(parziale.size()-1);
			Match m2 = vicino;
			if (!parziale.contains(m2)) {
				
				int team1 = m1.getTeamHomeID();
				int team2 = m1.getTeamAwayID();
				
				int t1 = m2.getTeamHomeID();
				int t2 = m2.getTeamAwayID();
				
				
				// team1 != t1, team2 != t2, team1 != t2, team2 != t1
			if ((team1 != t1 && team2 != t2) ||  (team1 != t2 && team2 != t1)) {
				
				
					parziale.add(m2);
					cerca(destinazione, parziale);
					parziale.remove(parziale.size()-1);
				}
			}
			
				
			}
		}

	public double calcolaPeso(List<Match> parziale) {
		double peso = 0;
		for (int i=0; i<parziale.size()-1; i++) {
			Match primo = parziale.get(i);
			Match secondo = parziale.get(i+1);
			peso += grafo.getEdgeWeight(grafo.getEdge(primo, secondo));
			
		}
		return peso;
		
	}
}

