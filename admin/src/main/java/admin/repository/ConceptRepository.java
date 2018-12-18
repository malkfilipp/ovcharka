package admin.repository;

import admin.domain.Concept;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ConceptRepository extends Neo4jRepository<Concept, Long> {
    Concept findByWord(@Param("word") String word);

    @Query("match (n:Concept) return n")
    Collection<Concept> getConceptList();

    @Query("match (n) detach delete n\n")
    void clear();

    @Query("load csv with headers from \"file:///concepts.csv\" as nodes\n" +
            "create (k:Concept {\n" +
            "word: nodes.word, \n" +
            "definition: nodes.definition, \n" +
            "score: toInteger(nodes.score)\n" +
            "})\n")
    void loadConceptsFromCsv();

    @Query("load csv with headers from \"file:///edges.csv\" as edges\n" +
            "match (a:Concept { word: edges.source})\n" +
            "match (b:Concept { word: edges.target })\n" +
            "create (a)-[w:Weight{weight: toFloat(edges.weight)}]->(b)\n")
    void loadEdgesFromCsv();
}