/*
 * #%L
 * ontop-quest-owlapi3
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import it.unibz.inf.ontop.model.OBDAModel;
import it.unibz.inf.ontop.owlrefplatform.core.QuestConstants;
import it.unibz.inf.ontop.owlrefplatform.core.QuestPreferences;
import it.unibz.inf.ontop.owlrefplatform.owlapi.*;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class QuestOWLExample {

    /*
     * Use the sample database using H2 from
     * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
     *
     * Please use the pre-bundled H2 server from the above link
     *
     */
    final String owlfile = "src/main/resources/airport/AirportProtege.owl";
    final String obdafile = "src/main/resources/airport/AirportProtege.obda";

    public void runQuery() throws Exception {

		/*
         * Load the ontology from an external .owl file.
		 */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
		 * Load the OBDA model from an external .obda file
		 */
        final OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
        QuestPreferences preference = new QuestPreferences();
        preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
        QuestOWLConfiguration config = QuestOWLConfiguration.builder()
                .preferences(preference).obdaModel(obdaModel).build();

        QuestOWLFactory factory = new QuestOWLFactory();

        QuestOWL reasoner = factory.createReasoner(ontology, config);

		/*
		 * Prepare the data connection for querying.
		 */
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

		/*
		 * Get the book information that is stored in the database
		 */
        String sparqlQuery =
                "PREFIX : <http://www.omg.org/AirportOrz#>\n" +
                        "SELECT ?c ?name\n" +
                        "WHERE {?c a :Airline; :airlineName ?name;}";

        String sparqlQuery2 = "PREFIX : <http://www.omg.org/AirportOrz#>\n" +
                "SELECT *\n" +
                "WHERE {\n" +
                "?a a :Airport.\n" +
                "?a :airportCity ?c.\n" +
                "?a :airportName ?n.\n" +
                "?c :from ?co.\n" +
                "?co :locatedIn ?r\n" +
                "}";

        try {
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery2);
            int columnSize = rs.getColumnCount();
            final ToStringRenderer renderer = ToStringRenderer.getInstance();
            int total = 0;
            while (rs.nextRow()) {
                total++;
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    System.out.print(renderer.getRendering(binding) + ", ");
                }
                System.out.print("\n");
            }
            System.out.println(total + " instances in total");
            rs.close();

			/*
			 * Print the query summary
			 */
            String sqlQuery = st.getUnfolding(sparqlQuery2);

            System.out.println();
            System.out.println("The input SPARQL query:");
            System.out.println("=======================");
            System.out.println(sparqlQuery);
            System.out.println();

            System.out.println("The output SQL query:");
            System.out.println("=====================");
            System.out.println(sqlQuery);

        } finally {
			/*
			 * Close connection and resources
			 */
            st.close();

            conn.close();

            reasoner.dispose();
        }
    }

    /**
     * Main client program
     */
    public static void main(String[] args) {
        try {
            QuestOWLExample example = new QuestOWLExample();
            example.runQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
