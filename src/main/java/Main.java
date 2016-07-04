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
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yin on 03/07/16.
 */
public class Main {

    final static String owlfile = "src/main/resources/airport/AirportProtege.owl";
    final static String obdafile = "src/main/resources/airport/AirportProtege.obda";


    public static void main(String[] args) {

        OntopConnector connector = OntopConnector.createOntopConnector(owlfile, obdafile);

        try {

            QueryStore.Query query = QueryStore.getAllAirportNamesWithCode("BFT");

            connector.executeQuery(query);

            if (query.hasResult()) {
                Map<String, List<String>> result = query.getResult();

                System.out.println(result.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
