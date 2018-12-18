package admin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("concept-service")
public class ConceptServiceProperties {
    private String conceptListPath;
    private String conceptsOutputPath;
    private String edgesOutputPath;

    public String getConceptListPath() {
        return conceptListPath;
    }

    public void setConceptListPath(String conceptListPath) {
        this.conceptListPath = conceptListPath;
    }

    public String getConceptsOutputPath() {
        return conceptsOutputPath;
    }

    public void setConceptsOutputPath(String conceptsOutputPath) {
        this.conceptsOutputPath = conceptsOutputPath;
    }

    public String getEdgesOutputPath() {
        return edgesOutputPath;
    }

    public void setEdgesOutputPath(String edgesOutputPath) {
        this.edgesOutputPath = edgesOutputPath;
    }
}
