package user.repository;

import user.domain.Keyword;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface KeywordRepository extends Neo4jRepository<Keyword, Long> {
    Keyword findByWord(@Param("word") String name);

    @Query("match (n:Keyword) return n")
    Collection<Keyword> graph();

    @Query("match (a:Keyword {word:{word}})-[e:Weight]->(n:Keyword) \n" +
            "with n, e.weight as w \n" +
            "order by w desc\n" +
            "limit 5\n" + // TODO: 15/12/2018 extract 5
            "return n")
    Collection<Keyword> getClosest(@Param("word") String word);
}
