import it.unibz.inf.ontop.exception.InvalidMappingException;
import it.unibz.inf.ontop.exception.InvalidPredicateDeclarationException;
import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.owlrefplatform.core.QuestConstants;
import it.unibz.inf.ontop.owlrefplatform.core.QuestPreferences;
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yin on 04/07/16.
 */
public class OntopConnector {

    private static OntopConnector connector;

    private OWLOntology ontology;
    private QuestOWLConfiguration configuration;
    private QuestOWL reasoner;

    private OntopConnector(String owlfile, String obdafile) {

        try {

            ontology = OWLManager.createOWLOntologyManager().loadOntologyFromOntologyDocument(new File(owlfile));

            OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

            QuestPreferences preference = new QuestPreferences();
            preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

            configuration = QuestOWLConfiguration.builder()
                    .preferences(preference).obdaModel(obdaModel).build();

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidMappingException e) {
            e.printStackTrace();
        } catch (InvalidPredicateDeclarationException e) {
            e.printStackTrace();
        }

    }

    public static OntopConnector createOntopConnector(String owlfile, String obdafile) {
        if (connector == null) {
            return new OntopConnector(owlfile, obdafile);
        } else {
            return connector;
        }
    }

    private void startReasoner() throws Exception{
        reasoner = new QuestOWL(ontology, configuration);
    }

    public void close() {

        try {
            if (reasoner != null) {
                QuestOWLConnection connection = reasoner.getConnection();
                if (connection != null) {
                    connection.close();
                }
                reasoner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(QueryStore.Query query) throws Exception{

        if (reasoner == null) {
            startReasoner();
        }

        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

        QuestOWLResultSet rs = st.executeTuple(query.getSparqlQuery());
        final ToStringRenderer renderer = ToStringRenderer.getInstance();

        List<String> cols = rs.getSignature();
        query.setColumns(cols.toArray(new String[0]));

        while (rs.nextRow()) {
            String[] row = new String[cols.size()];
            for (int i = 0; i < cols.size(); i++) {
                OWLObject binding = rs.getOWLObject(cols.get(i));
                String value = renderer.getRendering(binding);
                row[i] = value;
            }
            query.addRow(row);
        }
        rs.close();

    }


}
