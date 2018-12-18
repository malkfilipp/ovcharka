package admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin-app")
public class AdminProperties {
    private String inputConceptListFile;

    public String getInputConceptListFile() {
        return inputConceptListFile;
    }

    public void setInputConceptListFile(String inputConceptListFile) {
        this.inputConceptListFile = inputConceptListFile;
    }

    private Neo4jImport neo4jImport;

    public Neo4jImport getNeo4jImport() {
        return neo4jImport;
    }

    public void setNeo4jImport(Neo4jImport neo4jImport) {
        this.neo4jImport = neo4jImport;
    }

    public static class Neo4jImport {
        private String path;
        private String conceptsFilename;
        private String edgesFilename;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getConceptsFilename() {
            return conceptsFilename;
        }

        public void setConceptsFilename(String conceptsFilename) {
            this.conceptsFilename = conceptsFilename;
        }

        public String getEdgesFilename() {
            return edgesFilename;
        }

        public void setEdgesFilename(String edgesFilename) {
            this.edgesFilename = edgesFilename;
        }
    }
}
